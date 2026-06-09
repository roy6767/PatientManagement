import { Navigate } from 'react-router-dom'
import { useAuthStore } from '@/stores/authStore'
import Layout from './Layout'

export default function PrivateRoute({ children }: { children: React.ReactNode }) {
  const isAuthenticated = useAuthStore((s) => s.isAuthenticated())
  if (!isAuthenticated) return <Navigate to="/login" replace />
  return <Layout>{children}</Layout>
}
