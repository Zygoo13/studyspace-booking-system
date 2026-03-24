import { DashboardOutlined, LogoutOutlined, TeamOutlined, UserOutlined } from '@ant-design/icons'
import { Button, Layout, Menu, Space, Typography } from 'antd'
import type { MenuProps } from 'antd'
import { Link, Outlet, useLocation, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

const { Sider, Content } = Layout
const { Title, Text } = Typography

export default function AppLayout() {
    const { user, logout } = useAuth()
    const location = useLocation()
    const navigate = useNavigate()

    const selectedKey = location.pathname.startsWith('/admin/users')
        ? '/admin/users'
        : location.pathname === '/profile'
            ? '/profile'
            : '/'

    const items: MenuProps['items'] = [
        {
            key: '/',
            icon: <DashboardOutlined />,
            label: <Link to="/">Dashboard</Link>,
        },
        {
            key: '/profile',
            icon: <UserOutlined />,
            label: <Link to="/profile">Profile</Link>,
        },
        ...(user?.role === 'ADMIN'
            ? [
                {
                    key: '/admin/users',
                    icon: <TeamOutlined />,
                    label: <Link to="/admin/users">Users</Link>,
                },
            ]
            : []),
    ]

    const handleLogout = async () => {
        await logout()
        navigate('/login', { replace: true })
    }

    return (
        <Layout style={{ minHeight: '100vh' }}>
            <Sider
                width={240}
                theme="light"
                style={{
                    borderRight: '1px solid #f0f0f0',
                    display: 'flex',
                    flexDirection: 'column',
                }}
            >
                <div style={{ padding: 20 }}>
                    <Title level={3} style={{ margin: 0 }}>
                        StudySpace
                    </Title>
                    <Text type="secondary">Phase 1 Template</Text>
                </div>

                <Menu mode="inline" selectedKeys={[selectedKey]} items={items} style={{ borderRight: 0 }} />

                <div style={{ marginTop: 'auto', padding: 16 }}>
                    <div
                        style={{
                            padding: 12,
                            borderRadius: 12,
                            background: '#fafafa',
                            border: '1px solid #f0f0f0',
                            marginBottom: 12,
                        }}
                    >
                        <Space direction="vertical" size={0}>
                            <Text strong>{user?.fullName}</Text>
                            <Text type="secondary">{user?.role}</Text>
                        </Space>
                    </div>

                    <Button block icon={<LogoutOutlined />} onClick={() => void handleLogout()}>
                        Logout
                    </Button>
                </div>
            </Sider>

            <Layout>
                <Content style={{ padding: 24, background: '#f5f7fb' }}>
                    <Outlet />
                </Content>
            </Layout>
        </Layout>
    )
}