import { useState } from 'react'

import { PageHeader } from '../../components/shared/PageHeader'

import { CashExpensesEmpty } from './CashExpensesEmpty'
import { CashExpensesError } from './CashExpensesError'
import { CashExpensesFilters } from './CashExpensesFilters'
import { CashExpensesLoading } from './CashExpensesLoading'
import { CashExpenseForm } from './CashExpenseForm'
import { CashExpenseList } from './CashExpenseList'
import type { CashExpenseFilters, CreateCashExpenseRequest } from './types'
import { useCashExpenses, useCreateCashExpense } from './useCashExpenses'
import { useExpenseCategories } from './useExpenseCategories'

export function CashExpensesPage() {
  const [filters, setFilters] = useState<CashExpenseFilters>({ active: true })
  const [successMessage, setSuccessMessage] = useState('')
  const expenseCategories = useExpenseCategories()
  const cashExpenses = useCashExpenses(filters)
  const createCashExpense = useCreateCashExpense()

  function handleCreateCashExpense(values: CreateCashExpenseRequest) {
    setSuccessMessage('')
    createCashExpense.mutate(values, {
      onSuccess: (expense) => {
        setSuccessMessage(`Saída "${expense.description}" criada com sucesso.`)
      },
    })
  }

  const categories = expenseCategories.data ?? []

  return (
    <>
      <PageHeader
        title="Saídas"
        description="Registre pagamentos e consulte saídas por período, categoria, forma de pagamento e status."
      />

      <div className="grid gap-6 xl:grid-cols-[380px_1fr]">
        <div className="space-y-3">
          <CashExpenseForm
            categories={categories}
            isSaving={createCashExpense.isPending}
            onSubmit={handleCreateCashExpense}
          />

          {expenseCategories.isError && (
            <div className="rounded-md border border-[#f0d1c8] bg-[#fff7f4] px-4 py-3 text-sm font-medium text-[#8a3d2f]">
              Não foi possível carregar categorias de saída.
            </div>
          )}

          {categories.length === 0 && !expenseCategories.isLoading && !expenseCategories.isError && (
            <div className="rounded-md border border-[#efd59a] bg-[#fff9e8] px-4 py-3 text-sm font-medium text-[#8a5b16]">
              Crie uma categoria de saída antes de cadastrar lançamentos.
            </div>
          )}

          {successMessage && (
            <div className="rounded-md border border-[#c7dec4] bg-[#f4faf2] px-4 py-3 text-sm font-medium text-[#255c32]">
              {successMessage}
            </div>
          )}

          {createCashExpense.isError && (
            <div className="rounded-md border border-[#f0d1c8] bg-[#fff7f4] px-4 py-3 text-sm font-medium text-[#8a3d2f]">
              Não foi possível criar a saída. Confira os dados e tente novamente.
            </div>
          )}
        </div>

        <section className="space-y-4">
          <div>
            <h3 className="text-base font-semibold text-[#17211b]">Saídas cadastradas</h3>
            <p className="mt-1 text-sm text-[#5f6f65]">Use os filtros para refinar os lançamentos exibidos.</p>
          </div>

          <CashExpensesFilters categories={categories} filters={filters} onChange={setFilters} />

          {cashExpenses.isLoading && <CashExpensesLoading />}

          {cashExpenses.isError && <CashExpensesError onRetry={() => void cashExpenses.refetch()} />}

          {cashExpenses.data?.length === 0 && <CashExpensesEmpty />}

          {cashExpenses.data && cashExpenses.data.length > 0 && <CashExpenseList expenses={cashExpenses.data} />}
        </section>
      </div>
    </>
  )
}
