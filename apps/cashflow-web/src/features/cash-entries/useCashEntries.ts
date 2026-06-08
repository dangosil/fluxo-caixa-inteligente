import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'

import { createCashEntry, getCashEntries } from './cashEntryApi'
import type { CashEntryFilters, CreateCashEntryRequest } from './types'

export function useCashEntries(filters: CashEntryFilters) {
  return useQuery({
    queryKey: ['cash-entries', filters],
    queryFn: () => getCashEntries(filters),
  })
}

export function useCreateCashEntry() {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: (request: CreateCashEntryRequest) => createCashEntry(request),
    onSuccess: async () => {
      await queryClient.invalidateQueries({ queryKey: ['cash-entries'] })
      await queryClient.invalidateQueries({ queryKey: ['dashboard-summary'] })
    },
  })
}
