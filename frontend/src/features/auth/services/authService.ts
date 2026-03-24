import type {
    AuthTokenResponse,
    AuthUser,
    LoginRequest,
    RefreshTokenRequest,
} from '../types/auth'
import type { ApiResponse } from '../../../shared/types/api'
import { api } from '../../../lib/api'

export const authService = {
    async login(payload: LoginRequest): Promise<AuthTokenResponse> {
        const response = await api.post<ApiResponse<AuthTokenResponse>>('/api/auth/login', payload)

        if (!response.data.success || !response.data.data) {
            throw new Error(response.data.message || 'Login failed')
        }

        return response.data.data
    },

    async refresh(payload: RefreshTokenRequest): Promise<AuthTokenResponse> {
        const response = await api.post<ApiResponse<AuthTokenResponse>>('/api/auth/refresh', payload)

        if (!response.data.success || !response.data.data) {
            throw new Error(response.data.message || 'Refresh token failed')
        }

        return response.data.data
    },

    async logout(refreshToken: string): Promise<void> {
        await api.post<ApiResponse<null>>('/api/auth/logout', { refreshToken })
    },

    async getMe(): Promise<AuthUser> {
        const response = await api.get<ApiResponse<AuthUser>>('/api/users/me')

        if (!response.data.success || !response.data.data) {
            throw new Error(response.data.message || 'Get current user failed')
        }

        return response.data.data
    },
}