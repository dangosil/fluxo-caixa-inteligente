import type { CardBrand, FeePayer } from './types'

export const feePayerLabels: Record<FeePayer, string> = {
  MERCHANT: 'Empresa absorve a taxa',
  CUSTOMER: 'Taxa repassada ao cliente',
}

export const feePayerOptions: Array<{ value: FeePayer; label: string }> = [
  { value: 'MERCHANT', label: feePayerLabels.MERCHANT },
  { value: 'CUSTOMER', label: feePayerLabels.CUSTOMER },
]

export const cardBrandLabels: Record<CardBrand, string> = {
  VISA: 'Visa',
  MASTERCARD: 'Mastercard',
  ELO: 'Elo',
  AMEX: 'Amex',
  HIPERCARD: 'Hipercard',
  OTHER: 'Outra',
}

export const cardBrandOptions: Array<{ value: CardBrand; label: string }> = [
  { value: 'VISA', label: cardBrandLabels.VISA },
  { value: 'MASTERCARD', label: cardBrandLabels.MASTERCARD },
  { value: 'ELO', label: cardBrandLabels.ELO },
  { value: 'AMEX', label: cardBrandLabels.AMEX },
  { value: 'HIPERCARD', label: cardBrandLabels.HIPERCARD },
  { value: 'OTHER', label: cardBrandLabels.OTHER },
]
