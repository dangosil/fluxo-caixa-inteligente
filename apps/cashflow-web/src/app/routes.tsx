import { createBrowserRouter, Navigate } from 'react-router-dom'

import { AppLayout } from '../components/layout/AppLayout'
import { CashEntriesPage } from '../features/cash-entries/CashEntriesPage'
import { CashExpensesPage } from '../features/cash-expenses/CashExpensesPage'
import { CategoriesPage } from '../features/categories/CategoriesPage'
import { DashboardPage } from '../features/dashboard/DashboardPage'

export const router = createBrowserRouter([
  {
    path: '/',
    element: <AppLayout />,
    children: [
      { index: true, element: <Navigate to="/dashboard" replace /> },
      { path: 'dashboard', element: <DashboardPage /> },
      { path: 'categories', element: <CategoriesPage /> },
      { path: 'cash-entries', element: <CashEntriesPage /> },
      { path: 'cash-expenses', element: <CashExpensesPage /> },
    ],
  },
])
