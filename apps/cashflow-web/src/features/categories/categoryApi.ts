import { api } from '../../lib/api'

import type { Category, CategoryFilter, CreateCategoryRequest } from './types'

export async function getCategories(type: CategoryFilter) {
  const response = await api.get<Category[]>('/categories', {
    params: type === 'ALL' ? undefined : { type },
  })

  return response.data
}

export async function createCategory(request: CreateCategoryRequest) {
  const response = await api.post<Category>('/categories', request)

  return response.data
}
