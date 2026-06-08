import { PageHeader } from '../../components/shared/PageHeader'

import { DashboardError } from './DashboardError'
import { DashboardLoading } from './DashboardLoading'
import { DashboardSummaryCards } from './DashboardSummaryCards'
import { useDashboardSummary } from './useDashboardSummary'

export function DashboardPage() {
  const { data: summary, isLoading, isError, refetch } = useDashboardSummary()

  return (
    <>
      <PageHeader
        title="Dashboard"
        description="Resumo financeiro calculado pelo backend a partir dos lançamentos ativos."
      />

      {isLoading && <DashboardLoading />}

      {isError && <DashboardError onRetry={() => void refetch()} />}

      {summary && (
        <div className="space-y-4">
          <div className="rounded-md border border-[#dfe4dc] bg-white px-4 py-3 text-sm text-[#5f6f65] shadow-sm">
            Data de referência: <strong className="text-[#17211b]">{summary.date}</strong>
          </div>
          <DashboardSummaryCards summary={summary} />
        </div>
      )}
    </>
  )
}
