import { formatCurrency, formatDate } from '../../lib/formatters'

import { CashExpenseStatusBadge } from './CashExpenseStatusBadge'
import { PaymentMethodBadge } from './PaymentMethodBadge'
import type { CashExpense } from './types'

type CashExpenseListProps = {
  expenses: CashExpense[]
}

export function CashExpenseList({ expenses }: CashExpenseListProps) {
  return (
    <div className="overflow-hidden rounded-md border border-[#dfe4dc] bg-white shadow-sm">
      <div className="hidden grid-cols-[1fr_130px_120px_150px_110px] border-b border-[#dfe4dc] bg-[#f8faf7] px-4 py-3 text-xs font-semibold uppercase tracking-wide text-[#5f6f65] xl:grid">
        <span>Descrição</span>
        <span>Valor</span>
        <span>Data</span>
        <span>Forma</span>
        <span>Status</span>
      </div>

      <ul className="divide-y divide-[#edf1eb]">
        {expenses.map((expense) => (
          <li
            key={expense.id}
            className="grid gap-3 px-4 py-4 xl:grid-cols-[1fr_130px_120px_150px_110px] xl:items-center"
          >
            <div>
              <p className="font-medium text-[#17211b]">{expense.description}</p>
              <p className="mt-1 text-xs text-[#5f6f65]">{expense.categoryName}</p>
              {expense.notes && <p className="mt-1 text-xs text-[#5f6f65]">{expense.notes}</p>}
            </div>
            <strong className="text-[#8a3d2f]">{formatCurrency(expense.amount)}</strong>
            <span className="text-sm text-[#5f6f65]">{formatDate(expense.expenseDate)}</span>
            <div>
              <PaymentMethodBadge paymentMethod={expense.paymentMethod} />
            </div>
            <div>
              <CashExpenseStatusBadge active={expense.active} />
            </div>
          </li>
        ))}
      </ul>
    </div>
  )
}
