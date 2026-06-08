import { ArrowDownCircle, ArrowUpCircle } from 'lucide-react'

import { cn } from '../../lib/cn'
import type { CategoryType } from '../../types/api'

import { categoryTypeLabel } from './categoryLabels'

type CategoryTypeBadgeProps = {
  type: CategoryType
}

export function CategoryTypeBadge({ type }: CategoryTypeBadgeProps) {
  const Icon = type === 'INCOME' ? ArrowUpCircle : ArrowDownCircle

  return (
    <span
      className={cn(
        'inline-flex items-center gap-1 rounded-md border px-2 py-1 text-xs font-medium',
        type === 'INCOME'
          ? 'border-[#c7dec4] bg-[#f4faf2] text-[#255c32]'
          : 'border-[#f0d1c8] bg-[#fff7f4] text-[#8a3d2f]',
      )}
    >
      <Icon size={14} aria-hidden="true" />
      {categoryTypeLabel(type)}
    </span>
  )
}
