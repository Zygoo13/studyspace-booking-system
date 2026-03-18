import { api } from '../../../services/api.ts'
import type { ApiResponse } from '../../../shared/types/api.ts'
import type { LoginRequest, LoginResponseData } from '../types/auth.ts'

export async function login(payload: LoginRequest): Promise<ApiResponse<LoginResponseData>> {
    const response = await api.post<ApiResponse<LoginResponseData>>('/api/auth/login', payload)
    return response.data
}