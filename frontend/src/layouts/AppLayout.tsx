import { Link, Outlet, useLocation } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

export default function AppLayout() {
    const { user, logout } = useAuth()
    const location = useLocation()

    return (
        <div style={styles.page}>
            <aside style={styles.sidebar}>
                <div style={styles.brand}>StudySpace</div>

                <nav style={styles.nav}>
                    <Link style={linkStyle(location.pathname === '/')} to="/">
                        Dashboard
                    </Link>

                    <Link style={linkStyle(location.pathname === '/profile')} to="/profile">
                        Profile
                    </Link>

                    {user?.role === 'ADMIN' ? (
                        <Link style={linkStyle(location.pathname === '/admin/users')} to="/admin/users">
                            Users
                        </Link>
                    ) : null}
                </nav>

                <div style={styles.footer}>
                    <div style={styles.userBox}>
                        <div style={styles.userName}>{user?.fullName}</div>
                        <div style={styles.userRole}>{user?.role}</div>
                    </div>

                    <button onClick={() => void logout()} style={styles.logoutButton}>
                        Logout
                    </button>
                </div>
            </aside>

            <main style={styles.main}>
                <Outlet />
            </main>
        </div>
    )
}

function linkStyle(active: boolean): React.CSSProperties {
    return {
        display: 'block',
        padding: '10px 12px',
        borderRadius: 8,
        textDecoration: 'none',
        color: active ? '#ffffff' : '#111827',
        background: active ? '#111827' : 'transparent',
    }
}

const styles: Record<string, React.CSSProperties> = {
    page: {
        minHeight: '100vh',
        display: 'grid',
        gridTemplateColumns: '240px 1fr',
        background: '#f8fafc',
        fontFamily: 'Arial, sans-serif',
    },
    sidebar: {
        padding: 20,
        background: '#ffffff',
        borderRight: '1px solid #e5e7eb',
        display: 'flex',
        flexDirection: 'column',
        gap: 20,
    },
    brand: {
        fontSize: 20,
        fontWeight: 700,
    },
    nav: {
        display: 'grid',
        gap: 8,
    },
    footer: {
        marginTop: 'auto',
        display: 'grid',
        gap: 12,
    },
    userBox: {
        padding: 12,
        borderRadius: 8,
        background: '#f3f4f6',
    },
    userName: {
        fontWeight: 600,
    },
    userRole: {
        fontSize: 13,
        color: '#6b7280',
    },
    logoutButton: {
        padding: '10px 12px',
        border: 'none',
        borderRadius: 8,
        background: '#111827',
        color: '#ffffff',
        cursor: 'pointer',
    },
    main: {
        padding: 24,
    },
}