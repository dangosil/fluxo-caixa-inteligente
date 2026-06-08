import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'

import { createCategory, getCategories } from './categoryApi'
import type { CategoryFilter, CreateCategoryRequest } from './types'

export function useCategories(type: CategoryFilter) {
  return useQuery({
    queryKey: ['categories', type],
    queryFn: () => getCategories(type),
  })
}

export function useCreateCategory() {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: (request: CreateCategoryRequest) => createCategory(request),
    onSuccess: async () => {
      await queryClient.invalidateQueries({ queryKey: ['categories'] })
    },
  })
}
