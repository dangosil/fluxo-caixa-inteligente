import { LayoutDashboard, ListTree, TrendingDown, TrendingUp } from 'lucide-react'
import { NavLink, Outlet } from 'react-router-dom'

import { cn } from '../../lib/cn'

const navigation = [
  { label: 'Dashboard', path: '/dashboard', icon: LayoutDashboard },
  { label: 'Categorias', path: '/categories', icon: ListTree },
  { label: 'Entradas', path: '/cash-entries', icon: TrendingUp },
  { label: 'Saídas', path: '/cash-expenses', icon: TrendingDown },
]

export function AppLayout() {
  return (
    <div className="min-h-screen bg-[#f6f7f4] text-[#17211b]">
      <header className="border-b border-[#dfe4dc] bg-white">
        <div className="mx-auto flex max-w-6xl flex-col gap-4 px-4 py-4 sm:px-6 lg:flex-row lg:items-center lg:justify-between">
          <div>
            <p className="text-sm font-medium text-[#5f6f65]">Fluxo de Caixa</p>
            <h1 className="text-2xl font-semibold text-[#17211b]">Painel financeiro</h1>
          </div>

          <nav className="flex flex-wrap gap-2">
            {navigation.map((item) => {
              const Icon = item.icon

              return (
                <NavLink
                  key={item.path}
                  to={item.path}
                  className={({ isActive }) =>
                    cn(
                      'inline-flex h-10 items-center gap-2 rounded-md border px-3 text-sm font-medium transition',
                      isActive
                        ? 'border-[#345c3d] bg-[#345c3d] text-white'
                        : 'border-[#dfe4dc] bg-white text-[#334238] hover:border-[#9faf99]',
                    )
                  }
                >
                  <Icon size={16} aria-hidden="true" />
                  {item.label}
                </NavLink>
              )
            })}
          </nav>
        </div>
      </header>

      <main className="mx-auto max-w-6xl px-4 py-6 sm:px-6">
        <Outlet />
      </main>
    </div>
  )
}
