import { Card, Col, Row, Statistic, Tag, Typography } from 'antd'
import { useAuth } from '../context/AuthContext'

const { Title, Paragraph, Text } = Typography

export default function DashboardPage() {
    const { user } = useAuth()

    return (
        <div>
            <Title level={2} style={{ marginTop: 0 }}>
                Dashboard
            </Title>

            <Paragraph type="secondary">
                Đây là màn hình nền của Phase 1. Mục tiêu là xác thực, phân quyền route, app shell và user
                management cơ bản.
            </Paragraph>

            <Row gutter={[16, 16]}>
                <Col xs={24} md={12} xl={6}>
                    <Card>
                        <Statistic title="Current User ID" value={user?.id ?? '-'} />
                    </Card>
                </Col>

                <Col xs={24} md={12} xl={6}>
                    <Card>
                        <Statistic title="Role" value={user?.role ?? '-'} />
                    </Card>
                </Col>

                <Col xs={24} md={12} xl={6}>
                    <Card>
                        <Statistic title="Status" value={user?.active ? 'Active' : 'Inactive'} />
                    </Card>
                </Col>

                <Col xs={24} md={12} xl={6}>
                    <Card>
                        <Statistic title="Email" value={user?.email ?? '-'} />
                    </Card>
                </Col>
            </Row>

            <Card style={{ marginTop: 16 }}>
                <Title level={4}>Current Session</Title>
                <Paragraph>
                    <Text strong>Full name:</Text> {user?.fullName}
                </Paragraph>
                <Paragraph>
                    <Text strong>Email:</Text> {user?.email}
                </Paragraph>
                <Paragraph>
                    <Text strong>Role:</Text>{' '}
                    <Tag color={user?.role === 'ADMIN' ? 'red' : user?.role === 'STAFF' ? 'blue' : 'default'}>
                        {user?.role}
                    </Tag>
                </Paragraph>
            </Card>
        </div>
    )
}