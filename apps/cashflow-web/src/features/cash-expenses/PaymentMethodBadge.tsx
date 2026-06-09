import { CreditCard } from 'lucide-react'

import { paymentMethodLabels } from '../cash-entries/paymentMethodLabels'
import type { CashExpense } from './types'

type PaymentMethodBadgeProps = {
  paymentMethod: CashExpense['paymentMethod']
}

export function PaymentMethodBadge({ paymentMethod }: PaymentMethodBadgeProps) {
  return (
    <span className="inline-flex items-center gap-1 rounded-md border border-[#dfe4dc] bg-white px-2 py-1 text-xs font-medium text-[#5f6f65]">
      <CreditCard size={14} aria-hidden="true" />
      {paymentMethodLabels[paymentMethod]}
    </span>
  )
}
