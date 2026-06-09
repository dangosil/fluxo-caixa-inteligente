export function CashExpensesLoading() {
  return (
    <div className="rounded-md border border-[#dfe4dc] bg-white p-5 shadow-sm">
      <div className="h-5 w-40 animate-pulse rounded bg-[#e6ebe2]" />
      <div className="mt-5 space-y-3">
        {Array.from({ length: 4 }).map((_, index) => (
          <div key={index} className="h-16 animate-pulse rounded bg-[#f1f4ef]" />
        ))}
      </div>
    </div>
  )
}
