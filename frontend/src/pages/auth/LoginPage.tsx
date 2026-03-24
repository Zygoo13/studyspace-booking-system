import { LockOutlined, MailOutlined } from '@ant-design/icons'
import { Alert, Button, Card, Form, Input, Typography } from 'antd'
import { useState } from 'react'
import { Navigate, useLocation, useNavigate } from 'react-router-dom'
import { useAuth } from '../../context/AuthContext'

const { Title, Paragraph } = Typography

export default function LoginPage() {
    const navigate = useNavigate()
    const location = useLocation()
    const { login, isAuthenticated } = useAuth()

    const [error, setError] = useState('')
    const [isSubmitting, setIsSubmitting] = useState(false)

    const from = (location.state as { from?: { pathname?: string } } | null)?.from?.pathname || '/'

    if (isAuthenticated) {
        return <Navigate to="/" replace />
    }

    const handleFinish = async (values: { email: string; password: string }) => {
        setError('')
        setIsSubmitting(true)

        try {
            await login(values.email.trim(), values.password)
            navigate(from, { replace: true })
        } catch (err) {
            setError(err instanceof Error ? err.message : 'Login failed')
        } finally {
            setIsSubmitting(false)
        }
    }

    return (
        <div
            style={{
                minHeight: '100vh',
                display: 'grid',
                placeItems: 'center',
                background: '#f5f7fb',
                padding: 16,
            }}
        >
            <Card style={{ width: 420, borderRadius: 16 }}>
                <Title level={2} style={{ marginBottom: 8 }}>
                    StudySpace Login
                </Title>
                <Paragraph type="secondary">Login to access Phase 1 template</Paragraph>

                {error ? <Alert type="error" message={error} showIcon style={{ marginBottom: 16 }} /> : null}

                <Form
                    layout="vertical"
                    initialValues={{
                        email: 'admin@studyspace.com',
                        password: 'password',
                    }}
                    onFinish={(values) => void handleFinish(values)}
                >
                    <Form.Item
                        label="Email"
                        name="email"
                        rules={[
                            { required: true, message: 'Please enter email' },
                            { type: 'email', message: 'Invalid email' },
                        ]}
                    >
                        <Input prefix={<MailOutlined />} placeholder="Enter email" />
                    </Form.Item>

                    <Form.Item
                        label="Password"
                        name="password"
                        rules={[{ required: true, message: 'Please enter password' }]}
                    >
                        <Input.Password prefix={<LockOutlined />} placeholder="Enter password" />
                    </Form.Item>

                    <Button type="primary" htmlType="submit" loading={isSubmitting} block>
                        Login
                    </Button>
                </Form>
            </Card>
        </div>
    )
}