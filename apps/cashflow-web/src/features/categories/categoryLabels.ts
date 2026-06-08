import type { CategoryType } from '../../types/api'

export function categoryTypeLabel(type: CategoryType) {
  return type === 'INCOME' ? 'Entrada' : 'Saída'
}

export function categoryStatusLabel(active: boolean) {
  return active ? 'Ativa' : 'Inativa'
}
