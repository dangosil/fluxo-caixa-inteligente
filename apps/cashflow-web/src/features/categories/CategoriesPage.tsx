import { useState } from 'react'

import { PageHeader } from '../../components/shared/PageHeader'

import { CategoriesEmpty } from './CategoriesEmpty'
import { CategoriesError } from './CategoriesError'
import { CategoryFilterTabs } from './CategoryFilterTabs'
import { CategoryForm } from './CategoryForm'
import { CategoryList } from './CategoryList'
import { CategoriesLoading } from './CategoriesLoading'
import type { CategoryFilter, CreateCategoryRequest } from './types'
import { useCategories, useCreateCategory } from './useCategories'

export function CategoriesPage() {
  const [filter, setFilter] = useState<CategoryFilter>('ALL')
  const [successMessage, setSuccessMessage] = useState('')
  const categoriesQuery = useCategories(filter)
  const createCategory = useCreateCategory()

  function handleCreateCategory(values: CreateCategoryRequest) {
    setSuccessMessage('')
    createCategory.mutate(values, {
      onSuccess: (category) => {
        setSuccessMessage(`Categoria "${category.name}" criada com sucesso.`)
      },
    })
  }

  return (
    <>
      <PageHeader
        title="Categorias"
        description="Organize os lançamentos por categorias simples de entrada e saída."
      />

      <div className="grid gap-6 lg:grid-cols-[360px_1fr]">
        <div className="space-y-3">
          <CategoryForm isSaving={createCategory.isPending} onSubmit={handleCreateCategory} />

          {successMessage && (
            <div className="rounded-md border border-[#c7dec4] bg-[#f4faf2] px-4 py-3 text-sm font-medium text-[#255c32]">
              {successMessage}
            </div>
          )}

          {createCategory.isError && (
            <div className="rounded-md border border-[#f0d1c8] bg-[#fff7f4] px-4 py-3 text-sm font-medium text-[#8a3d2f]">
              Não foi possível criar a categoria. Confira os dados e tente novamente.
            </div>
          )}
        </div>

        <section className="space-y-4">
          <div className="flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
            <div>
              <h3 className="text-base font-semibold text-[#17211b]">Categorias cadastradas</h3>
              <p className="mt-1 text-sm text-[#5f6f65]">Use o filtro para alternar entre entradas e saídas.</p>
            </div>
            <CategoryFilterTabs value={filter} onChange={setFilter} />
          </div>

          {categoriesQuery.isLoading && <CategoriesLoading />}

          {categoriesQuery.isError && <CategoriesError onRetry={() => void categoriesQuery.refetch()} />}

          {categoriesQuery.data?.length === 0 && <CategoriesEmpty />}

          {categoriesQuery.data && categoriesQuery.data.length > 0 && (
            <CategoryList categories={categoriesQuery.data} />
          )}
        </section>
      </div>
    </>
  )
}
