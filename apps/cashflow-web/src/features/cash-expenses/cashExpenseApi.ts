import { api } from '../../lib/api'

import type { CashExpense, CashExpenseFilters, CreateCashExpenseRequest } from './types'

export async function getCashExpenses(filters: CashExpenseFilters) {
  const response = await api.get<CashExpense[]>('/cash-expenses', {
    params: {
      startDate: filters.startDate || undefined,
      endDate: filters.endDate || undefined,
      categoryId: filters.categoryId || undefined,
      paymentMethod: filters.paymentMethod || undefined,
      active: filters.active,
    },
  })

  return response.data
}

export async function createCashExpense(request: CreateCashExpenseRequest) {
  const response = await api.post<CashExpense>('/cash-expenses', request)

  return response.data
}
