import { api } from '../../lib/api'

import type { DashboardSummaryResponse } from './types'

export async function getDashboardSummary() {
  const response = await api.get<DashboardSummaryResponse>('/dashboard/summary')

  return response.data
}
