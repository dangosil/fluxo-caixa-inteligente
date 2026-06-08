import { api } from '../../lib/api'

import type { CashEntry, CashEntryFilters, CreateCashEntryRequest } from './types'

export async function getCashEntries(filters: CashEntryFilters) {
  const response = await api.get<CashEntry[]>('/cash-entries', {
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

export async function createCashEntry(request: CreateCashEntryRequest) {
  const response = await api.post<CashEntry>('/cash-entries', request)

  return response.data
}
