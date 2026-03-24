import { MailOutlined, ReloadOutlined, SafetyCertificateOutlined, UserOutlined } from '@ant-design/icons'
import { Button, Card, Col, Descriptions, Row, Space, Tag, Typography, message } from 'antd'
import { useState } from 'react'
import { useAuth } from '../context/AuthContext'

const { Title, Paragraph } = Typography

export default function ProfilePage() {
    const { user, refreshMe } = useAuth()
    const [isRefreshing, setIsRefreshing] = useState(false)
    const [messageApi, contextHolder] = message.useMessage()

    const handleRefresh = async () => {
        setIsRefreshing(true)

        try {
            await refreshMe()
            await messageApi.success('Refreshed profile successfully')
        } catch (err) {
            const errorMessage = err instanceof Error ? err.message : 'Refresh failed'
            await messageApi.error(errorMessage)
        } finally {
            setIsRefreshing(false)
        }
    }

    return (
        <>
            {contextHolder}

            <div>
                <Row justify="space-between" align="middle" style={{ marginBottom: 16 }}>
                    <Col>
                        <Title level={2} style={{ margin: 0 }}>
                            Profile
                        </Title>
                        <Paragraph type="secondary" style={{ marginBottom: 0 }}>
                            Thông tin current user lấy từ backend `/api/users/me`.
                        </Paragraph>
                    </Col>

                    <Col>
                        <Button
                            icon={<ReloadOutlined />}
                            loading={isRefreshing}
                            onClick={() => void handleRefresh()}
                        >
                            Refresh
                        </Button>
                    </Col>
                </Row>

                <Row gutter={[16, 16]}>
                    <Col xs={24} xl={8}>
                        <Card>
                            <Space direction="vertical" size="middle" style={{ width: '100%' }}>
                                <div>
                                    <Title level={4} style={{ marginBottom: 4 }}>
                                        {user?.fullName}
                                    </Title>
                                    <Tag color={user?.role === 'ADMIN' ? 'red' : user?.role === 'STAFF' ? 'blue' : 'default'}>
                                        {user?.role}
                                    </Tag>
                                    <Tag color={user?.active ? 'green' : 'default'}>
                                        {user?.active ? 'Active' : 'Inactive'}
                                    </Tag>
                                </div>

                                <Space>
                                    <UserOutlined />
                                    <span>User ID: {user?.id}</span>
                                </Space>

                                <Space>
                                    <MailOutlined />
                                    <span>{user?.email}</span>
                                </Space>

                                <Space>
                                    <SafetyCertificateOutlined />
                                    <span>Authenticated session</span>
                                </Space>
                            </Space>
                        </Card>
                    </Col>

                    <Col xs={24} xl={16}>
                        <Card title="Account Details">
                            <Descriptions column={1} bordered size="middle">
                                <Descriptions.Item label="ID">{user?.id}</Descriptions.Item>
                                <Descriptions.Item label="Full Name">{user?.fullName}</Descriptions.Item>
                                <Descriptions.Item label="Email">{user?.email}</Descriptions.Item>
                                <Descriptions.Item label="Role">{user?.role}</Descriptions.Item>
                                <Descriptions.Item label="Active">{String(user?.active)}</Descriptions.Item>
                            </Descriptions>
                        </Card>
                    </Col>
                </Row>
            </div>
        </>
    )
}