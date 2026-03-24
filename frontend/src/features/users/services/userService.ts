import { api } from '../../../lib/api'
import type { ApiResponse } from '../../../shared/types/api'
import type { UserDetail, UserSummary } from '../types/user'

export const userService = {
    async getUsers(): Promise<UserSummary[]> {
        const response = await api.get<ApiResponse<UserSummary[]>>('/api/admin/users')

        if (!response.data.success || !response.data.data) {
            throw new Error(response.data.message || 'Get users failed')
        }

        return response.data.data
    },

    async getUserById(id: number): Promise<UserDetail> {
        const response = await api.get<ApiResponse<UserDetail>>(`/api/admin/users/${id}`)

        if (!response.data.success || !response.data.data) {
            throw new Error(response.data.message || 'Get user failed')
        }

        return response.data.data
    },

    async updateUserStatus(id: number, active: boolean): Promise<UserDetail> {
        const response = await api.patch<ApiResponse<UserDetail>>(`/api/admin/users/${id}/status`, {
            active,
        })

        if (!response.data.success || !response.data.data) {
            throw new Error(response.data.message || 'Update user status failed')
        }

        return response.data.data
    },
}