import axios, { AxiosError } from 'axios'

interface ApiError { message: string; code: string; timestamp: string }

const api = axios.create({ baseURL: '' })

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token')
  if (token) config.headers.Authorization = `Bearer ${token}`
  return config
})

api.interceptors.response.use(
  (response) => response,
  (error: AxiosError<ApiError>) => {
    if (window.location.pathname !== '/login') {
      const msg = error.response?.data?.message ?? defaultMessageFor(error.response?.status)
      window.alert(msg)
    }
    return Promise.reject(error)
  }
)

function defaultMessageFor(status?: number): string {
  if (status === undefined) return 'Network error. Check your connection.'
  if (status >= 500) return 'Server error. Try again later.'
  if (status === 404) return 'Resource not found.'
  if (status === 403) return 'You do not have permission to do that.'
  if (status === 401) return 'Please log in to continue.'
  if (status === 400) return 'Invalid request.'
  return 'An unexpected error occurred.'
}

export default api
