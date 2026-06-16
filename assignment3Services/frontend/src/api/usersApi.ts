import axios from 'axios'

const api = axios.create({ baseURL: 'http://localhost:8081' })

api.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) config.headers.Authorization = `Bearer ${token}`
  return config
})

export interface UserResponse {
  id: number
  username: string
  email: string
  userRole: string
}

export interface LoginResponse {
  token: string
}

export const login = (username: string, password: string) =>
  api.post<LoginResponse>('/api/users/login', { username, password })

export const register = (username: string, email: string, password: string) =>
  api.post('/api/users', { username, email, password })

export const getAll = (sortBy?: string, direction = 'asc') =>
  api.get<UserResponse[]>('/api/users/findAll', { params: sortBy ? { sortBy, direction } : {} })

export const getByRole = (role: string, sortBy?: string, direction = 'asc') =>
  api.get<UserResponse[]>('/api/users/findByRole', { params: sortBy ? { role, sortBy, direction } : { role } })

export const updateRole = (id: number, userRole: string) =>
  api.put('/api/users/updateUserRole', null, { params: { id, userRole } })

export const updatePassword = (id: number, password: string) =>
  api.put('/api/users/updatePassword', null, { params: { id, password } })

export const deleteUser = (id: number) =>
  api.delete(`/api/users/delete/${id}`)

export const getByUsername = (username: string) =>
  api.get<UserResponse>(`/api/users/findByUsername/${username}`)
