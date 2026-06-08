export type DashboardCashSummary = {
  totalIncome: number
  totalExpense: number
  estimatedProfit: number
}

export type DashboardMonthlySummary = DashboardCashSummary & {
  year: number
  month: number
}

export type DashboardSummaryResponse = {
  date: string
  daily: DashboardCashSummary
  monthly: DashboardMonthlySummary
}
