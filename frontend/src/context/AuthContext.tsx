import {
    createContext,
    useCallback,
    useContext,
    useEffect,
    useMemo,
    useState,
    type ReactNode,
} from 'react'
import { authService } from '../features/auth/services/authService.ts'
import type { AuthUser } from '../features/auth/types/auth'
import { tokenStorage } from '../lib/token'

interface AuthContextValue {
    user: AuthUser | null
    isAuthenticated: boolean
    isLoading: boolean
    login: (email: string, password: string) => Promise<void>
    logout: () => Promise<void>
    refreshMe: () => Promise<void>
}

const AuthContext = createContext<AuthContextValue | undefined>(undefined)

interface Props {
    children: ReactNode
}

export function AuthProvider({ children }: Props) {
    const [user, setUser] = useState<AuthUser | null>(null)
    const [isLoading, setIsLoading] = useState(true)

    const refreshMe = useCallback(async () => {
        try {
            const me = await authService.getMe()
            setUser(me)
        } catch {
            tokenStorage.clear()
            setUser(null)
            throw new Error('Unable to refresh current user')
        }
    }, [])

    const bootstrapAuth = useCallback(async () => {
        const accessToken = tokenStorage.getAccessToken()

        if (!accessToken) {
            setUser(null)
            setIsLoading(false)
            return
        }

        try {
            const me = await authService.getMe()
            setUser(me)
        } catch {
            tokenStorage.clear()
            setUser(null)
        } finally {
            setIsLoading(false)
        }
    }, [])

    useEffect(() => {
        void bootstrapAuth()
    }, [bootstrapAuth])

    const login = useCallback(async (email: string, password: string) => {
        const tokens = await authService.login({ email, password })
        tokenStorage.setTokens(tokens.accessToken, tokens.refreshToken)

        const me = await authService.getMe()
        setUser(me)
    }, [])

    const logout = useCallback(async () => {
        const refreshToken = tokenStorage.getRefreshToken()

        try {
            if (refreshToken) {
                await authService.logout(refreshToken)
            }
        } catch {
            // ignore API error on logout
        } finally {
            tokenStorage.clear()
            setUser(null)
        }
    }, [])

    const value = useMemo<AuthContextValue>(
        () => ({
            user,
            isAuthenticated: !!user,
            isLoading,
            login,
            logout,
            refreshMe,
        }),
        [user, isLoading, login, logout, refreshMe],
    )

    return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

export function useAuth() {
    const context = useContext(AuthContext)

    if (!context) {
        throw new Error('useAuth must be used within AuthProvider')
    }

    return context
}