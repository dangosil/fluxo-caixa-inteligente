import { PageHeader } from '../../components/shared/PageHeader'
import { PlaceholderPanel } from '../../components/ui/PlaceholderPanel'

export function CashEntriesPage() {
  return (
    <>
      <PageHeader
        title="Entradas"
        description="Área reservada para registrar e consultar entradas do fluxo de caixa."
      />
      <PlaceholderPanel title="Lançamentos de entrada">
        A listagem e o formulário serão conectados à API em etapa futura.
      </PlaceholderPanel>
    </>
  )
}
