import { Navigate, Outlet } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

interface Props {
    allowedRoles: string[]
}

export default function RoleRoute({ allowedRoles }: Props) {
    const { user, isLoading } = useAuth()

    if (isLoading) {
        return <div style={styles.center}>Loading...</div>
    }

    if (!user) {
        return <Navigate to="/login" replace />
    }

    if (!allowedRoles.includes(user.role)) {
        return <Navigate to="/" replace />
    }

    return <Outlet />
}

const styles: Record<string, React.CSSProperties> = {
    center: {
        minHeight: '100vh',
        display: 'grid',
        placeItems: 'center',
        fontFamily: 'Arial, sans-serif',
    },
}