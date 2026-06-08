type PageHeaderProps = {
  title: string
  description: string
}

export function PageHeader({ title, description }: PageHeaderProps) {
  return (
    <div className="mb-6">
      <h2 className="text-xl font-semibold text-[#17211b]">{title}</h2>
      <p className="mt-1 max-w-2xl text-sm text-[#5f6f65]">{description}</p>
    </div>
  )
}
