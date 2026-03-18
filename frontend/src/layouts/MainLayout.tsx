import { Layout, Menu } from 'antd'
import { Link, Outlet, useLocation } from 'react-router-dom'

const { Header, Content } = Layout

export default function MainLayout() {
    const location = useLocation()

    const selectedKey = location.pathname === '/login' ? 'login' : 'home'

    return (
        <Layout style={{ minHeight: '100vh' }}>
            <Header>
                <Menu
                    theme="dark"
                    mode="horizontal"
                    selectedKeys={[selectedKey]}
                    items={[
                        { key: 'home', label: <Link to="/">Home</Link> },
                        { key: 'login', label: <Link to="/login">Login</Link> }
                    ]}
                />
            </Header>

            <Content style={{ padding: 24 }}>
                <Outlet />
            </Content>
        </Layout>
    )
}