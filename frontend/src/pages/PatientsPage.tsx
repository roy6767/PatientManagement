import { useEffect, useState } from 'react'
import { usePatientStore } from '@/stores/patientStore'
import { Search, Plus, ChevronLeft, ChevronRight, Users } from 'lucide-react'
import PatientModal from '@/components/PatientModal'

export default function PatientsPage() {
  const { patients, total, loading, fetchPatients } = usePatientStore()
  const [search, setSearch] = useState('')
  const [page, setPage] = useState(0)
  const [showModal, setShowModal] = useState(false)
  const pageSize = 10
  const totalPages = Math.ceil(total / pageSize)
  useEffect(() => { fetchPatients(page, pageSize, search) }, [page, search])

  return (
    <div className="max-w-4xl mx-auto space-y-5">
      <div className="flex items-start justify-between gap-4 flex-wrap">
        <div>
          <h2 className="text-lg font-bold" style={{ color: '#ffffff' }}>Patients</h2>
          <p className="text-sm mt-0.5" style={{ color: '#a0c0e8' }}>{total} total registered patients</p>
        </div>
        <button onClick={() => setShowModal(true)}
          className="flex items-center gap-2 text-sm font-semibold px-5 py-2.5 text-white transition-colors"
          style={{ backgroundColor: '#1d4ed8' }}
          onMouseEnter={e => (e.currentTarget.style.backgroundColor = '#1e3a8a')}
          onMouseLeave={e => (e.currentTarget.style.backgroundColor = '#1d4ed8')}
        >
          <Plus size={15} /> New Patient
        </button>
      </div>

      <div className="relative max-w-xs">
        <Search size={15} className="absolute left-3.5 top-1/2 -translate-y-1/2 pointer-events-none" style={{ color: '#a0c0e8' }} />
        <input type="text" placeholder="Search patients…" value={search}
          onChange={e => { setSearch(e.target.value); setPage(0) }}
          className="w-full pl-10 pr-4 py-2.5 text-sm text-white focus:outline-none transition-colors"
          style={{ backgroundColor: '#162040', border: '1px solid #1e3a6a', color: '#e8f0fe' }}
          onFocus={e => (e.target.style.borderColor = '#1d4ed8')}
          onBlur={e => (e.target.style.borderColor = '#1e3a6a')}
        />
      </div>

      <div style={{ backgroundColor: '#162040', border: '1px solid #1e3a6a' }}>
        {loading ? (
          <div className="flex items-center justify-center py-16">
            <div className="w-6 h-6 border-2 border-t-transparent animate-spin" style={{ borderColor: '#1d4ed8', borderTopColor: 'transparent' }} />
          </div>
        ) : patients.length === 0 ? (
          <div className="flex flex-col items-center py-16 px-6 text-center">
            <div className="w-14 h-14 flex items-center justify-center mb-3" style={{ backgroundColor: '#0f1c2e' }}>
              <Users size={26} style={{ color: '#8aabdc' }} />
            </div>
            <p className="text-sm font-semibold text-white">No patients found</p>
            <p className="text-xs mt-1" style={{ color: '#a0c0e8' }}>{search ? 'Try a different search term.' : 'Add your first patient.'}</p>
          </div>
        ) : (
          <>
            <div className="grid grid-cols-12 px-6 py-3" style={{ backgroundColor: '#0f1c2e', borderBottom: '1px solid #1e3a6a' }}>
              <div className="col-span-5 text-xs font-bold uppercase tracking-wide" style={{ color: '#a0c0e8' }}>Patient</div>
              <div className="col-span-4 text-xs font-bold uppercase tracking-wide hidden md:block" style={{ color: '#a0c0e8' }}>Phone</div>
              <div className="col-span-3 text-xs font-bold uppercase tracking-wide" style={{ color: '#a0c0e8' }}>Gender</div>
            </div>
            {patients.map((p, i) => (
              <div key={p.id} className="grid grid-cols-12 items-center px-6 py-4"
                style={{ borderBottom: i < patients.length - 1 ? '1px solid #0f1c2e' : 'none' }}>
                <div className="col-span-5 flex items-center gap-3 min-w-0 pr-3">
                  <div className="w-9 h-9 flex items-center justify-center font-bold text-sm shrink-0" style={{ backgroundColor: '#1a2d52', color: '#bfdbfe' }}>
                    {p.name[0].toUpperCase()}
                  </div>
                  <div className="min-w-0">
                    <p className="text-sm font-semibold text-white truncate">{p.name}</p>
                    <p className="text-xs truncate" style={{ color: '#a0c0e8' }}>{p.email}</p>
                  </div>
                </div>
                <div className="col-span-4 text-sm hidden md:block pr-3" style={{ color: '#b8cef5' }}>{p.phoneNumber || '—'}</div>
                <div className="col-span-3">
                  <span className="text-xs font-semibold px-2.5 py-1" style={{ backgroundColor: '#0f1c2e', color: '#93c5fd' }}>{p.gender}</span>
                </div>
              </div>
            ))}
            {totalPages > 1 && (
              <div className="flex items-center justify-between px-6 py-3.5" style={{ borderTop: '1px solid #1e3a6a', backgroundColor: '#0f1c2e' }}>
                <p className="text-xs" style={{ color: '#a0c0e8' }}>
                  Showing <span className="font-semibold" style={{ color: '#93c5fd' }}>{page * pageSize + 1}–{Math.min((page+1)*pageSize, total)}</span> of <span className="font-semibold" style={{ color: '#93c5fd' }}>{total}</span>
                </p>
                <div className="flex items-center gap-1.5">
                  <button onClick={() => setPage(p => Math.max(0, p-1))} disabled={page === 0}
                    className="w-8 h-8 flex items-center justify-center transition disabled:opacity-30"
                    style={{ border: '1px solid #1e3a6a', color: '#93c5fd' }}>
                    <ChevronLeft size={15} />
                  </button>
                  <span className="text-xs px-1" style={{ color: '#a0c0e8' }}>{page+1} / {totalPages}</span>
                  <button onClick={() => setPage(p => Math.min(totalPages-1, p+1))} disabled={page >= totalPages-1}
                    className="w-8 h-8 flex items-center justify-center transition disabled:opacity-30"
                    style={{ border: '1px solid #1e3a6a', color: '#93c5fd' }}>
                    <ChevronRight size={15} />
                  </button>
                </div>
              </div>
            )}
          </>
        )}
      </div>
      {showModal && <PatientModal onClose={() => { setShowModal(false); fetchPatients(page, pageSize, search) }} />}
    </div>
  )
}



