import { formatCurrency, formatDate } from '../../lib/formatters'
import { PaymentMethodBadge } from '../../components/shared/PaymentMethodBadge'
import { StatusBadge } from '../../components/shared/StatusBadge'

import { cardBrandLabels, feePayerLabels } from './cardFeeLabels'
import type { CashEntry } from './types'

type CashEntryListProps = {
  entries: CashEntry[]
}

export function CashEntryList({ entries }: CashEntryListProps) {
  return (
    <div className="overflow-hidden rounded-md border border-[#dfe4dc] bg-white shadow-sm">
      <div className="hidden grid-cols-[1fr_170px_170px_120px_150px_110px] border-b border-[#dfe4dc] bg-[#f8faf7] px-4 py-3 text-xs font-semibold uppercase tracking-wide text-[#5f6f65] xl:grid">
        <span>Descrição</span>
        <span>Venda</span>
        <span>Recebimento</span>
        <span>Data</span>
        <span>Forma</span>
        <span>Status</span>
      </div>

      <ul className="divide-y divide-[#edf1eb]">
        {entries.map((entry) => (
          <CashEntryListItem key={entry.id} entry={entry} />
        ))}
      </ul>
    </div>
  )
}

type CashEntryListItemProps = {
  entry: CashEntry
}

function CashEntryListItem({ entry }: CashEntryListItemProps) {
  const customerPaidAmount = entry.customerPaidAmount ?? entry.amount
  const installmentCount = Math.max(entry.installmentCount ?? 1, 1)
  const installmentAmount = entry.installmentAmount ?? customerPaidAmount / installmentCount
  const hasCardDetails = Boolean(entry.cardBrand) || installmentCount > 1

  return (
    <li className="grid gap-3 px-4 py-4 xl:grid-cols-[1fr_170px_170px_120px_150px_110px] xl:items-center">
      <div>
        <p className="font-medium text-[#17211b]">{entry.description}</p>
        <p className="mt-1 text-xs text-[#5f6f65]">{entry.categoryName}</p>
        {entry.notes && <p className="mt-1 text-xs text-[#5f6f65]">{entry.notes}</p>}
      </div>
      <div className="space-y-1 text-sm">
        <p>
          <span className="text-[#5f6f65]">Base: </span>
          <strong className="text-[#17211b]">{formatCurrency(entry.amount)}</strong>
        </p>
        <p className="text-xs text-[#5f6f65]">
          Taxa: {formatCurrency(entry.feeAmount ?? 0)}
          {entry.feeAmount > 0 && entry.feePayer ? ` · ${feePayerLabels[entry.feePayer]}` : ''}
        </p>
        {hasCardDetails && (
          <p className="text-xs text-[#5f6f65]">
            {entry.cardBrand ? cardBrandLabels[entry.cardBrand] : 'Cartão'}
            {installmentCount > 1
              ? ` · ${installmentCount}x de aproximadamente ${formatCurrency(installmentAmount)}`
              : ''}
          </p>
        )}
      </div>
      <div className="space-y-1 text-sm">
        <p>
          <span className="text-[#5f6f65]">Cliente paga: </span>
          <strong className="text-[#17211b]">{formatCurrency(customerPaidAmount)}</strong>
        </p>
        <p>
          <span className="text-[#5f6f65]">Empresa recebe: </span>
          <strong className="text-[#255c32]">{formatCurrency(entry.receivedAmount ?? entry.amount)}</strong>
        </p>
      </div>
      <span className="text-sm text-[#5f6f65]">{formatDate(entry.entryDate)}</span>
      <div>
        <PaymentMethodBadge paymentMethod={entry.paymentMethod} />
      </div>
      <div>
        <StatusBadge active={entry.active} />
      </div>
    </li>
  )
}
