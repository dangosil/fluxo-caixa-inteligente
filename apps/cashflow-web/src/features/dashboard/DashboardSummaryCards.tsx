import { SummaryCard } from './SummaryCard'
import type { DashboardSummaryResponse } from './types'

type DashboardSummaryCardsProps = {
  summary: DashboardSummaryResponse
}

export function DashboardSummaryCards({ summary }: DashboardSummaryCardsProps) {
  const dailyProfitTone = summary.daily.estimatedProfit < 0 ? 'warning' : 'profit'
  const monthlyProfitTone = summary.monthly.estimatedProfit < 0 ? 'warning' : 'profit'

  return (
    <div className="grid gap-4 md:grid-cols-2 xl:grid-cols-3">
      <SummaryCard label="Entradas hoje" value={summary.daily.totalIncome} tone="income" />
      <SummaryCard label="Saídas hoje" value={summary.daily.totalExpense} tone="expense" />
      <SummaryCard
        label="Lucro estimado hoje"
        value={summary.daily.estimatedProfit}
        tone={dailyProfitTone}
      />
      <SummaryCard label="Entradas do mês" value={summary.monthly.totalIncome} tone="income" />
      <SummaryCard label="Saídas do mês" value={summary.monthly.totalExpense} tone="expense" />
      <SummaryCard
        label="Lucro estimado do mês"
        value={summary.monthly.estimatedProfit}
        tone={monthlyProfitTone}
      />
    </div>
  )
}
