import { cn } from '../../lib/cn'

import type { CategoryFilter } from './types'

type CategoryFilterTabsProps = {
  value: CategoryFilter
  onChange: (value: CategoryFilter) => void
}

const filters: Array<{ label: string; value: CategoryFilter }> = [
  { label: 'Todas', value: 'ALL' },
  { label: 'Entrada', value: 'INCOME' },
  { label: 'Saída', value: 'EXPENSE' },
]

export function CategoryFilterTabs({ value, onChange }: CategoryFilterTabsProps) {
  return (
    <div className="inline-flex rounded-md border border-[#dfe4dc] bg-white p-1 shadow-sm">
      {filters.map((filter) => (
        <button
          key={filter.value}
          type="button"
          onClick={() => onChange(filter.value)}
          className={cn(
            'h-9 rounded px-3 text-sm font-medium transition',
            value === filter.value
              ? 'bg-[#345c3d] text-white'
              : 'text-[#5f6f65] hover:bg-[#f1f4ef] hover:text-[#17211b]',
          )}
        >
          {filter.label}
        </button>
      ))}
    </div>
  )
}
