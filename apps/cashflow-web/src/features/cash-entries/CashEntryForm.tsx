import { zodResolver } from '@hookform/resolvers/zod'
import { useEffect, useRef } from 'react'
import { useForm } from 'react-hook-form'
import { z } from 'zod'

import type { Category } from '../categories/types'

import { formatCurrency } from '../../lib/formatters'
import { paymentMethodOptions } from '../../lib/paymentMethodLabels'
import { cardBrandOptions, feePayerOptions } from './cardFeeLabels'
import type { CreateCashEntryRequest } from './types'

const optionalCurrencySchema = z.preprocess(
  (value) => (value === '' || value === null ? undefined : value),
  z.coerce.number().min(0, 'Informe um valor maior ou igual a zero.').optional(),
)

const optionalCardBrandSchema = z.preprocess(
  (value) => (value === '' ? undefined : value),
  z.enum(['VISA', 'MASTERCARD', 'ELO', 'AMEX', 'HIPERCARD', 'OTHER']).optional(),
)

const cashEntrySchema = z
  .object({
    description: z.string().trim().min(2, 'Informe uma descrição.'),
    amount: z.coerce.number().positive('Informe um valor maior que zero.'),
    entryDate: z.string().min(1, 'Informe a data.'),
    expectedReceiptDate: z.string().optional(),
    categoryId: z.string().min(1, 'Selecione uma categoria.'),
    paymentMethod: z.enum(['PIX', 'CASH', 'CREDIT_CARD', 'DEBIT_CARD', 'BANK_TRANSFER', 'OTHER'], {
      message: 'Selecione a forma de pagamento.',
    }),
    notes: z.string().optional(),
    feeAmount: optionalCurrencySchema,
    feePayer: z.enum(['MERCHANT', 'CUSTOMER'], {
      message: 'Selecione quem paga a taxa.',
    }),
    cardBrand: optionalCardBrandSchema,
    installmentCount: z.coerce.number().int().min(1, 'Informe ao menos 1 parcela.').optional(),
  })
  .superRefine((values, context) => {
    const feeAmount = values.feeAmount ?? 0

    if (values.feePayer === 'MERCHANT' && feeAmount > values.amount) {
      context.addIssue({
        code: 'custom',
        path: ['feeAmount'],
        message: 'A taxa não pode ser maior que o valor quando a empresa absorve.',
      })
    }
  })

type CashEntryFormInput = z.input<typeof cashEntrySchema>
type CashEntryFormValues = z.output<typeof cashEntrySchema>

type CashEntryFormProps = {
  categories: Category[]
  isSaving: boolean
  onSubmit: (values: CreateCashEntryRequest) => void
}

export function CashEntryForm({ categories, isSaving, onSubmit }: CashEntryFormProps) {
  const today = new Date().toISOString().slice(0, 10)
  const {
    register,
    handleSubmit,
    reset,
    setValue,
    watch,
    getValues,
    formState: { errors },
  } = useForm<CashEntryFormInput, unknown, CashEntryFormValues>({
    resolver: zodResolver(cashEntrySchema),
    defaultValues: {
      description: '',
      amount: 0,
      entryDate: today,
      expectedReceiptDate: today,
      categoryId: '',
      paymentMethod: 'PIX',
      notes: '',
      feeAmount: 0,
      feePayer: 'MERCHANT',
      cardBrand: undefined,
      installmentCount: 1,
    },
  })

  const paymentMethod = watch('paymentMethod')
  const entryDate = watch('entryDate')
  const previousEntryDateRef = useRef(entryDate)
  const isCardPayment = paymentMethod === 'CREDIT_CARD' || paymentMethod === 'DEBIT_CARD'
  const amount = Number(watch('amount') || 0)
  const feeAmount = Number(watch('feeAmount') || 0)
  const feePayer = watch('feePayer') || 'MERCHANT'
  const installmentCount = Number(watch('installmentCount') || 1)
  const customerPaidAmount = feePayer === 'CUSTOMER' ? amount + feeAmount : amount
  const receivedAmount = feePayer === 'MERCHANT' ? Math.max(amount - feeAmount, 0) : amount
  const installmentAmountPreview = installmentCount > 0 ? customerPaidAmount / installmentCount : customerPaidAmount

  useEffect(() => {
    if (!entryDate) {
      return
    }

    const previousEntryDate = previousEntryDateRef.current
    const currentExpectedReceiptDate = getValues('expectedReceiptDate')

    if (!currentExpectedReceiptDate || currentExpectedReceiptDate === previousEntryDate) {
      setValue('expectedReceiptDate', entryDate)
    }

    previousEntryDateRef.current = entryDate
  }, [entryDate, getValues, setValue])

  useEffect(() => {
    if (!isCardPayment) {
      setValue('feeAmount', 0)
      setValue('feePayer', 'MERCHANT')
      setValue('cardBrand', undefined)
      setValue('installmentCount', 1)
    }
  }, [isCardPayment, setValue])

  function submit(values: CashEntryFormValues) {
    const submittedInstallmentCount = values.installmentCount ?? 1
    const submittedFeeAmount = values.feeAmount ?? 0
    const submittedCustomerPaidAmount = values.feePayer === 'CUSTOMER'
      ? values.amount + submittedFeeAmount
      : values.amount
    const feePayload = values.paymentMethod === 'CREDIT_CARD' || values.paymentMethod === 'DEBIT_CARD'
      ? {
          feeAmount: submittedFeeAmount,
          feePayer: values.feePayer,
          cardBrand: values.cardBrand,
          installmentCount: submittedInstallmentCount,
          installmentAmount: submittedCustomerPaidAmount / submittedInstallmentCount,
        }
      : {
          feeAmount: 0,
          feePayer: 'MERCHANT' as const,
          cardBrand: undefined,
          installmentCount: 1,
          installmentAmount: undefined,
        }

    onSubmit({
      description: values.description.trim(),
      amount: values.amount,
      entryDate: values.entryDate,
      expectedReceiptDate: values.expectedReceiptDate || values.entryDate,
      categoryId: values.categoryId,
      paymentMethod: values.paymentMethod,
      notes: values.notes?.trim() || undefined,
      ...feePayload,
    })
    reset({
      description: '',
      amount: 0,
      entryDate: today,
      expectedReceiptDate: today,
      categoryId: '',
      paymentMethod: 'PIX',
      notes: '',
      feeAmount: 0,
      feePayer: 'MERCHANT',
      cardBrand: undefined,
      installmentCount: 1,
    })
  }

  return (
    <form
      onSubmit={handleSubmit(submit)}
      className="rounded-md border border-[#dfe4dc] bg-white p-5 shadow-sm"
    >
      <div>
        <h3 className="text-base font-semibold text-[#17211b]">Nova entrada</h3>
        <p className="mt-1 text-sm text-[#5f6f65]">Registre recebimentos do fluxo de caixa.</p>
      </div>

      <div className="mt-5 grid gap-4">
        <label className="block">
          <span className="text-sm font-medium text-[#334238]">Descrição</span>
          <input
            type="text"
            placeholder="Ex.: Venda no balcão"
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
            <span className="text-sm font-medium text-[#334238]">Data da venda</span>
            <input
              type="date"
              className="mt-2 h-11 w-full rounded-md border border-[#cfd8cc] bg-white px-3 text-sm outline-none transition focus:border-[#345c3d] focus:ring-2 focus:ring-[#345c3d]/20"
              {...register('entryDate')}
            />
            {errors.entryDate && <span className="mt-1 block text-sm text-[#8a3d2f]">{errors.entryDate.message}</span>}
          </label>

          <label className="block">
            <span id="expected-receipt-date-label" className="text-sm font-medium text-[#334238]">
              Recebimento previsto
            </span>
            <input
              type="date"
              aria-labelledby="expected-receipt-date-label"
              className="mt-2 h-11 w-full rounded-md border border-[#cfd8cc] bg-white px-3 text-sm outline-none transition focus:border-[#345c3d] focus:ring-2 focus:ring-[#345c3d]/20"
              {...register('expectedReceiptDate')}
            />
            <span className="mt-1 block text-xs text-[#5f6f65]">Usado nos resumos e no dashboard.</span>
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

        {isCardPayment && (
          <section className="rounded-md border border-[#dfe4dc] bg-[#f8faf7] p-4">
            <h4 className="text-sm font-semibold text-[#17211b]">Taxas da maquininha</h4>
            <div className="mt-4 grid gap-4 md:grid-cols-2">
              <label className="block">
                <span className="text-sm font-medium text-[#334238]">Taxa da maquininha</span>
                <input
                  type="number"
                  min="0"
                  step="0.01"
                  className="mt-2 h-11 w-full rounded-md border border-[#cfd8cc] bg-white px-3 text-sm outline-none transition focus:border-[#345c3d] focus:ring-2 focus:ring-[#345c3d]/20"
                  {...register('feeAmount')}
                />
                {errors.feeAmount && <span className="mt-1 block text-sm text-[#8a3d2f]">{errors.feeAmount.message}</span>}
              </label>

              <label className="block">
                <span className="text-sm font-medium text-[#334238]">Quem paga a taxa</span>
                <select
                  className="mt-2 h-11 w-full rounded-md border border-[#cfd8cc] bg-white px-3 text-sm outline-none transition focus:border-[#345c3d] focus:ring-2 focus:ring-[#345c3d]/20"
                  {...register('feePayer')}
                >
                  {feePayerOptions.map((option) => (
                    <option key={option.value} value={option.value}>
                      {option.label}
                    </option>
                  ))}
                </select>
                {errors.feePayer && <span className="mt-1 block text-sm text-[#8a3d2f]">{errors.feePayer.message}</span>}
              </label>

              <label className="block">
                <span className="text-sm font-medium text-[#334238]">Bandeira</span>
                <select
                  className="mt-2 h-11 w-full rounded-md border border-[#cfd8cc] bg-white px-3 text-sm outline-none transition focus:border-[#345c3d] focus:ring-2 focus:ring-[#345c3d]/20"
                  {...register('cardBrand')}
                >
                  <option value="">Não informar</option>
                  {cardBrandOptions.map((option) => (
                    <option key={option.value} value={option.value}>
                      {option.label}
                    </option>
                  ))}
                </select>
              </label>

              <label className="block">
                <span className="text-sm font-medium text-[#334238]">Número de parcelas</span>
                <input
                  type="number"
                  min="1"
                  step="1"
                  className="mt-2 h-11 w-full rounded-md border border-[#cfd8cc] bg-white px-3 text-sm outline-none transition focus:border-[#345c3d] focus:ring-2 focus:ring-[#345c3d]/20"
                  {...register('installmentCount')}
                />
                {errors.installmentCount && (
                  <span className="mt-1 block text-sm text-[#8a3d2f]">{errors.installmentCount.message}</span>
                )}
              </label>

              <div className="rounded-md border border-[#dfe4dc] bg-white p-3 md:col-span-2">
                <span className="text-xs font-medium uppercase tracking-wide text-[#5f6f65]">
                  Valor aproximado da parcela
                </span>
                <p className="mt-1 text-base font-semibold text-[#17211b]">
                  {installmentCount}x de aproximadamente {formatCurrency(installmentAmountPreview)}
                </p>
                <p className="mt-1 text-xs text-[#5f6f65]">
                  Valor informativo calculado pelo total pago pelo cliente. Pode haver diferença por arredondamento.
                </p>
              </div>
            </div>

            <div className="mt-4 grid gap-3 rounded-md border border-[#dfe4dc] bg-white p-3 sm:grid-cols-2">
              <div>
                <span className="text-xs font-medium uppercase tracking-wide text-[#5f6f65]">Cliente paga</span>
                <p className="mt-1 text-base font-semibold text-[#17211b]">{formatCurrency(customerPaidAmount)}</p>
              </div>
              <div>
                <span className="text-xs font-medium uppercase tracking-wide text-[#5f6f65]">Empresa recebe</span>
                <p className="mt-1 text-base font-semibold text-[#255c32]">{formatCurrency(receivedAmount)}</p>
              </div>
            </div>
          </section>
        )}
      </div>

      <button
        type="submit"
        disabled={isSaving || categories.length === 0}
        className="mt-5 inline-flex h-10 items-center rounded-md border border-[#345c3d] bg-[#345c3d] px-4 text-sm font-medium text-white transition hover:bg-[#27472f] disabled:cursor-not-allowed disabled:opacity-70"
      >
        {isSaving ? 'Salvando...' : 'Criar entrada'}
      </button>
    </form>
  )
}
