import { categoryStatusLabel } from './categoryLabels'

type CategoryStatusBadgeProps = {
  active: boolean
}

export function CategoryStatusBadge({ active }: CategoryStatusBadgeProps) {
  return (
    <span className="inline-flex rounded-md border border-[#dfe4dc] bg-white px-2 py-1 text-xs font-medium text-[#5f6f65]">
      {categoryStatusLabel(active)}
    </span>
  )
}
