import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuthStore } from '@/stores/authStore'
import api from '@/lib/axios'
import { Activity, FlaskConical } from 'lucide-react'

export default function LoginPage() {
  const navigate  = useNavigate()
  const login     = useAuthStore(s => s.login)
  const [email,    setEmail]    = useState('')
  const [password, setPassword] = useState('')
  const [error,    setError]    = useState('')
  const [loading,  setLoading]  = useState(false)

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setError(''); setLoading(true)
    try {
      const res = await api.post('/v1/auth/login', { email, password })
      login(res.data.token, res.data.role, email)
      navigate('/dashboard')
    } catch { setError('Sorry, your email or password was incorrect.') }
    finally   { setLoading(false) }
  }

  const handleDemoLogin = () => {
    login('demo-token', 'ADMIN', 'demo@medicare.se')
    navigate('/dashboard')
  }

  return (
    /* Wrapper — full viewport */
    <div style={{ position: 'relative', width: '100vw', height: '100vh', overflow: 'hidden', display: 'flex', alignItems: 'center', justifyContent: 'flex-end', padding: '0 5vw' }}>

      {/* ── Full-bleed background image ── */}
      <img
        src="/hero-bg.jpg"
        alt=""
        style={{
          position: 'absolute', inset: 0,
          width: '100%', height: '100%',
          objectFit: 'cover', objectPosition: 'center',
          zIndex: 0,
        }}
      />

      {/* ── Dark overlay ── */}
      <div style={{ position: 'absolute', inset: 0, backgroundColor: 'rgba(4, 10, 25, 0.60)', zIndex: 1 }} />

      {/* ── Left branding (desktop) ── */}
      <div style={{ position: 'absolute', left: '6vw', top: '50%', transform: 'translateY(-50%)', zIndex: 2, maxWidth: 380 }}
        className="hidden lg:block">
        <div className="flex items-center gap-3 mb-8">
          <div className="w-10 h-10 flex items-center justify-center" style={{ backgroundColor: '#1d4ed8' }}>
            <Activity size={22} className="text-[#bfdbfe]" />
          </div>
          <span className="text-white font-bold text-xl tracking-tight">MediCare</span>
        </div>
        <h1 className="text-4xl font-black text-white leading-tight">
          Manage your<br />patients{' '}
          <span style={{ color: '#bfdbfe' }}>smarter.</span>
        </h1>
        <p className="mt-4 text-sm" style={{ color: 'rgba(255,255,255,0.45)' }}>
          A complete hospital management system for bookings,<br />billing, departments and more.
        </p>
      </div>

      {/* ── Floating login card ── */}
      <div
        className="relative z-10 w-full"
        style={{
          maxWidth: 360,
          backgroundColor: 'transparent',
          border: 'none',
        }}
      >
        {/* Header */}
        <div className="px-8 pt-8 pb-5">
          {/* Mobile logo */}
          <div className="flex items-center gap-2 mb-5 lg:hidden">
            <div className="w-8 h-8 flex items-center justify-center" style={{ backgroundColor: '#1d4ed8' }}>
              <Activity size={16} className="text-[#bfdbfe]" />
            </div>
            <span className="text-white font-bold">MediCare</span>
          </div>
          <h2 className="text-xl font-bold text-white">Log into MediCare</h2>
          <p className="text-xs mt-1" style={{ color: '#b8cef5' }}>Enter your credentials to continue</p>
        </div>

        {/* Form */}
        <div className="px-8 py-6 space-y-3">
          {error && (
            <div className="px-4 py-3 text-xs" style={{ backgroundColor: 'rgba(220,38,38,0.10)', border: '1px solid rgba(220,38,38,0.25)', color: '#f87171' }}>
              {error}
            </div>
          )}

          {[
            { type: 'email',    value: email,    setter: setEmail,    placeholder: 'Email address' },
            { type: 'password', value: password, setter: setPassword, placeholder: 'Password' },
          ].map(({ type, value, setter, placeholder }) => (
            <input
              key={type}
              type={type}
              value={value}
              onChange={e => setter(e.target.value)}
              required
              placeholder={placeholder}
              className="w-full px-4 py-3 text-sm text-white focus:outline-none transition-colors placeholder-[#a0c0e8]"
              style={{ backgroundColor: 'rgba(0,0,0,0.35)', border: '1px solid rgba(96,165,250,0.30)' }}
              onFocus={e  => (e.target.style.borderColor  = 'rgba(96,165,250,0.80)')}
              onBlur={e   => (e.target.style.borderColor  = 'rgba(96,165,250,0.30)')}
            />
          ))}

          <button
            onClick={handleSubmit as unknown as React.MouseEventHandler}
            disabled={loading || !email || !password}
            className="w-full py-3 text-sm font-bold text-white mt-1 transition-all disabled:opacity-40 disabled:cursor-not-allowed"
            style={{ backgroundColor: '#1d4ed8' }}
            onMouseEnter={e => { if (email && password) (e.currentTarget.style.backgroundColor = '#1e3a8a') }}
            onMouseLeave={e => (e.currentTarget.style.backgroundColor = '#1d4ed8')}
          >
            {loading ? 'Logging in…' : 'Log in'}
          </button>

          <div className="text-center pt-1">
            <a href="#" onClick={e => e.preventDefault()} className="text-xs" style={{ color: '#93c5fd' }}>
              Forgot password?
            </a>
          </div>
        </div>

        {/* OR divider */}
        <div className="flex items-center gap-4 px-8">
          <div className="flex-1 h-px" style={{ backgroundColor: 'rgba(96,165,250,0.07)' }} />
          <span className="text-[10px] font-bold tracking-widest" style={{ color: '#4a6fa5' }}>OR</span>
          <div className="flex-1 h-px" style={{ backgroundColor: 'rgba(96,165,250,0.07)' }} />
        </div>

        {/* Secondary actions */}
        <div className="px-8 pt-4 pb-8 space-y-3">
          <button onClick={handleDemoLogin}
            className="w-full py-3 text-sm font-semibold flex items-center justify-center gap-2 transition-colors"
            style={{ border: '1px solid rgba(96,165,250,0.18)', color: '#93c5fd', backgroundColor: 'transparent' }}
            onMouseEnter={e => (e.currentTarget.style.backgroundColor = 'rgba(96,165,250,0.05)')}
            onMouseLeave={e => (e.currentTarget.style.backgroundColor = 'transparent')}
          >
            <FlaskConical size={14} /> Preview without backend (Demo)
          </button>

          <button
            className="w-full py-3 text-sm font-semibold transition-colors"
            style={{ border: '1px solid rgba(255,255,255,0.07)', color: 'rgba(255,255,255,0.30)', backgroundColor: 'transparent' }}
            onMouseEnter={e => (e.currentTarget.style.backgroundColor = 'rgba(255,255,255,0.03)')}
            onMouseLeave={e => (e.currentTarget.style.backgroundColor = 'transparent')}
          >
            Create new account
          </button>
        </div>

        {/* Footer */}
        <div className="px-8 pb-5 text-center">
          <div className="flex justify-center gap-4 pt-4 mb-1">
            {['About', 'Help', 'Privacy', 'Terms'].map(item => (
              <a key={item} href="#" onClick={e => e.preventDefault()}
                className="text-[10px] hover:underline" style={{ color: '#4a6fa5' }}>
                {item}
              </a>
            ))}
          </div>
          <p className="text-[10px]" style={{ color: '#4a6fa5' }}>© 2026 MEDICARE</p>
        </div>
      </div>
    </div>
  )
}


