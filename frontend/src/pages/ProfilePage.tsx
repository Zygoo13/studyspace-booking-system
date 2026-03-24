import { useState } from 'react'
import { useAuth } from '../context/AuthContext'

export default function ProfilePage() {
    const { user, refreshMe } = useAuth()
    const [isRefreshing, setIsRefreshing] = useState(false)
    const [error, setError] = useState('')

    const handleRefresh = async () => {
        setIsRefreshing(true)
        setError('')

        try {
            await refreshMe()
        } catch (err) {
            setError(err instanceof Error ? err.message : 'Refresh failed')
        } finally {
            setIsRefreshing(false)
        }
    }

    return (
        <div style={styles.page}>
            <div style={styles.header}>
                <h1 style={styles.title}>Profile</h1>
                <button onClick={() => void handleRefresh()} style={styles.button}>
                    {isRefreshing ? 'Refreshing...' : 'Refresh'}
                </button>
            </div>

            {error ? <div style={styles.error}>{error}</div> : null}

            <div style={styles.card}>
                <pre style={styles.pre}>{JSON.stringify(user, null, 2)}</pre>
            </div>
        </div>
    )
}

const styles: Record<string, React.CSSProperties> = {
    page: {
        display: 'grid',
        gap: 16,
    },
    header: {
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',
    },
    title: {
        margin: 0,
    },
    button: {
        padding: '10px 14px',
        border: 'none',
        borderRadius: 8,
        background: '#111827',
        color: '#ffffff',
        cursor: 'pointer',
    },
    error: {
        padding: 12,
        borderRadius: 8,
        background: '#fef2f2',
        color: '#b91c1c',
    },
    card: {
        padding: 16,
        borderRadius: 12,
        background: '#111827',
        color: '#e5e7eb',
        overflowX: 'auto',
    },
    pre: {
        margin: 0,
    },
}