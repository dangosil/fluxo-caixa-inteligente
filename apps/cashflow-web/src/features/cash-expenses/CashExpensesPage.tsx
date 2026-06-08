import { PageHeader } from '../../components/shared/PageHeader'
import { PlaceholderPanel } from '../../components/ui/PlaceholderPanel'

export function CashExpensesPage() {
  return (
    <>
      <PageHeader
        title="Saídas"
        description="Área reservada para registrar e consultar saídas do fluxo de caixa."
      />
      <PlaceholderPanel title="Lançamentos de saída">
        A listagem e o formulário serão conectados à API em etapa futura.
      </PlaceholderPanel>
    </>
  )
}
