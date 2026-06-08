import { PageHeader } from '../../components/shared/PageHeader'
import { PlaceholderPanel } from '../../components/ui/PlaceholderPanel'
import { formatCurrency } from '../../lib/formatters'

export function DashboardPage() {
  return (
    <>
      <PageHeader
        title="Dashboard"
        description="Resumo inicial do fluxo de caixa. Os dados reais serão conectados à API nas próximas tarefas."
      />

      <div className="grid gap-4 md:grid-cols-3">
        <PlaceholderPanel title="Entradas do dia">
          <strong className="text-[#345c3d]">{formatCurrency(0)}</strong>
        </PlaceholderPanel>
        <PlaceholderPanel title="Saídas do dia">
          <strong className="text-[#8a3d2f]">{formatCurrency(0)}</strong>
        </PlaceholderPanel>
        <PlaceholderPanel title="Lucro estimado">
          <strong className="text-[#17211b]">{formatCurrency(0)}</strong>
        </PlaceholderPanel>
      </div>
    </>
  )
}
