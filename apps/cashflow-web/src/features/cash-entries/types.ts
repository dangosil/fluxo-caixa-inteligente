import type { PaymentMethod } from '../../types/api'

export type CashEntry = {
  id: string
  description: string
  amount: number
  entryDate: string
  categoryId: string
  categoryName: string
  paymentMethod: PaymentMethod
  notes?: string | null
  active: boolean
  createdAt?: string
  updatedAt?: string
}

export type CashEntryFilters = {
  startDate?: string
  endDate?: string
  categoryId?: string
  paymentMethod?: PaymentMethod
  active?: boolean
}

export type CreateCashEntryRequest = {
  description: string
  amount: number
  entryDate: string
  categoryId: string
  paymentMethod: PaymentMethod
  notes?: string
}
