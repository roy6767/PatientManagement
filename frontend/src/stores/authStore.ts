import { create } from 'zustand'
import { persist } from 'zustand/middleware'

interface AuthState {
  token: string | null
  role: string | null
  email: string | null
  login: (token: string, role: string, email: string) => void
  logout: () => void
  isAuthenticated: () => boolean
}

export const useAuthStore = create<AuthState>()(
  persist(
    (set, get) => ({
      token: null,
      role: null,
      email: null,
      login: (token, role, email) => set({ token, role, email }),
      logout: () => set({ token: null, role: null, email: null }),
      isAuthenticated: () => !!get().token,
    }),
    { name: 'auth-storage' },
  ),
)
