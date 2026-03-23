import { Navigate, Outlet, useLocation } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

interface Props {
    allowedRoles?: string[]
}

export default function ProtectedRoute({ allowedRoles }: Props) {
    const { user, isAuthenticated, isLoading } = useAuth()
    const location = useLocation()

    if (isLoading) {
        return <div style={styles.center}>Loading...</div>
    }

    if (!isAuthenticated) {
        return <Navigate to="/login" replace state={{ from: location }} />
    }

    if (allowedRoles && allowedRoles.length > 0) {
        const hasRole = user?.role && allowedRoles.includes(user.role)
        if (!hasRole) {
            return <Navigate to="/" replace />
        }
    }

    return <Outlet />
}

const styles: Record<string, React.CSSProperties> = {
    center: {
        minHeight: '100vh',
        display: 'grid',
        placeItems: 'center',
        fontFamily: 'Arial, sans-serif'
    }
}