import { zodResolver } from '@hookform/resolvers/zod'
import { useMutation } from '@tanstack/react-query'
import { Alert, Button, Card, Form, Input, Typography } from 'antd'
import { Controller, useForm } from 'react-hook-form'
import { loginSchema, type LoginFormValues } from '../../features/auth/schemas/loginSchema.ts'
import { login } from '../../features/auth/services/authService.ts'

export default function LoginPage() {
    const {
        control,
        handleSubmit,
        formState: { errors }
    } = useForm<LoginFormValues>({
        resolver: zodResolver(loginSchema),
        defaultValues: {
            email: '',
            password: ''
        }
    })

    const loginMutation = useMutation({
        mutationFn: login
    })

    const onSubmit = (values: LoginFormValues) => {
        loginMutation.mutate(values)
    }

    return (
        <Card style={{ maxWidth: 420, margin: '0 auto' }}>
            <Typography.Title level={3}>Đăng nhập</Typography.Title>

            {loginMutation.isError && (
                <Alert
                    type="error"
                    showIcon
                    style={{ marginBottom: 16 }}
                    message="Đăng nhập thất bại"
                    description="Backend chưa có API login hoặc thông tin đăng nhập chưa đúng."
                />
            )}

            <Form layout="vertical" onFinish={handleSubmit(onSubmit)}>
                <Form.Item
                    label="Email"
                    validateStatus={errors.email ? 'error' : ''}
                    help={errors.email?.message}
                >
                    <Controller
                        name="email"
                        control={control}
                        render={({ field }) => (
                            <Input {...field} placeholder="Nhập email" />
                        )}
                    />
                </Form.Item>

                <Form.Item
                    label="Password"
                    validateStatus={errors.password ? 'error' : ''}
                    help={errors.password?.message}
                >
                    <Controller
                        name="password"
                        control={control}
                        render={({ field }) => (
                            <Input.Password {...field} placeholder="Nhập mật khẩu" />
                        )}
                    />
                </Form.Item>

                <Button
                    type="primary"
                    htmlType="submit"
                    block
                    loading={loginMutation.isPending}
                >
                    Login
                </Button>
            </Form>
        </Card>
    )
}