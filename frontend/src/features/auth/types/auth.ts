export interface AuthTokenResponse {
    accessToken: string
    refreshToken: string
    tokenType: string
    expiresIn: number
}

export interface LoginRequest {
    email: string
    password: string
}

export interface LogoutRequest {
    refreshToken: string
}

export interface RefreshTokenRequest {
    refreshToken: string
}

export interface AuthUser {
    id: number
    fullName: string
    email: string
    role: string
    active: boolean
}