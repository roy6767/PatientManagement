import { Link, useLocation, useNavigate } from 'react-router-dom'
import { useAuthStore } from '@/stores/authStore'
import { LayoutDashboard, Users, CalendarDays, Building2, LogOut, Activity, Menu, X, ChevronRight } from 'lucide-react'
import { useState } from 'react'

const navItems = [
  { to: '/dashboard',   label: 'Dashboard',   icon: LayoutDashboard },
  { to: '/patients',    label: 'Patients',     icon: Users },
  { to: '/bookings',    label: 'Bookings',     icon: CalendarDays },
  { to: '/departments', label: 'Departments',  icon: Building2 },
]

export default function Layout({ children }: { children: React.ReactNode }) {
  const location = useLocation()
  const navigate = useNavigate()
  const { email, logout } = useAuthStore()
  const [open, setOpen] = useState(false)
  const currentPage = navItems.find(n => location.pathname.startsWith(n.to))

  return (
    <div className="flex h-screen overflow-hidden" style={{ backgroundColor: '#0f1c2e' }}>

      {/* Sidebar */}
      <aside
        className={`fixed inset-y-0 left-0 z-50 w-60 flex flex-col transform transition-transform duration-300 md:relative md:translate-x-0 ${open ? 'translate-x-0' : '-translate-x-full'}`}
        style={{ backgroundColor: '#0a1520', borderRight: '1px solid #1e3a6a' }}
      >
        <div className="flex items-center gap-3 px-6 py-5 shrink-0" style={{ borderBottom: '1px solid #1e3a6a' }}>
          <div className="w-8 h-8 flex items-center justify-center shrink-0" style={{ backgroundColor: '#1d4ed8' }}>
            <Activity size={17} className="text-[#93c5fd]" />
          </div>
          <div>
            <p className="font-bold text-white text-sm leading-none">MediCare</p>
            <p className="text-[10px] mt-0.5" style={{ color: '#5a7fa8' }}>Patient System</p>
          </div>
        </div>

        <nav className="flex-1 py-5 px-3 space-y-0.5 overflow-y-auto">
          <p className="text-[10px] font-bold uppercase tracking-widest px-3 mb-3" style={{ color: '#3a5a8a' }}>Menu</p>
          {navItems.map(({ to, label, icon: Icon }) => {
            const active = location.pathname.startsWith(to)
            return (
              <Link key={to} to={to} onClick={() => setOpen(false)}
                className="flex items-center gap-3 px-3 py-2.5 text-sm font-medium transition-colors"
                style={{ backgroundColor: active ? '#1a2d52' : 'transparent', color: active ? '#93c5fd' : '#7b96c2' }}
                onMouseEnter={e => { if (!active) (e.currentTarget as HTMLAnchorElement).style.backgroundColor = '#0f1c2e' }}
                onMouseLeave={e => { if (!active) (e.currentTarget as HTMLAnchorElement).style.backgroundColor = 'transparent' }}
              >
                <Icon size={16} className="shrink-0" />
                <span className="flex-1 leading-none">{label}</span>
                {active && <ChevronRight size={12} style={{ color: '#5a7fa8' }} />}
              </Link>
            )
          })}
        </nav>

        <div className="px-3 py-4 shrink-0" style={{ borderTop: '1px solid #1e3a6a' }}>
          <div className="flex items-center gap-3 px-3 py-2.5" style={{ backgroundColor: '#0f1c2e' }}>
            <div className="w-8 h-8 flex items-center justify-center shrink-0 font-bold text-sm" style={{ backgroundColor: '#1a2d52', color: '#93c5fd' }}>
              {email?.[0]?.toUpperCase() ?? 'U'}
            </div>
            <div className="flex-1 min-w-0">
              <p className="text-xs font-medium text-white truncate leading-none">{email}</p>
              <p className="text-[10px] mt-0.5" style={{ color: '#5a7fa8' }}>Administrator</p>
            </div>
          </div>
          <button onClick={() => { logout(); navigate('/login') }}
            className="mt-2 w-full flex items-center gap-2 px-3 py-2 text-xs transition-colors"
            style={{ color: '#7b96c2' }}
            onMouseEnter={e => (e.currentTarget.style.color = '#93c5fd')}
            onMouseLeave={e => (e.currentTarget.style.color = '#7b96c2')}
          >
            <LogOut size={13} /> Sign out
          </button>
        </div>
      </aside>

      {open && <div className="fixed inset-0 z-40 md:hidden" style={{ backgroundColor: 'rgba(0,0,0,0.7)' }} onClick={() => setOpen(false)} />}

      <div className="flex-1 flex flex-col overflow-hidden min-w-0">
        <header className="shrink-0 px-6 py-4 flex items-center gap-4" style={{ backgroundColor: '#0d1a2e', borderBottom: '1px solid #1e3a6a' }}>
          <button className="md:hidden w-8 h-8 flex items-center justify-center" style={{ color: '#93c5fd' }} onClick={() => setOpen(!open)}>
            {open ? <X size={19} /> : <Menu size={19} />}
          </button>
          <div className="flex-1 min-w-0">
            <p className="text-sm font-bold leading-none" style={{ color: '#e0eaff' }}>{currentPage?.label ?? 'Patient Management'}</p>
            <p className="text-xs mt-0.5 hidden sm:block" style={{ color: '#5a7fa8' }}>
              {new Date().toLocaleDateString('en-SE', { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' })}
            </p>
          </div>
          <div className="hidden sm:flex items-center gap-2 px-3 py-1.5" style={{ backgroundColor: '#0f1c2e', border: '1px solid #1e3a6a' }}>
            <span className="w-1.5 h-1.5 bg-[#3b82f6] animate-pulse shrink-0" />
            <span className="text-xs font-medium" style={{ color: '#60a5fa' }}>Online</span>
          </div>
        </header>

        <main className="flex-1 overflow-y-auto p-6" style={{ backgroundColor: '#0f1c2e' }}>
          {children}
        </main>
      </div>
    </div>
  )
}


