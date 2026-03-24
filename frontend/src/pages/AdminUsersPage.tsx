import { useEffect, useState } from 'react'
import { userService } from '../features/users/services/userService'
import type { UserSummary } from '../features/users/types/user'

export default function AdminUsersPage() {
    const [users, setUsers] = useState<UserSummary[]>([])
    const [isLoading, setIsLoading] = useState(true)
    const [error, setError] = useState('')

    const loadUsers = async () => {
        setIsLoading(true)
        setError('')

        try {
            const data = await userService.getUsers()
            setUsers(data)
        } catch (err) {
            setError(err instanceof Error ? err.message : 'Load users failed')
        } finally {
            setIsLoading(false)
        }
    }

    useEffect(() => {
        void loadUsers()
    }, [])

    const handleToggleStatus = async (user: UserSummary) => {
        try {
            const updated = await userService.updateUserStatus(user.id, !user.active)

            setUsers((prev) =>
                prev.map((item) =>
                    item.id === updated.id ? { ...item, active: updated.active } : item,
                ),
            )
        } catch (err) {
            setError(err instanceof Error ? err.message : 'Update status failed')
        }
    }

    if (isLoading) {
        return <div style={styles.info}>Loading users...</div>
    }

    return (
        <div style={styles.page}>
            <div style={styles.header}>
                <h1 style={styles.title}>Admin Users</h1>
                <button onClick={() => void loadUsers()} style={styles.button}>
                    Reload
                </button>
            </div>

            {error ? <div style={styles.error}>{error}</div> : null}

            <div style={styles.tableWrap}>
                <table style={styles.table}>
                    <thead>
                        <tr>
                            <th style={styles.th}>ID</th>
                            <th style={styles.th}>Full Name</th>
                            <th style={styles.th}>Email</th>
                            <th style={styles.th}>Phone</th>
                            <th style={styles.th}>Role</th>
                            <th style={styles.th}>Active</th>
                            <th style={styles.th}>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        {users.map((user) => (
                            <tr key={user.id}>
                                <td style={styles.td}>{user.id}</td>
                                <td style={styles.td}>{user.fullName}</td>
                                <td style={styles.td}>{user.email}</td>
                                <td style={styles.td}>{user.phone}</td>
                                <td style={styles.td}>{user.role}</td>
                                <td style={styles.td}>{user.active ? 'Active' : 'Inactive'}</td>
                                <td style={styles.td}>
                                    <button
                                        onClick={() => void handleToggleStatus(user)}
                                        style={styles.actionButton}
                                    >
                                        {user.active ? 'Deactivate' : 'Activate'}
                                    </button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
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
    info: {
        padding: 16,
        fontFamily: 'Arial, sans-serif',
    },
    error: {
        padding: 12,
        borderRadius: 8,
        background: '#fef2f2',
        color: '#b91c1c',
    },
    tableWrap: {
        overflowX: 'auto',
        background: '#ffffff',
        borderRadius: 12,
        border: '1px solid #e5e7eb',
    },
    table: {
        width: '100%',
        borderCollapse: 'collapse',
    },
    th: {
        textAlign: 'left',
        padding: 12,
        borderBottom: '1px solid #e5e7eb',
        background: '#f9fafb',
    },
    td: {
        padding: 12,
        borderBottom: '1px solid #f1f5f9',
    },
    actionButton: {
        padding: '8px 12px',
        border: 'none',
        borderRadius: 8,
        background: '#2563eb',
        color: '#ffffff',
        cursor: 'pointer',
    },
}