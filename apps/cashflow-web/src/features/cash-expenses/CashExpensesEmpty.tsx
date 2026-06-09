export function CashExpensesEmpty() {
  return (
    <div className="rounded-md border border-dashed border-[#cfd8cc] bg-white p-8 text-center shadow-sm">
      <h3 className="text-base font-semibold text-[#17211b]">Nenhuma saída encontrada</h3>
      <p className="mt-2 text-sm text-[#5f6f65]">
        Crie uma saída ou ajuste os filtros para visualizar outros lançamentos.
      </p>
    </div>
  )
}
