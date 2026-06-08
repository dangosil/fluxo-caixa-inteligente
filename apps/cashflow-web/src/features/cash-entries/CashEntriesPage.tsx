import { useState } from 'react'

import { PageHeader } from '../../components/shared/PageHeader'

import { CashEntriesEmpty } from './CashEntriesEmpty'
import { CashEntriesError } from './CashEntriesError'
import { CashEntriesFilters } from './CashEntriesFilters'
import { CashEntriesLoading } from './CashEntriesLoading'
import { CashEntryForm } from './CashEntryForm'
import { CashEntryList } from './CashEntryList'
import type { CashEntryFilters, CreateCashEntryRequest } from './types'
import { useCashEntries, useCreateCashEntry } from './useCashEntries'
import { useIncomeCategories } from './useIncomeCategories'

export function CashEntriesPage() {
  const [filters, setFilters] = useState<CashEntryFilters>({ active: true })
  const [successMessage, setSuccessMessage] = useState('')
  const incomeCategories = useIncomeCategories()
  const cashEntries = useCashEntries(filters)
  const createCashEntry = useCreateCashEntry()

  function handleCreateCashEntry(values: CreateCashEntryRequest) {
    setSuccessMessage('')
    createCashEntry.mutate(values, {
      onSuccess: (entry) => {
        setSuccessMessage(`Entrada "${entry.description}" criada com sucesso.`)
      },
    })
  }

  const categories = incomeCategories.data ?? []

  return (
    <>
      <PageHeader
        title="Entradas"
        description="Registre recebimentos e consulte entradas por período, categoria e forma de pagamento."
      />

      <div className="grid gap-6 xl:grid-cols-[380px_1fr]">
        <div className="space-y-3">
          <CashEntryForm
            categories={categories}
            isSaving={createCashEntry.isPending}
            onSubmit={handleCreateCashEntry}
          />

          {incomeCategories.isError && (
            <div className="rounded-md border border-[#f0d1c8] bg-[#fff7f4] px-4 py-3 text-sm font-medium text-[#8a3d2f]">
              Não foi possível carregar categorias de entrada.
            </div>
          )}

          {categories.length === 0 && !incomeCategories.isLoading && !incomeCategories.isError && (
            <div className="rounded-md border border-[#efd59a] bg-[#fff9e8] px-4 py-3 text-sm font-medium text-[#8a5b16]">
              Crie uma categoria de entrada antes de cadastrar lançamentos.
            </div>
          )}

          {successMessage && (
            <div className="rounded-md border border-[#c7dec4] bg-[#f4faf2] px-4 py-3 text-sm font-medium text-[#255c32]">
              {successMessage}
            </div>
          )}

          {createCashEntry.isError && (
            <div className="rounded-md border border-[#f0d1c8] bg-[#fff7f4] px-4 py-3 text-sm font-medium text-[#8a3d2f]">
              Não foi possível criar a entrada. Confira os dados e tente novamente.
            </div>
          )}
        </div>

        <section className="space-y-4">
          <div>
            <h3 className="text-base font-semibold text-[#17211b]">Entradas cadastradas</h3>
            <p className="mt-1 text-sm text-[#5f6f65]">Use os filtros para refinar os lançamentos exibidos.</p>
          </div>

          <CashEntriesFilters categories={categories} filters={filters} onChange={setFilters} />

          {cashEntries.isLoading && <CashEntriesLoading />}

          {cashEntries.isError && <CashEntriesError onRetry={() => void cashEntries.refetch()} />}

          {cashEntries.data?.length === 0 && <CashEntriesEmpty />}

          {cashEntries.data && cashEntries.data.length > 0 && <CashEntryList entries={cashEntries.data} />}
        </section>
      </div>
    </>
  )
}
