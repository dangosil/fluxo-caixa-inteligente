import { AlertTriangle, ArrowDownCircle, ArrowUpCircle, CircleDollarSign } from 'lucide-react'

import { cn } from '../../lib/cn'
import { formatCurrency } from '../../lib/formatters'

type SummaryCardTone = 'income' | 'expense' | 'profit' | 'warning'

type SummaryCardProps = {
  label: string
  value: number
  tone: SummaryCardTone
}

const toneStyles: Record<SummaryCardTone, string> = {
  income: 'border-[#c7dec4] bg-[#f4faf2] text-[#255c32]',
  expense: 'border-[#f0d1c8] bg-[#fff7f4] text-[#8a3d2f]',
  profit: 'border-[#bfd9d3] bg-[#f1f8f6] text-[#1f6358]',
  warning: 'border-[#efd59a] bg-[#fff9e8] text-[#8a5b16]',
}

const icons = {
  income: ArrowUpCircle,
  expense: ArrowDownCircle,
  profit: CircleDollarSign,
  warning: AlertTriangle,
}

export function SummaryCard({ label, value, tone }: SummaryCardProps) {
  const Icon = icons[tone]

  return (
    <article className={cn('rounded-md border p-5 shadow-sm', toneStyles[tone])}>
      <div className="flex items-center justify-between gap-3">
        <p className="text-sm font-medium">{label}</p>
        <Icon size={20} aria-hidden="true" />
      </div>
      <strong className="mt-4 block text-2xl font-semibold text-current">
        {formatCurrency(value)}
      </strong>
    </article>
  )
}
