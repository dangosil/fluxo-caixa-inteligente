import type { CategoryType } from '../../types/api'

export type Category = {
  id: string
  name: string
  type: CategoryType
  active: boolean
  createdAt?: string
  updatedAt?: string
}

export type CategoryFilter = 'ALL' | CategoryType

export type CreateCategoryRequest = {
  name: string
  type: CategoryType
}
