import { ReloadOutlined } from '@ant-design/icons'
import { Button, Card, Popconfirm, Space, Switch, Table, Tag, Typography, message } from 'antd'
import type { ColumnsType } from 'antd/es/table'
import { useEffect, useMemo, useState } from 'react'
import { useAuth } from '../context/AuthContext'
import { userService } from '../features/users/services/userService'
import type { UserSummary } from '../features/users/types/user'

const { Title, Paragraph, Text } = Typography

export default function AdminUsersPage() {
    const { user: currentUser } = useAuth()
    const [users, setUsers] = useState<UserSummary[]>([])
    const [isLoading, setIsLoading] = useState(true)
    const [updatingUserId, setUpdatingUserId] = useState<number | null>(null)
    const [messageApi, contextHolder] = message.useMessage()

    const loadUsers = async () => {
        setIsLoading(true)

        try {
            const data = await userService.getUsers()
            setUsers(data)
        } catch (err) {
            const errorMessage = err instanceof Error ? err.message : 'Load users failed'
            await messageApi.error(errorMessage)
        } finally {
            setIsLoading(false)
        }
    }

    useEffect(() => {
        void loadUsers()
    }, [])

    const handleToggleStatus = async (targetUser: UserSummary) => {
        if (currentUser?.id === targetUser.id) {
            await messageApi.warning('You cannot deactivate your own current account here')
            return
        }

        setUpdatingUserId(targetUser.id)

        try {
            const updated = await userService.updateUserStatus(targetUser.id, !targetUser.active)

            setUsers((prev) =>
                prev.map((item) =>
                    item.id === updated.id ? { ...item, active: updated.active } : item,
                ),
            )

            await messageApi.success('Updated user status successfully')
        } catch (err) {
            const errorMessage = err instanceof Error ? err.message : 'Update status failed'
            await messageApi.error(errorMessage)
        } finally {
            setUpdatingUserId(null)
        }
    }

    const columns: ColumnsType<UserSummary> = useMemo(
        () => [
            {
                title: 'ID',
                dataIndex: 'id',
                key: 'id',
                width: 80,
            },
            {
                title: 'Full Name',
                dataIndex: 'fullName',
                key: 'fullName',
            },
            {
                title: 'Email',
                dataIndex: 'email',
                key: 'email',
            },
            {
                title: 'Phone',
                dataIndex: 'phone',
                key: 'phone',
            },
            {
                title: 'Role',
                dataIndex: 'role',
                key: 'role',
                render: (role: string) => (
                    <Tag color={role === 'ADMIN' ? 'red' : role === 'STAFF' ? 'blue' : 'default'}>
                        {role}
                    </Tag>
                ),
            },
            {
                title: 'Status',
                dataIndex: 'active',
                key: 'active',
                render: (active: boolean) =>
                    active ? <Tag color="green">Active</Tag> : <Tag>Inactive</Tag>,
            },
            {
                title: 'Action',
                key: 'action',
                width: 180,
                render: (_, record) => {
                    const isSelf = currentUser?.id === record.id

                    return (
                        <Space>
                            <Popconfirm
                                title={record.active ? 'Deactivate this user?' : 'Activate this user?'}
                                description={
                                    isSelf
                                        ? 'You cannot change your own current account status here.'
                                        : 'This action will update the user active flag.'
                                }
                                okText="Confirm"
                                cancelText="Cancel"
                                onConfirm={() => void handleToggleStatus(record)}
                                disabled={isSelf}
                            >
                                <Switch
                                    checked={record.active}
                                    loading={updatingUserId === record.id}
                                    disabled={isSelf}
                                />
                            </Popconfirm>

                            {isSelf ? <Text type="secondary">Current user</Text> : null}
                        </Space>
                    )
                },
            },
        ],
        [currentUser?.id, updatingUserId],
    )

    return (
        <>
            {contextHolder}

            <div>
                <div
                    style={{
                        display: 'flex',
                        justifyContent: 'space-between',
                        alignItems: 'center',
                        marginBottom: 16,
                    }}
                >
                    <div>
                        <Title level={2} style={{ margin: 0 }}>
                            Admin Users
                        </Title>
                        <Paragraph type="secondary" style={{ marginBottom: 0 }}>
                            Quản lý user cơ bản cho Phase 1 foundation.
                        </Paragraph>
                    </div>

                    <Button icon={<ReloadOutlined />} onClick={() => void loadUsers()} loading={isLoading}>
                        Reload
                    </Button>
                </div>

                <Card>
                    <Table<UserSummary>
                        rowKey="id"
                        columns={columns}
                        dataSource={users}
                        loading={isLoading}
                        pagination={{ pageSize: 8, showSizeChanger: false }}
                    />
                </Card>
            </div>
        </>
    )
}