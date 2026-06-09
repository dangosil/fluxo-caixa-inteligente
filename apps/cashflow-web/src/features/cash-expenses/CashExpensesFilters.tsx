import type { Category } from '../categories/types'
import { paymentMethodOptions } from '../../lib/paymentMethodLabels'

import type { CashExpenseFilters } from './types'

type CashExpensesFiltersProps = {
  categories: Category[]
  filters: CashExpenseFilters
  onChange: (filters: CashExpenseFilters) => void
}

export function CashExpensesFilters({ categories, filters, onChange }: CashExpensesFiltersProps) {
  function updateFilter<Key extends keyof CashExpenseFilters>(key: Key, value: CashExpenseFilters[Key] | '') {
    onChange({
      ...filters,
      [key]: value === '' ? undefined : value,
    })
  }

  return (
    <div className="rounded-md border border-[#dfe4dc] bg-white p-4 shadow-sm">
      <div className="grid gap-3 md:grid-cols-2 xl:grid-cols-5">
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
            onChange={(event) => updateFilter('paymentMethod', event.target.value as CashExpenseFilters['paymentMethod'] | '')}
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

        <label className="block">
          <span className="text-sm font-medium text-[#334238]">Status</span>
          <select
            value={filters.active === undefined ? '' : String(filters.active)}
            onChange={(event) => {
              const value = event.target.value
              updateFilter('active', value === '' ? '' : value === 'true')
            }}
            className="mt-2 h-10 w-full rounded-md border border-[#cfd8cc] bg-white px-3 text-sm outline-none transition focus:border-[#345c3d] focus:ring-2 focus:ring-[#345c3d]/20"
          >
            <option value="">Todas</option>
            <option value="true">Ativas</option>
            <option value="false">Inativas</option>
          </select>
        </label>
      </div>
    </div>
  )
}
