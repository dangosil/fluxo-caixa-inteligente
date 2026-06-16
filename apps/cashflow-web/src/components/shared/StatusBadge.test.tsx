import { render, screen } from '@testing-library/react'
import { describe, expect, it } from 'vitest'

import { StatusBadge } from './StatusBadge'

describe('StatusBadge', () => {
  it('renders the active label', () => {
    render(<StatusBadge active />)

    expect(screen.getByText('Ativa')).toBeInTheDocument()
  })

  it('renders the inactive label', () => {
    render(<StatusBadge active={false} />)

    expect(screen.getByText('Inativa')).toBeInTheDocument()
  })
})
