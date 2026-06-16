import { describe, expect, it } from 'vitest'

import { formatCurrency } from './formatters'

describe('formatCurrency', () => {
  it('formats a positive number as Brazilian Real', () => {
    expect(formatCurrency(1234.56)).toBe('R$\u00A01.234,56')
  })

  it('formats zero as Brazilian Real', () => {
    expect(formatCurrency(0)).toBe('R$\u00A00,00')
  })
})
