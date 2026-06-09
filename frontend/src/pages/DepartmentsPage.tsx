import { useEffect, useState } from 'react'
import { useDepartmentStore } from '@/stores/departmentStore'
import { Building2, User, Stethoscope, ChevronDown, ChevronUp } from 'lucide-react'

export default function DepartmentsPage() {
  const { departments, doctors, treatments, loading, fetchDepartments, fetchDoctorsByDept, fetchTreatmentsByDept } = useDepartmentStore()
  const [expanded, setExpanded] = useState<number | null>(null)
  const [loadingDept, setLoadingDept] = useState<number | null>(null)

  useEffect(() => { fetchDepartments() }, [])

  const toggle = async (id: number) => {
    if (expanded === id) { setExpanded(null); return }
    setExpanded(id); setLoadingDept(id)
    await Promise.all([fetchDoctorsByDept(id), fetchTreatmentsByDept(id)])
    setLoadingDept(null)
  }

  return (
    <div className="max-w-3xl mx-auto space-y-5">
      <div>
        <h2 className="text-lg font-bold" style={{ color: '#ffffff' }}>Departments</h2>
        <p className="text-sm mt-0.5" style={{ color: '#a0c0e8' }}>
          {departments.length} department{departments.length !== 1 ? 's' : ''} · Click any row to expand
        </p>
      </div>

      {loading ? (
        <div className="flex items-center justify-center py-16">
          <div className="w-6 h-6 border-2 border-t-transparent animate-spin" style={{ borderColor: '#1d4ed8', borderTopColor: 'transparent' }} />
        </div>
      ) : departments.length === 0 ? (
        <div className="flex flex-col items-center py-16 px-6 text-center" style={{ backgroundColor: '#162040', border: '1px solid #1e3a6a' }}>
          <div className="w-14 h-14 flex items-center justify-center mb-3" style={{ backgroundColor: '#0f1c2e' }}>
            <Building2 size={26} style={{ color: '#8aabdc' }} />
          </div>
          <p className="text-sm font-semibold text-white">No departments found</p>
          <p className="text-xs mt-1" style={{ color: '#a0c0e8' }}>No departments are currently in the system.</p>
        </div>
      ) : (
        <div className="space-y-3">
          {departments.map((dept) => (
            <div key={dept.id} style={{ backgroundColor: '#162040', border: '1px solid #1e3a6a' }}>
              <button onClick={() => toggle(dept.id)} className="w-full flex items-center gap-4 px-6 py-4 text-left">
                <div className="w-10 h-10 flex items-center justify-center shrink-0" style={{ backgroundColor: '#1a2d52' }}>
                  <Building2 size={18} style={{ color: '#93c5fd' }} />
                </div>
                <div className="flex-1 min-w-0">
                  <p className="text-sm font-semibold text-white">{dept.name}</p>
                  <p className="text-xs mt-0.5 truncate" style={{ color: '#a0c0e8' }}>{dept.description || 'No description'}</p>
                </div>
                <div className="flex items-center gap-3 shrink-0">
                  <span className="text-xs font-semibold px-2.5 py-1"
                    style={dept.active ? { backgroundColor: '#1e4a7a', color: '#93c5fd' } : { backgroundColor: '#1a1a1a', color: '#6b7280' }}>
                    {dept.active ? 'Active' : 'Inactive'}
                  </span>
                  <div className="w-7 h-7 flex items-center justify-center" style={{ backgroundColor: '#0f1c2e', color: '#a0c0e8' }}>
                    {expanded === dept.id ? <ChevronUp size={14} /> : <ChevronDown size={14} />}
                  </div>
                </div>
              </button>

              {expanded === dept.id && (
                <div className="px-6 py-5" style={{ borderTop: '1px solid #1e3a6a' }}>
                  {loadingDept === dept.id ? (
                    <div className="flex items-center justify-center py-6">
                      <div className="w-5 h-5 border-2 border-t-transparent animate-spin" style={{ borderColor: '#1d4ed8', borderTopColor: 'transparent' }} />
                    </div>
                  ) : (
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                      <div>
                        <div className="flex items-center gap-2 mb-3">
                          <User size={13} style={{ color: '#93c5fd' }} />
                          <p className="text-xs font-bold uppercase tracking-wider" style={{ color: '#a0c0e8' }}>Doctors ({doctors.length})</p>
                        </div>
                        {doctors.length === 0 ? (
                          <p className="text-xs italic pl-1" style={{ color: '#a0c0e8' }}>No doctors assigned.</p>
                        ) : doctors.map(d => (
                          <div key={d.id} className="flex items-center gap-3 px-3 py-2.5 mb-2" style={{ backgroundColor: '#0f1c2e' }}>
                            <div className="w-8 h-8 flex items-center justify-center font-bold text-xs shrink-0" style={{ backgroundColor: '#1a2d52', color: '#bfdbfe' }}>
                              {d.firstName[0]}
                            </div>
                            <div className="flex-1 min-w-0">
                              <p className="text-xs font-semibold text-white truncate">Dr. {d.firstName} {d.lastName}</p>
                              <p className="text-[11px] truncate" style={{ color: '#a0c0e8' }}>{d.designation}</p>
                            </div>
                            <span className="text-[10px] font-semibold px-2 py-0.5 shrink-0"
                              style={d.active ? { backgroundColor: '#1e4a7a', color: '#93c5fd' } : { backgroundColor: '#1a1a1a', color: '#6b7280' }}>
                              {d.active ? 'Active' : 'Off'}
                            </span>
                          </div>
                        ))}
                      </div>
                      <div>
                        <div className="flex items-center gap-2 mb-3">
                          <Stethoscope size={13} style={{ color: '#93c5fd' }} />
                          <p className="text-xs font-bold uppercase tracking-wider" style={{ color: '#a0c0e8' }}>Treatments ({treatments.length})</p>
                        </div>
                        {treatments.length === 0 ? (
                          <p className="text-xs italic pl-1" style={{ color: '#a0c0e8' }}>No treatments listed.</p>
                        ) : treatments.map(t => (
                          <div key={t.id} className="flex items-center justify-between px-3 py-2.5 mb-2" style={{ backgroundColor: '#0f1c2e' }}>
                            <p className="text-xs font-semibold text-white truncate mr-3">{t.name}</p>
                            <span className="text-xs font-bold px-2.5 py-1 shrink-0" style={{ backgroundColor: '#1a2d52', color: '#93c5fd' }}>
                              {t.price.toFixed(0)} kr
                            </span>
                          </div>
                        ))}
                      </div>
                    </div>
                  )}
                </div>
              )}
            </div>
          ))}
        </div>
      )}
    </div>
  )
}



