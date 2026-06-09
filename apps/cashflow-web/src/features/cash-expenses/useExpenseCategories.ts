import { useQuery } from '@tanstack/react-query'

import { getCategories } from '../categories/categoryApi'

export function useExpenseCategories() {
  return useQuery({
    queryKey: ['categories', 'EXPENSE', 'active'],
    queryFn: () => getCategories('EXPENSE').then((categories) => categories.filter((category) => category.active)),
  })
}
