type CashExpenseStatusBadgeProps = {
  active: boolean
}

export function CashExpenseStatusBadge({ active }: CashExpenseStatusBadgeProps) {
  return (
    <span className="inline-flex rounded-md border border-[#dfe4dc] bg-white px-2 py-1 text-xs font-medium text-[#5f6f65]">
      {active ? 'Ativa' : 'Inativa'}
    </span>
  )
}
