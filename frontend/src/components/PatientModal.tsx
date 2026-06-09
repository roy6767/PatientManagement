import { useState } from 'react'
import { usePatientStore } from '@/stores/patientStore'
import { X, User } from 'lucide-react'

interface Props { onClose: () => void }

export default function PatientModal({ onClose }: Props) {
  const { createPatient } = usePatientStore()
  const [form, setForm] = useState({ name: '', email: '', birthDate: '', gender: 'MALE' as 'MALE'|'FEMALE'|'OTHER', address: '', phoneNumber: '' })
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')

  const set = (k: keyof typeof form) => (e: React.ChangeEvent<HTMLInputElement|HTMLSelectElement>) => setForm({ ...form, [k]: e.target.value })

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault(); setLoading(true); setError('')
    try { await createPatient(form); onClose() }
    catch { setError('Failed to create patient. Please check the details.') }
    finally { setLoading(false) }
  }

  const inp = "w-full px-4 py-2.5 text-sm text-white focus:outline-none transition-colors"
  const inpStyle = { backgroundColor: '#0f1c2e', border: '1px solid #1e3a6a', color: '#e8f0fe' }

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center px-4" style={{ backgroundColor: 'rgba(0,0,0,0.55)' }}>
      <div className="w-full max-w-md overflow-hidden" style={{ backgroundColor: '#162040', border: '1px solid #1e3a6a' }}>
        <div className="flex items-center justify-between px-6 py-5" style={{ borderBottom: '1px solid #1e3a6a' }}>
          <div className="flex items-center gap-3">
            <div className="w-9 h-9 flex items-center justify-center" style={{ backgroundColor: '#1a2d52' }}>
              <User size={18} style={{ color: '#60a5fa' }} />
            </div>
            <div>
              <p className="text-sm font-semibold" style={{ color: '#e0eaff' }}>New Patient</p>
              <p className="text-xs" style={{ color: '#5a7fa8' }}>Fill in the patient details below</p>
            </div>
          </div>
          <button onClick={onClose} className="w-8 h-8 flex items-center justify-center transition-colors" style={{ color: '#5a7fa8' }}
            onMouseEnter={e => (e.currentTarget.style.color = '#bfdbfe')}
            onMouseLeave={e => (e.currentTarget.style.color = '#5a7fa8')}>
            <X size={18} />
          </button>
        </div>

        <form onSubmit={handleSubmit} className="px-6 py-5 space-y-4">
          {error && <div className="text-xs px-4 py-3" style={{ backgroundColor: '#2e0a0a', border: '1px solid #5a1a1a', color: '#f87171' }}>{error}</div>}

          <Field label="Full Name"><input type="text" value={form.name} onChange={set('name')} required placeholder="Jane Doe" className={inp} style={inpStyle} /></Field>
          <Field label="Email Address"><input type="email" value={form.email} onChange={set('email')} required placeholder="jane@example.com" className={inp} style={inpStyle} /></Field>

          <div className="grid grid-cols-2 gap-3">
            <Field label="Date of Birth"><input type="date" value={form.birthDate} onChange={set('birthDate')} required className={inp} style={inpStyle} /></Field>
            <Field label="Gender">
              <select value={form.gender} onChange={set('gender')} className={inp} style={{ ...inpStyle }}>
                <option value="MALE">Male</option>
                <option value="FEMALE">Female</option>
                <option value="OTHER">Other</option>
              </select>
            </Field>
          </div>

          <Field label="Phone Number"><input type="tel" value={form.phoneNumber} onChange={set('phoneNumber')} required placeholder="+46 70 000 0000" className={inp} style={inpStyle} /></Field>
          <Field label="Address"><input type="text" value={form.address} onChange={set('address')} required placeholder="123 Main Street, City" className={inp} style={inpStyle} /></Field>
        </form>

        <div className="flex gap-3 px-6 pb-6">
          <button type="button" onClick={onClose} className="flex-1 py-2.5 text-sm font-semibold transition-colors"
            style={{ border: '1px solid #1e3a6a', color: '#7b96c2' }}>Cancel</button>
          <button type="submit" disabled={loading} onClick={handleSubmit} className="flex-1 py-2.5 text-sm font-semibold text-white transition-colors disabled:opacity-50"
            style={{ backgroundColor: '#1d4ed8' }}>
            {loading ? 'Saving…' : 'Create Patient'}
          </button>
        </div>
      </div>
    </div>
  )
}

function Field({ label, children }: { label: string; children: React.ReactNode }) {
  return (
    <div>
      <label className="block text-xs font-bold uppercase tracking-wide mb-1.5" style={{ color: '#5a7fa8' }}>{label}</label>
      {children}
    </div>
  )
}


