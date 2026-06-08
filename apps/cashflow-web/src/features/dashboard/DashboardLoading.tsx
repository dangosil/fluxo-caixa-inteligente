export function DashboardLoading() {
  return (
    <div className="grid gap-4 md:grid-cols-2 xl:grid-cols-3">
      {Array.from({ length: 6 }).map((_, index) => (
        <div
          key={index}
          className="h-32 animate-pulse rounded-md border border-[#dfe4dc] bg-white p-5 shadow-sm"
        >
          <div className="h-4 w-32 rounded bg-[#e6ebe2]" />
          <div className="mt-8 h-7 w-40 rounded bg-[#e6ebe2]" />
        </div>
      ))}
    </div>
  )
}
