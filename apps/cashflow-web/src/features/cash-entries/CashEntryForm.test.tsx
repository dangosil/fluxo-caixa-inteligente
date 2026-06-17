import { cleanup, render, screen, waitFor } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { afterEach, describe, expect, it, vi } from 'vitest'

import type { Category } from '../categories/types'

import { CashEntryForm } from './CashEntryForm'

const categories: Category[] = [
  {
    id: 'category-1',
    name: 'Vendas',
    type: 'INCOME',
    active: true,
  },
]

afterEach(() => {
  cleanup()
})

describe('CashEntryForm', () => {
  it.each([
    ['PIX', false],
    ['CASH', false],
    ['CREDIT_CARD', true],
    ['DEBIT_CARD', true],
  ] as const)('toggles card fee fields for %s', async (paymentMethod, shouldShowFeeFields) => {
    const user = userEvent.setup()

    render(<CashEntryForm categories={categories} isSaving={false} onSubmit={vi.fn()} />)

    await user.selectOptions(screen.getByLabelText('Forma de pagamento'), paymentMethod)

    const feeHeading = screen.queryByRole('heading', { name: 'Taxas da maquininha' })

    if (shouldShowFeeFields) {
      expect(feeHeading).toBeInTheDocument()
      expect(screen.getByLabelText('Taxa da maquininha')).toBeInTheDocument()
      expect(screen.getByLabelText('Quem paga a taxa')).toBeInTheDocument()
    } else {
      expect(feeHeading).not.toBeInTheDocument()
      expect(screen.queryByLabelText('Taxa da maquininha')).not.toBeInTheDocument()
      expect(screen.queryByLabelText('Quem paga a taxa')).not.toBeInTheDocument()
    }
  })

  it('renders sale date and expected receipt date fields', () => {
    render(<CashEntryForm categories={categories} isSaving={false} onSubmit={vi.fn()} />)

    expect(screen.getByLabelText('Data da venda')).toBeInTheDocument()
    expect(screen.getByLabelText('Recebimento previsto')).toBeInTheDocument()
    expect(screen.getByText('Usado nos resumos e no dashboard.')).toBeInTheDocument()
  })

  it('defaults expected receipt date to the sale date when sale date changes', async () => {
    const user = userEvent.setup()

    render(<CashEntryForm categories={categories} isSaving={false} onSubmit={vi.fn()} />)

    await user.clear(screen.getByLabelText('Data da venda'))
    await user.type(screen.getByLabelText('Data da venda'), '2026-06-08')

    expect(screen.getByLabelText('Recebimento previsto')).toHaveValue('2026-06-08')
  })

  it('preserves a manually changed expected receipt date when sale date changes again', async () => {
    const user = userEvent.setup()

    render(<CashEntryForm categories={categories} isSaving={false} onSubmit={vi.fn()} />)

    await user.clear(screen.getByLabelText('Data da venda'))
    await user.type(screen.getByLabelText('Data da venda'), '2026-06-08')
    await user.clear(screen.getByLabelText('Recebimento previsto'))
    await user.type(screen.getByLabelText('Recebimento previsto'), '2026-06-12')
    await user.clear(screen.getByLabelText('Data da venda'))
    await user.type(screen.getByLabelText('Data da venda'), '2026-06-09')

    expect(screen.getByLabelText('Recebimento previsto')).toHaveValue('2026-06-12')
  })

  it('submits expectedReceiptDate with the payload', async () => {
    const user = userEvent.setup()
    const onSubmit = vi.fn()

    render(<CashEntryForm categories={categories} isSaving={false} onSubmit={onSubmit} />)

    await user.type(screen.getByLabelText('Descrição'), 'Venda no cartão')
    await user.clear(screen.getByLabelText('Valor'))
    await user.type(screen.getByLabelText('Valor'), '100')
    await user.clear(screen.getByLabelText('Data da venda'))
    await user.type(screen.getByLabelText('Data da venda'), '2026-06-08')
    await user.clear(screen.getByLabelText('Recebimento previsto'))
    await user.type(screen.getByLabelText('Recebimento previsto'), '2026-06-09')
    await user.selectOptions(screen.getByLabelText('Categoria'), 'category-1')
    await user.selectOptions(screen.getByLabelText('Forma de pagamento'), 'CREDIT_CARD')
    await user.click(screen.getByRole('button', { name: 'Criar entrada' }))

    await waitFor(() => {
      expect(onSubmit).toHaveBeenCalledWith(expect.objectContaining({
        entryDate: '2026-06-08',
        expectedReceiptDate: '2026-06-09',
      }))
    })
  })

  it('clears card fee values from the payload after switching from credit card to pix', async () => {
    const user = userEvent.setup()
    const onSubmit = vi.fn()

    render(<CashEntryForm categories={categories} isSaving={false} onSubmit={onSubmit} />)

    await user.type(screen.getByLabelText('Descrição'), 'Venda pix')
    await user.clear(screen.getByLabelText('Valor'))
    await user.type(screen.getByLabelText('Valor'), '100')
    await user.selectOptions(screen.getByLabelText('Categoria'), 'category-1')
    await user.selectOptions(screen.getByLabelText('Forma de pagamento'), 'CREDIT_CARD')
    await user.clear(screen.getByLabelText('Taxa da maquininha'))
    await user.type(screen.getByLabelText('Taxa da maquininha'), '3.50')
    await user.selectOptions(screen.getByLabelText('Quem paga a taxa'), 'CUSTOMER')
    await user.clear(screen.getByLabelText('Número de parcelas'))
    await user.type(screen.getByLabelText('Número de parcelas'), '4')
    await user.selectOptions(screen.getByLabelText('Forma de pagamento'), 'PIX')
    await user.click(screen.getByRole('button', { name: 'Criar entrada' }))

    await waitFor(() => {
      expect(onSubmit).toHaveBeenCalledWith(expect.objectContaining({
        paymentMethod: 'PIX',
        feeAmount: 0,
        feePayer: 'MERCHANT',
        installmentCount: 1,
      }))
    })
  })

  it('keeps installment amount as an informational preview without changing totals', async () => {
    const user = userEvent.setup()

    render(<CashEntryForm categories={categories} isSaving={false} onSubmit={vi.fn()} />)

    await user.clear(screen.getByLabelText('Valor'))
    await user.type(screen.getByLabelText('Valor'), '100')
    await user.selectOptions(screen.getByLabelText('Forma de pagamento'), 'CREDIT_CARD')
    await user.clear(screen.getByLabelText('Taxa da maquininha'))
    await user.type(screen.getByLabelText('Taxa da maquininha'), '10')

    expect(screen.getByText('Cliente paga').nextElementSibling).toHaveTextContent('R$ 100,00')
    expect(screen.getByText('Empresa recebe').nextElementSibling).toHaveTextContent('R$ 90,00')

    await user.clear(screen.getByLabelText('Número de parcelas'))
    await user.type(screen.getByLabelText('Número de parcelas'), '3')

    expect(screen.getByText('3x de aproximadamente R$ 33,33')).toBeInTheDocument()
    expect(screen.getByText('Cliente paga').nextElementSibling).toHaveTextContent('R$ 100,00')
    expect(screen.getByText('Empresa recebe').nextElementSibling).toHaveTextContent('R$ 90,00')
    expect(screen.getByText(/Pode haver diferença por arredondamento/i)).toBeInTheDocument()
  })
})
