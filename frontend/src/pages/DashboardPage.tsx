import { useAuth } from '../context/AuthContext'

export default function DashboardPage() {
    const { user } = useAuth()

    return (
        <div style={styles.page}>
            <h1 style={styles.title}>Dashboard</h1>
            <p style={styles.subtitle}>Foundation template đã đăng nhập thành công.</p>

            <div style={styles.card}>
                <div><strong>Name:</strong> {user?.fullName}</div>
                <div><strong>Email:</strong> {user?.email}</div>
                <div><strong>Role:</strong> {user?.role}</div>
                <div><strong>Active:</strong> {String(user?.active)}</div>
            </div>
        </div>
    )
}

const styles: Record<string, React.CSSProperties> = {
    page: {
        display: 'grid',
        gap: 16,
    },
    title: {
        margin: 0,
    },
    subtitle: {
        margin: 0,
        color: '#6b7280',
    },
    card: {
        padding: 20,
        borderRadius: 12,
        background: '#ffffff',
        border: '1px solid #e5e7eb',
        display: 'grid',
        gap: 12,
    },
}