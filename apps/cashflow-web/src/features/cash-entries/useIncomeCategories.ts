import { useQuery } from '@tanstack/react-query'

import { getCategories } from '../categories/categoryApi'

export function useIncomeCategories() {
  return useQuery({
    queryKey: ['categories', 'INCOME', 'active'],
    queryFn: () => getCategories('INCOME').then((categories) => categories.filter((category) => category.active)),
  })
}
