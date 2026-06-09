import { zodResolver } from '@hookform/resolvers/zod'
import { useForm } from 'react-hook-form'
import { z } from 'zod'

import { paymentMethodOptions } from '../../lib/paymentMethodLabels'
import type { Category } from '../categories/types'

import type { CreateCashExpenseRequest } from './types'

const cashExpenseSchema = z.object({
  description: z.string().trim().min(2, 'Informe uma descrição.'),
  amount: z.coerce.number().positive('Informe um valor maior que zero.'),
  expenseDate: z.string().min(1, 'Informe a data.'),
  categoryId: z.string().min(1, 'Selecione uma categoria.'),
  paymentMethod: z.enum(['PIX', 'CASH', 'CREDIT_CARD', 'DEBIT_CARD', 'BANK_TRANSFER', 'OTHER'], {
    message: 'Selecione a forma de pagamento.',
  }),
  notes: z.string().optional(),
})

type CashExpenseFormInput = z.input<typeof cashExpenseSchema>
type CashExpenseFormValues = z.output<typeof cashExpenseSchema>

type CashExpenseFormProps = {
  categories: Category[]
  isSaving: boolean
  onSubmit: (values: CreateCashExpenseRequest) => void
}

export function CashExpenseForm({ categories, isSaving, onSubmit }: CashExpenseFormProps) {
  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm<CashExpenseFormInput, unknown, CashExpenseFormValues>({
    resolver: zodResolver(cashExpenseSchema),
    defaultValues: {
      description: '',
      amount: 0,
      expenseDate: new Date().toISOString().slice(0, 10),
      categoryId: '',
      paymentMethod: 'PIX',
      notes: '',
    },
  })

  function submit(values: CashExpenseFormValues) {
    onSubmit({
      description: values.description.trim(),
      amount: values.amount,
      expenseDate: values.expenseDate,
      categoryId: values.categoryId,
      paymentMethod: values.paymentMethod,
      notes: values.notes?.trim() || undefined,
    })
    reset({
      description: '',
      amount: 0,
      expenseDate: new Date().toISOString().slice(0, 10),
      categoryId: '',
      paymentMethod: 'PIX',
      notes: '',
    })
  }

  return (
    <form
      onSubmit={handleSubmit(submit)}
      className="rounded-md border border-[#dfe4dc] bg-white p-5 shadow-sm"
    >
      <div>
        <h3 className="text-base font-semibold text-[#17211b]">Nova saída</h3>
        <p className="mt-1 text-sm text-[#5f6f65]">Registre pagamentos e despesas do fluxo de caixa.</p>
      </div>

      <div className="mt-5 grid gap-4">
        <label className="block">
          <span className="text-sm font-medium text-[#334238]">Descrição</span>
          <input
            type="text"
            placeholder="Ex.: Pagamento de aluguel"
            className="mt-2 h-11 w-full rounded-md border border-[#cfd8cc] bg-white px-3 text-sm outline-none transition focus:border-[#345c3d] focus:ring-2 focus:ring-[#345c3d]/20"
            {...register('description')}
          />
          {errors.description && <span className="mt-1 block text-sm text-[#8a3d2f]">{errors.description.message}</span>}
        </label>

        <div className="grid gap-4 md:grid-cols-2">
          <label className="block">
            <span className="text-sm font-medium text-[#334238]">Valor</span>
            <input
              type="number"
              min="0"
              step="0.01"
              className="mt-2 h-11 w-full rounded-md border border-[#cfd8cc] bg-white px-3 text-sm outline-none transition focus:border-[#345c3d] focus:ring-2 focus:ring-[#345c3d]/20"
              {...register('amount')}
            />
            {errors.amount && <span className="mt-1 block text-sm text-[#8a3d2f]">{errors.amount.message}</span>}
          </label>

          <label className="block">
            <span className="text-sm font-medium text-[#334238]">Data</span>
            <input
              type="date"
              className="mt-2 h-11 w-full rounded-md border border-[#cfd8cc] bg-white px-3 text-sm outline-none transition focus:border-[#345c3d] focus:ring-2 focus:ring-[#345c3d]/20"
              {...register('expenseDate')}
            />
            {errors.expenseDate && <span className="mt-1 block text-sm text-[#8a3d2f]">{errors.expenseDate.message}</span>}
          </label>
        </div>

        <label className="block">
          <span className="text-sm font-medium text-[#334238]">Categoria</span>
          <select
            className="mt-2 h-11 w-full rounded-md border border-[#cfd8cc] bg-white px-3 text-sm outline-none transition focus:border-[#345c3d] focus:ring-2 focus:ring-[#345c3d]/20"
            {...register('categoryId')}
          >
            <option value="">Selecione</option>
            {categories.map((category) => (
              <option key={category.id} value={category.id}>
                {category.name}
              </option>
            ))}
          </select>
          {errors.categoryId && <span className="mt-1 block text-sm text-[#8a3d2f]">{errors.categoryId.message}</span>}
        </label>

        <label className="block">
          <span className="text-sm font-medium text-[#334238]">Forma de pagamento</span>
          <select
            className="mt-2 h-11 w-full rounded-md border border-[#cfd8cc] bg-white px-3 text-sm outline-none transition focus:border-[#345c3d] focus:ring-2 focus:ring-[#345c3d]/20"
            {...register('paymentMethod')}
          >
            {paymentMethodOptions.map((option) => (
              <option key={option.value} value={option.value}>
                {option.label}
              </option>
            ))}
          </select>
          {errors.paymentMethod && <span className="mt-1 block text-sm text-[#8a3d2f]">{errors.paymentMethod.message}</span>}
        </label>

        <label className="block">
          <span className="text-sm font-medium text-[#334238]">Observações</span>
          <textarea
            rows={3}
            className="mt-2 w-full rounded-md border border-[#cfd8cc] bg-white px-3 py-2 text-sm outline-none transition focus:border-[#345c3d] focus:ring-2 focus:ring-[#345c3d]/20"
            {...register('notes')}
          />
        </label>
      </div>

      <button
        type="submit"
        disabled={isSaving || categories.length === 0}
        className="mt-5 inline-flex h-10 items-center rounded-md border border-[#345c3d] bg-[#345c3d] px-4 text-sm font-medium text-white transition hover:bg-[#27472f] disabled:cursor-not-allowed disabled:opacity-70"
      >
        {isSaving ? 'Salvando...' : 'Criar saída'}
      </button>
    </form>
  )
}
