type CashEntriesErrorProps = {
  onRetry: () => void
}

export function CashEntriesError({ onRetry }: CashEntriesErrorProps) {
  return (
    <div className="rounded-md border border-[#f0d1c8] bg-[#fff7f4] p-5 text-[#8a3d2f] shadow-sm">
      <h3 className="text-base font-semibold">Não foi possível carregar as entradas</h3>
      <p className="mt-2 text-sm">Verifique se a API está rodando e tente novamente.</p>
      <button
        type="button"
        onClick={onRetry}
        className="mt-4 rounded-md border border-[#8a3d2f] px-3 py-2 text-sm font-medium transition hover:bg-[#8a3d2f] hover:text-white"
      >
        Tentar novamente
      </button>
    </div>
  )
}
