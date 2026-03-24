import { createBrowserRouter } from 'react-router-dom'
import ProtectedRoute from '../../components/ProtectedRoute'
import RoleRoute from '../../components/RoleRoute'
import AppLayout from '../../layouts/AppLayout'
import LoginPage from '../../pages/auth/LoginPage'
import DashboardPage from '../../pages/DashboardPage'
import ProfilePage from '../../pages/ProfilePage'
import AdminUsersPage from '../../pages/AdminUsersPage'

export const router = createBrowserRouter([
    {
        path: '/login',
        element: <LoginPage />,
    },
    {
        element: <ProtectedRoute />,
        children: [
            {
                element: <AppLayout />,
                children: [
                    {
                        path: '/',
                        element: <DashboardPage />,
                    },
                    {
                        path: '/profile',
                        element: <ProfilePage />,
                    },
                ],
            },
        ],
    },
    {
        element: <ProtectedRoute />,
        children: [
            {
                element: <RoleRoute allowedRoles={['ADMIN']} />,
                children: [
                    {
                        element: <AppLayout />,
                        children: [
                            {
                                path: '/admin/users',
                                element: <AdminUsersPage />,
                            },
                        ],
                    },
                ],
            },
        ],
    },
])