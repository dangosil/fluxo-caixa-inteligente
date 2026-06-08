import { formatCurrency, formatDate } from '../../lib/formatters'

import { CashEntryStatusBadge } from './CashEntryStatusBadge'
import { PaymentMethodBadge } from './PaymentMethodBadge'
import type { CashEntry } from './types'

type CashEntryListProps = {
  entries: CashEntry[]
}

export function CashEntryList({ entries }: CashEntryListProps) {
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
        {entries.map((entry) => (
          <li
            key={entry.id}
            className="grid gap-3 px-4 py-4 xl:grid-cols-[1fr_130px_120px_150px_110px] xl:items-center"
          >
            <div>
              <p className="font-medium text-[#17211b]">{entry.description}</p>
              <p className="mt-1 text-xs text-[#5f6f65]">{entry.categoryName}</p>
              {entry.notes && <p className="mt-1 text-xs text-[#5f6f65]">{entry.notes}</p>}
            </div>
            <strong className="text-[#255c32]">{formatCurrency(entry.amount)}</strong>
            <span className="text-sm text-[#5f6f65]">{formatDate(entry.entryDate)}</span>
            <div>
              <PaymentMethodBadge paymentMethod={entry.paymentMethod} />
            </div>
            <div>
              <CashEntryStatusBadge active={entry.active} />
            </div>
          </li>
        ))}
      </ul>
    </div>
  )
}
