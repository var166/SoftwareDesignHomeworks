export interface UserInfo {
  id: number
  username: string
  email: string
  userRole: string
}

export const getToken = () => localStorage.getItem('token')

export const getUserInfo = (): UserInfo | null => {
  const raw = localStorage.getItem('userInfo')
  return raw ? JSON.parse(raw) : null
}

export const setSession = (token: string, info: UserInfo) => {
  localStorage.setItem('token', token)
  localStorage.setItem('userInfo', JSON.stringify(info))
}

export const logout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('userInfo')
}

export const isAdmin = () => getUserInfo()?.userRole === 'ADMIN'
export const isStoreManager = () => getUserInfo()?.userRole === 'STORE_MANAGER'
export const isUser = () => getUserInfo()?.userRole === 'USER'
