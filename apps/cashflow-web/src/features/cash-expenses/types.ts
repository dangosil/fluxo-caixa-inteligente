import type { PaymentMethod } from '../../types/api'

export type CashExpense = {
  id: string
  description: string
  amount: number
  expenseDate: string
  categoryId: string
  categoryName: string
  paymentMethod: PaymentMethod
  notes?: string | null
  active: boolean
  createdAt?: string
  updatedAt?: string
}

export type CashExpenseFilters = {
  startDate?: string
  endDate?: string
  categoryId?: string
  paymentMethod?: PaymentMethod
  active?: boolean
}

export type CreateCashExpenseRequest = {
  description: string
  amount: number
  expenseDate: string
  categoryId: string
  paymentMethod: PaymentMethod
  notes?: string
}
