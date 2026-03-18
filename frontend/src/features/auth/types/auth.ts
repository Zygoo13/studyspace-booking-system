export interface LoginRequest {
    email: string
    password: string
}

export interface AuthUser {
    id: number
    fullName: string
    email: string
    role: string
}

export interface LoginResponseData {
    accessToken: string
    refreshToken: string
    user: AuthUser
}