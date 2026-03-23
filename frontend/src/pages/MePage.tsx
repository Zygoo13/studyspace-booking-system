import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

export default function MePage() {
    const { user, refreshMe, logout } = useAuth()
    const [isRefreshing, setIsRefreshing] = useState(false)

    useEffect(() => {
        if (!user) {
            void handleRefresh()
        }
    }, [])

    const handleRefresh = async () => {
        setIsRefreshing(true)
        try {
            await refreshMe()
        } finally {
            setIsRefreshing(false)
        }
    }

    return (
        <div style={styles.page}>
            <div style={styles.card}>
                <h1>Current User</h1>

                <div style={styles.box}>
                    <pre style={styles.pre}>
                        {JSON.stringify(user, null, 2)}
                    </pre>
                </div>

                <div style={styles.actions}>
                    <button onClick={() => void handleRefresh()} style={styles.button}>
                        {isRefreshing ? 'Refreshing...' : 'Refresh /me'}
                    </button>

                    <button onClick={() => void logout()} style={styles.button}>
                        Logout
                    </button>

                    <Link to="/" style={styles.linkButton}>
                        Home
                    </Link>
                </div>
            </div>
        </div>
    )
}

const styles: Record<string, React.CSSProperties> = {
    page: {
        minHeight: '100vh',
        display: 'grid',
        placeItems: 'center',
        background: '#f8fafc',
        padding: 16,
        fontFamily: 'Arial, sans-serif'
    },
    card: {
        width: '100%',
        maxWidth: 720,
        background: '#fff',
        padding: 24,
        borderRadius: 12,
        boxShadow: '0 10px 30px rgba(0,0,0,0.08)'
    },
    box: {
        marginTop: 16,
        padding: 16,
        borderRadius: 8,
        background: '#0f172a',
        color: '#e2e8f0',
        overflowX: 'auto'
    },
    pre: {
        margin: 0
    },
    actions: {
        display: 'flex',
        gap: 12,
        marginTop: 20,
        flexWrap: 'wrap'
    },
    button: {
        padding: '10px 14px',
        border: 'none',
        borderRadius: 8,
        background: '#111827',
        color: '#fff',
        cursor: 'pointer'
    },
    linkButton: {
        padding: '10px 14px',
        borderRadius: 8,
        background: '#2563eb',
        color: '#fff',
        textDecoration: 'none'
    }
}