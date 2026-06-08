import { CategoryStatusBadge } from './CategoryStatusBadge'
import { CategoryTypeBadge } from './CategoryTypeBadge'
import type { Category } from './types'

type CategoryListProps = {
  categories: Category[]
}

export function CategoryList({ categories }: CategoryListProps) {
  return (
    <div className="overflow-hidden rounded-md border border-[#dfe4dc] bg-white shadow-sm">
      <div className="hidden grid-cols-[1fr_140px_120px] border-b border-[#dfe4dc] bg-[#f8faf7] px-4 py-3 text-xs font-semibold uppercase tracking-wide text-[#5f6f65] md:grid">
        <span>Nome</span>
        <span>Tipo</span>
        <span>Status</span>
      </div>

      <ul className="divide-y divide-[#edf1eb]">
        {categories.map((category) => (
          <li
            key={category.id}
            className="grid gap-3 px-4 py-4 md:grid-cols-[1fr_140px_120px] md:items-center"
          >
            <div>
              <p className="font-medium text-[#17211b]">{category.name}</p>
              <p className="mt-1 text-xs text-[#5f6f65]">ID: {category.id}</p>
            </div>
            <div>
              <CategoryTypeBadge type={category.type} />
            </div>
            <div>
              <CategoryStatusBadge active={category.active} />
            </div>
          </li>
        ))}
      </ul>
    </div>
  )
}
