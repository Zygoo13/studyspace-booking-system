import type { ApiResponse } from '../../../shared/types/api'
import { api } from '../../../lib/api'

interface TestResponse {
    message: string
    name: string
}

export const testService = {
    async authenticated(): Promise<TestResponse> {
        const response = await api.get<ApiResponse<TestResponse>>('/api/test/authenticated')
        if (!response.data.success || !response.data.data) {
            throw new Error(response.data.message || 'Authenticated test failed')
        }
        return response.data.data
    },

    async staff(): Promise<TestResponse> {
        const response = await api.get<ApiResponse<TestResponse>>('/api/test/staff')
        if (!response.data.success || !response.data.data) {
            throw new Error(response.data.message || 'Staff test failed')
        }
        return response.data.data
    },

    async admin(): Promise<TestResponse> {
        const response = await api.get<ApiResponse<TestResponse>>('/api/test/admin')
        if (!response.data.success || !response.data.data) {
            throw new Error(response.data.message || 'Admin test failed')
        }
        return response.data.data
    }
}