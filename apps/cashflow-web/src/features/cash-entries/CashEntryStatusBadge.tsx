type CashEntryStatusBadgeProps = {
  active: boolean
}

export function CashEntryStatusBadge({ active }: CashEntryStatusBadgeProps) {
  return (
    <span className="inline-flex rounded-md border border-[#dfe4dc] bg-white px-2 py-1 text-xs font-medium text-[#5f6f65]">
      {active ? 'Ativa' : 'Inativa'}
    </span>
  )
}
