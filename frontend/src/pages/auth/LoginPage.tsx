import { Button, Card, Form, Input, Typography } from 'antd'

export default function LoginPage() {
    return (
        <Card style={{ maxWidth: 420, margin: '0 auto' }}>
            <Typography.Title level={3}>Đăng nhập</Typography.Title>

            <Form layout="vertical">
                <Form.Item label="Email" name="email">
                    <Input placeholder="Nhập email" />
                </Form.Item>

                <Form.Item label="Password" name="password">
                    <Input.Password placeholder="Nhập mật khẩu" />
                </Form.Item>

                <Button type="primary" htmlType="submit" block>
                    Login
                </Button>
            </Form>
        </Card>
    )
}