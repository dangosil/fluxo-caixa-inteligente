import { PageHeader } from '../../components/shared/PageHeader'
import { PlaceholderPanel } from '../../components/ui/PlaceholderPanel'

export function CategoriesPage() {
  return (
    <>
      <PageHeader
        title="Categorias"
        description="Área reservada para listar, criar, editar e desativar categorias de entrada e saída."
      />
      <PlaceholderPanel title="Cadastro de categorias">
        O CRUD será implementado em etapa futura usando os endpoints de categorias.
      </PlaceholderPanel>
    </>
  )
}
