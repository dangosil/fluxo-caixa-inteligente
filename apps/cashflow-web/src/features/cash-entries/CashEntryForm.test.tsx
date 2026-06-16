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
})
