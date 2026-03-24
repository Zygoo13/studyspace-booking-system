export interface UserSummary {
  id: number
  fullName: string
  email: string
  phone: string
  role: string
  active: boolean
}

export interface UserDetail {
  id: number
  fullName: string
  email: string
  phone: string
  role: string
  active: boolean
  createdAt: string
  updatedAt: string
}

export interface UpdateUserStatusRequest {
  active: boolean
}