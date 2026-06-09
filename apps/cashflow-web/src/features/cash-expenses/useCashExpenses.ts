import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'

import { createCashExpense, getCashExpenses } from './cashExpenseApi'
import type { CashExpenseFilters, CreateCashExpenseRequest } from './types'

export function useCashExpenses(filters: CashExpenseFilters) {
  return useQuery({
    queryKey: ['cash-expenses', filters],
    queryFn: () => getCashExpenses(filters),
  })
}

export function useCreateCashExpense() {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: (request: CreateCashExpenseRequest) => createCashExpense(request),
    onSuccess: async () => {
      await queryClient.invalidateQueries({ queryKey: ['cash-expenses'] })
      await queryClient.invalidateQueries({ queryKey: ['dashboard-summary'] })
    },
  })
}
