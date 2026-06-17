import { cleanup, render, screen } from '@testing-library/react'
import { afterEach, describe, expect, it } from 'vitest'

import { CashEntryList } from './CashEntryList'
import type { CashEntry } from './types'

const entry: CashEntry = {
  id: 'entry-1',
  description: 'Venda no cartão',
  amount: 100,
  feeAmount: 3,
  feePayer: 'MERCHANT',
  installmentCount: 1,
  customerPaidAmount: 100,
  receivedAmount: 97,
  entryDate: '2026-06-08',
  expectedReceiptDate: '2026-06-09',
  categoryId: 'category-1',
  categoryName: 'Vendas',
  paymentMethod: 'CREDIT_CARD',
  active: true,
}

afterEach(() => {
  cleanup()
})

describe('CashEntryList', () => {
  it('shows sale date and expected receipt date', () => {
    render(<CashEntryList entries={[entry]} />)

    expect(screen.getByText('Data da venda')).toBeInTheDocument()
    expect(screen.getByText('Recebimento previsto')).toBeInTheDocument()
    expect(screen.getByText('Venda em 08/06/2026')).toBeInTheDocument()
    expect(screen.getByText('Recebe em 09/06/2026')).toBeInTheDocument()
  })

  it('makes it clear when receipt happens on another date', () => {
    render(<CashEntryList entries={[entry]} />)

    expect(screen.getByText('Recebimento em outra data')).toBeInTheDocument()
  })

  it('shows a simple receipt date when sale and expected receipt dates are the same', () => {
    render(<CashEntryList entries={[{ ...entry, expectedReceiptDate: entry.entryDate }]} />)

    expect(screen.getByText('Venda em 08/06/2026')).toBeInTheDocument()
    expect(screen.getByText('Recebe em 08/06/2026')).toBeInTheDocument()
    expect(screen.queryByText('Recebimento em outra data')).not.toBeInTheDocument()
  })
})
