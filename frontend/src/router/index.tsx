import { createBrowserRouter } from 'react-router-dom'
import ProtectedRoute from '../components/ProtectedRoute'
import HomePage from '../pages/HomePage'
import LoginPage from '../pages/auth/LoginPage'
import MePage from '../pages/MePage'
import TestAuthPage from '../pages/TestAuthPage'
import TestStaffPage from '../pages/TestStaffPage'
import TestAdminPage from '../pages/TestAdminPage'

export const router = createBrowserRouter([
    {
        path: '/',
        element: <HomePage />
    },
    {
        path: '/login',
        element: <LoginPage />
    },
    {
        element: <ProtectedRoute />,
        children: [
            {
                path: '/me',
                element: <MePage />
            },
            {
                path: '/test/authenticated',
                element: <TestAuthPage />
            }
        ]
    },
    {
        element: <ProtectedRoute allowedRoles={['STAFF', 'ADMIN']} />,
        children: [
            {
                path: '/test/staff',
                element: <TestStaffPage />
            }
        ]
    },
    {
        element: <ProtectedRoute allowedRoles={['ADMIN']} />,
        children: [
            {
                path: '/test/admin',
                element: <TestAdminPage />
            }
        ]
    }
])