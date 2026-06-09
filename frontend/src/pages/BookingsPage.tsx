import { useState } from 'react'
import { useBookingStore } from '@/stores/bookingStore'
import { CalendarDays, CheckCircle, XCircle, Clock, Search } from 'lucide-react'

const statusConfig = {
  BOOKED:    { label: 'Booked',    bg: '#0d1f2e', color: '#93c5fd', icon: Clock },
  COMPLETED: { label: 'Completed', bg: '#1e4a7a', color: '#93c5fd', icon: CheckCircle },
  CANCELLED: { label: 'Cancelled', bg: '#2e0a0a', color: '#f87171', icon: XCircle },
}

export default function BookingsPage() {
  const { bookings, loading, cancelBooking, completeBooking, fetchByPatient } = useBookingStore()
  const [patientId, setPatientId] = useState('')
  const [queried, setQueried] = useState(false)

  const handleSearch = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!patientId.trim()) return
    await fetchByPatient(patientId.trim())
    setQueried(true)
  }

  return (
    <div className="max-w-4xl mx-auto space-y-5">
      <div>
        <h2 className="text-lg font-bold" style={{ color: '#ffffff' }}>Bookings</h2>
        <p className="text-sm mt-0.5" style={{ color: '#a0c0e8' }}>Look up appointments by patient ID</p>
      </div>

      <div className="px-6 py-5" style={{ backgroundColor: '#162040', border: '1px solid #1e3a6a' }}>
        <p className="text-sm font-semibold text-white mb-3">Find Patient Appointments</p>
        <form onSubmit={handleSearch} className="flex gap-3">
          <div className="relative flex-1">
            <Search size={15} className="absolute left-3.5 top-1/2 -translate-y-1/2 pointer-events-none" style={{ color: '#a0c0e8' }} />
            <input type="text" placeholder="Enter patient UUID…" value={patientId}
              onChange={e => setPatientId(e.target.value)}
              className="w-full pl-10 pr-4 py-2.5 text-sm text-white focus:outline-none transition-colors"
              style={{ backgroundColor: '#0f1c2e', border: '1px solid #1e3a6a' }}
              onFocus={e => (e.target.style.borderColor = '#1d4ed8')}
              onBlur={e => (e.target.style.borderColor = '#1e3a6a')}
            />
          </div>
          <button type="submit" className="px-5 py-2.5 text-sm font-semibold text-white shrink-0 transition-colors"
            style={{ backgroundColor: '#1d4ed8' }}
            onMouseEnter={e => (e.currentTarget.style.backgroundColor = '#1e3a8a')}
            onMouseLeave={e => (e.currentTarget.style.backgroundColor = '#1d4ed8')}
          >Search</button>
        </form>
      </div>

      <div style={{ backgroundColor: '#162040', border: '1px solid #1e3a6a' }}>
        {!queried ? (
          <div className="flex flex-col items-center py-16 px-6 text-center">
            <div className="w-14 h-14 flex items-center justify-center mb-3" style={{ backgroundColor: '#0f1c2e' }}>
              <CalendarDays size={26} style={{ color: '#8aabdc' }} />
            </div>
            <p className="text-sm font-semibold text-white">No search yet</p>
            <p className="text-xs mt-1" style={{ color: '#a0c0e8' }}>Enter a patient UUID above to load their bookings.</p>
          </div>
        ) : loading ? (
          <div className="flex items-center justify-center py-16">
            <div className="w-6 h-6 border-2 border-t-transparent animate-spin" style={{ borderColor: '#1d4ed8', borderTopColor: 'transparent' }} />
          </div>
        ) : bookings.length === 0 ? (
          <div className="flex flex-col items-center py-16 px-6 text-center">
            <div className="w-14 h-14 flex items-center justify-center mb-3" style={{ backgroundColor: '#0f1c2e' }}>
              <CalendarDays size={26} style={{ color: '#8aabdc' }} />
            </div>
            <p className="text-sm font-semibold text-white">No bookings found</p>
            <p className="text-xs mt-1" style={{ color: '#a0c0e8' }}>This patient has no appointments on record.</p>
          </div>
        ) : (
          <>
            <div className="px-6 py-4" style={{ borderBottom: '1px solid #1e3a6a', backgroundColor: '#0f1c2e' }}>
              <p className="text-sm font-semibold" style={{ color: '#ffffff' }}>
                {bookings.length} appointment{bookings.length !== 1 ? 's' : ''} found
              </p>
            </div>
            {bookings.map((b, i) => {
              const s = statusConfig[b.status]
              const Icon = s.icon
              return (
                <div key={b.id} className="px-6 py-5"
                  style={{ borderBottom: i < bookings.length - 1 ? '1px solid #0f1c2e' : 'none' }}>
                  <div className="flex items-start justify-between gap-6 flex-wrap">
                    <div className="space-y-2">
                      <div className="flex items-center gap-2 flex-wrap">
                        <CalendarDays size={15} style={{ color: '#a0c0e8' }} />
                        <span className="text-sm font-semibold text-white">{b.appointmentDate}</span>
                        <span className="text-xs" style={{ color: '#a0c0e8' }}>{b.startTime} – {b.endTime}</span>
                      </div>
                      <p className="text-xs" style={{ color: '#a0c0e8' }}>
                        Doctor ID: <span className="font-semibold text-white">{b.doctorId}</span>
                        &nbsp;·&nbsp;Treatment ID: <span className="font-semibold text-white">{b.treatmentId}</span>
                      </p>
                    </div>
                    <div className="flex items-center gap-3 shrink-0">
                      <span className="inline-flex items-center gap-1.5 text-xs font-semibold px-3 py-1.5"
                        style={{ backgroundColor: s.bg, color: s.color }}>
                        <Icon size={12} /> {s.label}
                      </span>
                      {b.status === 'BOOKED' && (
                        <div className="flex gap-2">
                          <button onClick={async () => { await completeBooking(b.id); fetchByPatient(patientId) }}
                            className="text-xs font-semibold px-3 py-1.5 transition-colors"
                            style={{ border: '1px solid #1e4a7a', color: '#93c5fd' }}>Complete</button>
                          <button onClick={async () => { await cancelBooking(b.id); fetchByPatient(patientId) }}
                            className="text-xs font-semibold px-3 py-1.5 transition-colors"
                            style={{ border: '1px solid #3d1414', color: '#f87171' }}>Cancel</button>
                        </div>
                      )}
                    </div>
                  </div>
                </div>
              )
            })}
          </>
        )}
      </div>
    </div>
  )
}



