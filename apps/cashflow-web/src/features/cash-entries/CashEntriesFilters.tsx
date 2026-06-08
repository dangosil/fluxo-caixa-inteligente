import type { Category } from '../categories/types'

import { paymentMethodOptions } from './paymentMethodLabels'
import type { CashEntryFilters } from './types'

type CashEntriesFiltersProps = {
  categories: Category[]
  filters: CashEntryFilters
  onChange: (filters: CashEntryFilters) => void
}

export function CashEntriesFilters({ categories, filters, onChange }: CashEntriesFiltersProps) {
  function updateFilter<Key extends keyof CashEntryFilters>(key: Key, value: CashEntryFilters[Key] | '') {
    onChange({
      ...filters,
      [key]: value === '' ? undefined : value,
    })
  }

  return (
    <div className="rounded-md border border-[#dfe4dc] bg-white p-4 shadow-sm">
      <div className="grid gap-3 md:grid-cols-2 xl:grid-cols-4">
        <label className="block">
          <span className="text-sm font-medium text-[#334238]">Data inicial</span>
          <input
            type="date"
            value={filters.startDate ?? ''}
            onChange={(event) => updateFilter('startDate', event.target.value)}
            className="mt-2 h-10 w-full rounded-md border border-[#cfd8cc] bg-white px-3 text-sm outline-none transition focus:border-[#345c3d] focus:ring-2 focus:ring-[#345c3d]/20"
          />
        </label>

        <label className="block">
          <span className="text-sm font-medium text-[#334238]">Data final</span>
          <input
            type="date"
            value={filters.endDate ?? ''}
            onChange={(event) => updateFilter('endDate', event.target.value)}
            className="mt-2 h-10 w-full rounded-md border border-[#cfd8cc] bg-white px-3 text-sm outline-none transition focus:border-[#345c3d] focus:ring-2 focus:ring-[#345c3d]/20"
          />
        </label>

        <label className="block">
          <span className="text-sm font-medium text-[#334238]">Categoria</span>
          <select
            value={filters.categoryId ?? ''}
            onChange={(event) => updateFilter('categoryId', event.target.value)}
            className="mt-2 h-10 w-full rounded-md border border-[#cfd8cc] bg-white px-3 text-sm outline-none transition focus:border-[#345c3d] focus:ring-2 focus:ring-[#345c3d]/20"
          >
            <option value="">Todas</option>
            {categories.map((category) => (
              <option key={category.id} value={category.id}>
                {category.name}
              </option>
            ))}
          </select>
        </label>

        <label className="block">
          <span className="text-sm font-medium text-[#334238]">Forma</span>
          <select
            value={filters.paymentMethod ?? ''}
            onChange={(event) => updateFilter('paymentMethod', event.target.value as CashEntryFilters['paymentMethod'] | '')}
            className="mt-2 h-10 w-full rounded-md border border-[#cfd8cc] bg-white px-3 text-sm outline-none transition focus:border-[#345c3d] focus:ring-2 focus:ring-[#345c3d]/20"
          >
            <option value="">Todas</option>
            {paymentMethodOptions.map((option) => (
              <option key={option.value} value={option.value}>
                {option.label}
              </option>
            ))}
          </select>
        </label>
      </div>
    </div>
  )
}
