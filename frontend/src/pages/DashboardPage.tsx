import { useEffect } from 'react'
import { usePatientStore } from '@/stores/patientStore'
import { useDepartmentStore } from '@/stores/departmentStore'
import { useBookingStore } from '@/stores/bookingStore'
import { useAuthStore } from '@/stores/authStore'
import { Users, CalendarDays, Building2, ArrowRight, TrendingUp } from 'lucide-react'
import { Link } from 'react-router-dom'

function getGreeting() {
  const h = new Date().getHours()
  if (h < 12) return 'Good morning'
  if (h < 17) return 'Good afternoon'
  return 'Good evening'
}

export default function DashboardPage() {
  const { patients, total, fetchPatients } = usePatientStore()
  const { departments, fetchDepartments } = useDepartmentStore()
  const { bookings } = useBookingStore()
  const email = useAuthStore(s => s.email)

  useEffect(() => { fetchPatients(0, 5); fetchDepartments() }, [])

  const firstName = email?.split('@')[0] ?? 'there'
  const stats = [
    { label: 'Total Patients', value: total, icon: Users, to: '/patients', trend: '+12% this month' },
    { label: 'Departments', value: departments.length, icon: Building2, to: '/departments', trend: 'Active units' },
    { label: 'Appointments', value: bookings.length, icon: CalendarDays, to: '/bookings', trend: 'This period' },
  ]

  return (
    <div className="max-w-4xl mx-auto space-y-5">
      {/* Banner */}
      <div className="relative px-8 py-7" style={{ background: 'linear-gradient(135deg,#1e4a7a 0%,#0f2a4d 100%)', border: '1px solid #1e4a7a' }}>
        <p className="text-sm" style={{ color: '#b8cef5' }}>{getGreeting()},</p>
        <h2 className="text-2xl font-bold text-white mt-0.5 capitalize">{firstName}</h2>
        <p className="text-sm mt-1" style={{ color: '#b8cef5' }}>Here's what's happening in your system today.</p>
        <div className="mt-4 inline-flex items-center gap-2 px-3 py-1" style={{ backgroundColor: '#1e3060', border: '1px solid #1e4a7a' }}>
          <span className="w-2 h-2 bg-blue-400 animate-pulse" />
          <span className="text-xs" style={{ color: '#b8cef5' }}>All systems operational</span>
        </div>
      </div>

      {/* Stats */}
      <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
        {stats.map(({ label, value, icon: Icon, to, trend }) => (
          <Link key={label} to={to} className="group block p-6 transition-all" style={{ backgroundColor: '#162040', border: '1px solid #1e3a6a' }}>
            <div className="flex items-start justify-between mb-5">
              <div className="w-10 h-10 flex items-center justify-center" style={{ backgroundColor: '#1a2d52' }}>
                <Icon size={20} style={{ color: '#93c5fd' }} />
              </div>
              <ArrowRight size={15} style={{ color: '#a0c0e8' }} className="mt-0.5" />
            </div>
            <p className="text-3xl font-bold leading-none" style={{ color: '#bfdbfe' }}>{value}</p>
            <p className="text-sm font-semibold mt-2 text-white">{label}</p>
            <p className="text-xs mt-1 flex items-center gap-1" style={{ color: '#a0c0e8' }}>
              <TrendingUp size={11} />{trend}
            </p>
          </Link>
        ))}
      </div>

      {/* Recent patients */}
      <div style={{ backgroundColor: '#162040', border: '1px solid #1e3a6a' }}>
        <div className="flex items-start justify-between px-6 py-5" style={{ borderBottom: '1px solid #1e3a6a' }}>
          <div>
            <h3 className="text-sm font-semibold" style={{ color: '#ffffff' }}>Recent Patients</h3>
            <p className="text-xs mt-0.5" style={{ color: '#a0c0e8' }}>Latest registered patients</p>
          </div>
          <Link to="/patients" className="text-xs font-semibold flex items-center gap-1" style={{ color: '#93c5fd' }}>
            View all <ArrowRight size={12} />
          </Link>
        </div>
        {patients.length === 0 ? (
          <div className="flex flex-col items-center py-12 px-6 text-center">
            <div className="w-12 h-12 flex items-center justify-center mb-3" style={{ backgroundColor: '#0f1c2e' }}>
              <Users size={22} style={{ color: '#8aabdc' }} />
            </div>
            <p className="text-sm font-semibold text-white">No patients yet</p>
            <p className="text-xs mt-1" style={{ color: '#a0c0e8' }}>Patients will appear here once added.</p>
            <Link to="/patients" className="mt-4 text-xs px-4 py-2 font-semibold text-white" style={{ backgroundColor: '#1d4ed8' }}>
              Add first patient
            </Link>
          </div>
        ) : (
          patients.map((p, i) => (
            <div key={p.id} className="flex items-center gap-4 px-6 py-4"
              style={{ borderBottom: i < patients.length - 1 ? '1px solid #0f1c2e' : 'none' }}>
              <div className="w-9 h-9 flex items-center justify-center font-bold text-sm shrink-0" style={{ backgroundColor: '#1a2d52', color: '#bfdbfe' }}>
                {p.name[0].toUpperCase()}
              </div>
              <div className="flex-1 min-w-0">
                <p className="text-sm font-semibold text-white truncate">{p.name}</p>
                <p className="text-xs truncate" style={{ color: '#a0c0e8' }}>{p.email}</p>
              </div>
              <span className="shrink-0 text-xs font-medium px-2.5 py-1" style={{ backgroundColor: '#0f1c2e', color: '#93c5fd' }}>
                {p.gender}
              </span>
            </div>
          ))
        )}
      </div>

      {/* Quick links */}
      <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
        {[
          { to: '/bookings', icon: CalendarDays, label: 'Manage Bookings', sub: 'View & update appointments' },
          { to: '/departments', icon: Building2, label: 'Departments', sub: 'Doctors, treatments & more' },
        ].map(({ to, icon: Icon, label, sub }) => (
          <Link key={to} to={to} className="flex items-center gap-4 px-6 py-5" style={{ backgroundColor: '#162040', border: '1px solid #1e3a6a' }}>
            <div className="w-10 h-10 flex items-center justify-center shrink-0" style={{ backgroundColor: '#1a2d52' }}>
              <Icon size={19} style={{ color: '#93c5fd' }} />
            </div>
            <div className="flex-1 min-w-0">
              <p className="text-sm font-semibold text-white">{label}</p>
              <p className="text-xs mt-0.5" style={{ color: '#a0c0e8' }}>{sub}</p>
            </div>
            <ArrowRight size={15} style={{ color: '#a0c0e8' }} className="shrink-0" />
          </Link>
        ))}
      </div>
    </div>
  )
}



