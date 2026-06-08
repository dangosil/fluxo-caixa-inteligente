import type { PaymentMethod } from '../../types/api'

export const paymentMethodLabels: Record<PaymentMethod, string> = {
  PIX: 'Pix',
  CASH: 'Dinheiro',
  CREDIT_CARD: 'Cartão de crédito',
  DEBIT_CARD: 'Cartão de débito',
  BANK_TRANSFER: 'Transferência bancária',
  OTHER: 'Outro',
}

export const paymentMethodOptions: Array<{ value: PaymentMethod; label: string }> = [
  { value: 'PIX', label: paymentMethodLabels.PIX },
  { value: 'CASH', label: paymentMethodLabels.CASH },
  { value: 'CREDIT_CARD', label: paymentMethodLabels.CREDIT_CARD },
  { value: 'DEBIT_CARD', label: paymentMethodLabels.DEBIT_CARD },
  { value: 'BANK_TRANSFER', label: paymentMethodLabels.BANK_TRANSFER },
  { value: 'OTHER', label: paymentMethodLabels.OTHER },
]
