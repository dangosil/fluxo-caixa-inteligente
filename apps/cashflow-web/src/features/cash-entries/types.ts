import type { PaymentMethod } from '../../types/api'

export type FeePayer = 'MERCHANT' | 'CUSTOMER'

export type CardBrand = 'VISA' | 'MASTERCARD' | 'ELO' | 'AMEX' | 'HIPERCARD' | 'OTHER'

export type CashEntry = {
  id: string
  description: string
  amount: number
  feeAmount: number
  feePayer: FeePayer
  cardBrand?: CardBrand | null
  installmentCount: number
  installmentAmount?: number | null
  customerPaidAmount: number
  receivedAmount: number
  entryDate: string
  expectedReceiptDate: string
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
  expectedReceiptDate?: string
  categoryId: string
  paymentMethod: PaymentMethod
  notes?: string
  feeAmount?: number
  feePayer?: FeePayer
  cardBrand?: CardBrand
  installmentCount?: number
  installmentAmount?: number
}
