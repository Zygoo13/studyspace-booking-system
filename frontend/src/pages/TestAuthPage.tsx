import { useState } from 'react'
import { Link } from 'react-router-dom'
import { testService } from '../features/auth/services/testService'

export default function TestAuthPage() {
    const [result, setResult] = useState<unknown>(null)
    const [error, setError] = useState('')
    const [loading, setLoading] = useState(false)

    const handleCall = async () => {
        setLoading(true)
        setError('')
        try {
            const data = await testService.authenticated()
            setResult(data)
        } catch (err) {
            setResult(null)
            setError(err instanceof Error ? err.message : 'Call failed')
        } finally {
            setLoading(false)
        }
    }

    return (
        <div style={styles.page}>
            <div style={styles.card}>
                <h1>Authenticated Test</h1>
                <p>Gọi backend: /api/test/authenticated</p>

                <div style={styles.actions}>
                    <button onClick={() => void handleCall()} style={styles.button}>
                        {loading ? 'Calling...' : 'Call API'}
                    </button>
                    <Link to="/" style={styles.linkButton}>
                        Home
                    </Link>
                </div>

                {error ? <div style={styles.error}>{error}</div> : null}

                <div style={styles.box}>
                    <pre style={styles.pre}>{JSON.stringify(result, null, 2)}</pre>
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
    actions: {
        display: 'flex',
        gap: 12,
        marginTop: 16,
        marginBottom: 16,
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
    },
    error: {
        padding: 12,
        borderRadius: 8,
        background: '#fef2f2',
        color: '#b91c1c',
        marginBottom: 16
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
    }
}