type PlaceholderPanelProps = {
  title: string
  children: React.ReactNode
}

export function PlaceholderPanel({ title, children }: PlaceholderPanelProps) {
  return (
    <section className="rounded-md border border-[#dfe4dc] bg-white p-5 shadow-sm">
      <h3 className="text-base font-semibold text-[#17211b]">{title}</h3>
      <div className="mt-3 text-sm leading-6 text-[#5f6f65]">{children}</div>
    </section>
  )
}
