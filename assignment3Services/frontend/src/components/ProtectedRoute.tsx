import { Navigate } from 'react-router-dom'
import type { ReactNode } from 'react'
import { getToken, getUserInfo } from '../utils/auth'

interface Props {
  children: ReactNode
  roles?: string[]
}

export default function ProtectedRoute({ children, roles }: Props) {
  const token = getToken()
  if (!token) return <Navigate to="/login" replace />

  if (roles) {
    const info = getUserInfo()
    if (!info || !roles.includes(info.userRole)) {
      return <Navigate to="/" replace />
    }
  }

  return <>{children}</>
}
