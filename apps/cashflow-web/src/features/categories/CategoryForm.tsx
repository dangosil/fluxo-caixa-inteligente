import { zodResolver } from '@hookform/resolvers/zod'
import { useForm } from 'react-hook-form'
import { z } from 'zod'

import type { CreateCategoryRequest } from './types'

const categorySchema = z.object({
  name: z.string().trim().min(2, 'Informe pelo menos 2 caracteres.'),
  type: z.enum(['INCOME', 'EXPENSE'], {
    message: 'Selecione o tipo da categoria.',
  }),
})

type CategoryFormValues = z.infer<typeof categorySchema>

type CategoryFormProps = {
  isSaving: boolean
  onSubmit: (values: CreateCategoryRequest) => void
}

export function CategoryForm({ isSaving, onSubmit }: CategoryFormProps) {
  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm<CategoryFormValues>({
    resolver: zodResolver(categorySchema),
    defaultValues: {
      name: '',
      type: 'INCOME',
    },
  })

  function submit(values: CategoryFormValues) {
    onSubmit({
      name: values.name.trim(),
      type: values.type,
    })
    reset()
  }

  return (
    <form
      onSubmit={handleSubmit(submit)}
      className="rounded-md border border-[#dfe4dc] bg-white p-5 shadow-sm"
    >
      <div>
        <h3 className="text-base font-semibold text-[#17211b]">Nova categoria</h3>
        <p className="mt-1 text-sm text-[#5f6f65]">
          Cadastre categorias simples para organizar entradas e saídas.
        </p>
      </div>

      <div className="mt-5 grid gap-4 md:grid-cols-[1fr_180px]">
        <label className="block">
          <span className="text-sm font-medium text-[#334238]">Nome</span>
          <input
            type="text"
            placeholder="Ex.: Vendas"
            className="mt-2 h-11 w-full rounded-md border border-[#cfd8cc] bg-white px-3 text-sm outline-none transition focus:border-[#345c3d] focus:ring-2 focus:ring-[#345c3d]/20"
            {...register('name')}
          />
          {errors.name && <span className="mt-1 block text-sm text-[#8a3d2f]">{errors.name.message}</span>}
        </label>

        <label className="block">
          <span className="text-sm font-medium text-[#334238]">Tipo</span>
          <select
            className="mt-2 h-11 w-full rounded-md border border-[#cfd8cc] bg-white px-3 text-sm outline-none transition focus:border-[#345c3d] focus:ring-2 focus:ring-[#345c3d]/20"
            {...register('type')}
          >
            <option value="INCOME">Entrada</option>
            <option value="EXPENSE">Saída</option>
          </select>
          {errors.type && <span className="mt-1 block text-sm text-[#8a3d2f]">{errors.type.message}</span>}
        </label>
      </div>

      <button
        type="submit"
        disabled={isSaving}
        className="mt-5 inline-flex h-10 items-center rounded-md border border-[#345c3d] bg-[#345c3d] px-4 text-sm font-medium text-white transition hover:bg-[#27472f] disabled:cursor-not-allowed disabled:opacity-70"
      >
        {isSaving ? 'Salvando...' : 'Criar categoria'}
      </button>
    </form>
  )
}
