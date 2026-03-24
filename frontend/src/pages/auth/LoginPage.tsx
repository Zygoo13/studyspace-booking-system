import { useState, type FormEvent } from 'react'
import { Navigate, useLocation, useNavigate } from 'react-router-dom'
import { useAuth } from '../../context/AuthContext'

export default function LoginPage() {
    const navigate = useNavigate()
    const location = useLocation()
    const { login, isAuthenticated } = useAuth()

    const [email, setEmail] = useState('admin@studyspace.com')
    const [password, setPassword] = useState('password')
    const [error, setError] = useState('')
    const [isSubmitting, setIsSubmitting] = useState(false)

    const from = (location.state as { from?: { pathname?: string } } | null)?.from?.pathname || '/'

    if (isAuthenticated) {
        return <Navigate to="/" replace />
    }

    const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault()
        setError('')
        setIsSubmitting(true)

        try {
            await login(email.trim(), password)
            navigate(from, { replace: true })
        } catch (err: unknown) {
            setError(err instanceof Error ? err.message : 'Login failed')
        } finally {
            setIsSubmitting(false)
        }
    }

    return (
        <div style={styles.page}>
            <div style={styles.card}>
                <h1 style={styles.title}>StudySpace Login</h1>
                <p style={styles.subtitle}>Login to access Phase 1 template</p>

                <form onSubmit={handleSubmit} style={styles.form}>
                    <div style={styles.field}>
                        <label htmlFor="email">Email</label>
                        <input
                            id="email"
                            type="email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            placeholder="Enter email"
                            style={styles.input}
                            required
                        />
                    </div>

                    <div style={styles.field}>
                        <label htmlFor="password">Password</label>
                        <input
                            id="password"
                            type="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            placeholder="Enter password"
                            style={styles.input}
                            required
                        />
                    </div>

                    {error ? <div style={styles.error}>{error}</div> : null}

                    <button type="submit" style={styles.button} disabled={isSubmitting}>
                        {isSubmitting ? 'Signing in...' : 'Login'}
                    </button>
                </form>
            </div>
        </div>
    )
}

const styles: Record<string, React.CSSProperties> = {
    page: {
        minHeight: '100vh',
        display: 'grid',
        placeItems: 'center',
        background: '#f5f7fb',
        padding: 16,
        fontFamily: 'Arial, sans-serif',
    },
    card: {
        width: '100%',
        maxWidth: 420,
        background: '#fff',
        padding: 24,
        borderRadius: 12,
        boxShadow: '0 10px 30px rgba(0,0,0,0.08)',
    },
    title: {
        margin: '0 0 8px',
    },
    subtitle: {
        margin: '0 0 20px',
        color: '#666',
    },
    form: {
        display: 'grid',
        gap: 16,
    },
    field: {
        display: 'grid',
        gap: 8,
    },
    input: {
        padding: '12px 14px',
        border: '1px solid #d0d7de',
        borderRadius: 8,
        fontSize: 14,
    },
    button: {
        padding: '12px 14px',
        border: 'none',
        borderRadius: 8,
        background: '#111827',
        color: '#fff',
        cursor: 'pointer',
        fontSize: 14,
    },
    error: {
        padding: 12,
        borderRadius: 8,
        background: '#fef2f2',
        color: '#b91c1c',
        fontSize: 14,
    },
}