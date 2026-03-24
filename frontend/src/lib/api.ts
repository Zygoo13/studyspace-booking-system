import axios, { AxiosError, type InternalAxiosRequestConfig } from 'axios'
import { tokenStorage } from './token'

const baseURL = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080'

export const api = axios.create({
    baseURL,
    headers: {
        'Content-Type': 'application/json',
    },
})

let isRefreshing = false
let pendingQueue: Array<(token: string | null) => void> = []

const processQueue = (token: string | null) => {
    pendingQueue.forEach((callback) => callback(token))
    pendingQueue = []
}

api.interceptors.request.use((config: InternalAxiosRequestConfig) => {
    const accessToken = tokenStorage.getAccessToken()

    if (accessToken) {
        config.headers.Authorization = `Bearer ${accessToken}`
    }

    return config
})

api.interceptors.response.use(
    (response) => response,
    async (error: AxiosError) => {
        const originalRequest = error.config as InternalAxiosRequestConfig & { _retry?: boolean }

        if (!error.response || !originalRequest) {
            return Promise.reject(error)
        }

        const isUnauthorized = error.response.status === 401
        const isRefreshRequest = originalRequest.url?.includes('/api/auth/refresh')

        if (!isUnauthorized || isRefreshRequest || originalRequest._retry) {
            return Promise.reject(error)
        }

        const refreshToken = tokenStorage.getRefreshToken()

        if (!refreshToken) {
            tokenStorage.clear()
            return Promise.reject(error)
        }

        if (isRefreshing) {
            return new Promise((resolve, reject) => {
                pendingQueue.push((newAccessToken) => {
                    if (!newAccessToken) {
                        reject(error)
                        return
                    }

                    originalRequest.headers.Authorization = `Bearer ${newAccessToken}`
                    resolve(api(originalRequest))
                })
            })
        }

        originalRequest._retry = true
        isRefreshing = true

        try {
            const refreshResponse = await axios.post(`${baseURL}/api/auth/refresh`, {
                refreshToken,
            })

            const responseData = refreshResponse.data?.data
            const newAccessToken = responseData?.accessToken as string | undefined
            const newRefreshToken = responseData?.refreshToken as string | undefined

            if (!newAccessToken || !newRefreshToken) {
                throw new Error('Invalid refresh response')
            }

            tokenStorage.setTokens(newAccessToken, newRefreshToken)
            processQueue(newAccessToken)

            originalRequest.headers.Authorization = `Bearer ${newAccessToken}`
            return api(originalRequest)
        } catch (refreshError) {
            processQueue(null)
            tokenStorage.clear()
            return Promise.reject(refreshError)
        } finally {
            isRefreshing = false
        }
    },
)