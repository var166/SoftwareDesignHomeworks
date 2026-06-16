import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { login, register, getByUsername } from '../api/usersApi'
import { setSession } from '../utils/auth'
import axios from 'axios'

export default function LoginPage() {
  const [mode, setMode] = useState<'login' | 'register'>('login')
  const [username, setUsername] = useState('')
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const navigate = useNavigate()

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setError('')
    try {
      if (mode === 'login') {
        const res = await login(username, password)
        localStorage.setItem('token', res.data.token)
        const userRes = await getByUsername(username)
        setSession(res.data.token, { id: userRes.data.id, username: userRes.data.username, email: userRes.data.email, userRole: userRes.data.userRole })
        if (userRes.data.userRole === 'ADMIN') navigate('/users')
        else if (userRes.data.userRole === 'STORE_MANAGER') navigate('/manage-store')
        else navigate('/my-orders')
      } else {
        await register(username, email, password)
        alert('Registered successfully! Please log in.')
        setMode('login')
        setPassword('')
      }
    } catch (e) {
      const msg = axios.isAxiosError(e)
        ? (e.response?.data?.message ?? e.response?.data ?? `Error ${e.response?.status}`)
        : 'Unexpected error'
      setError(String(msg))
    }
  }

  return (
    <div style={container}>
      <form onSubmit={handleSubmit} style={card}>
        <h2 style={{ marginBottom: '1.5rem', color: '#cdd6f4' }}>{mode === 'login' ? 'Login' : 'Register'}</h2>
        <input style={input} placeholder="Username" value={username} onChange={e => setUsername(e.target.value)} required />
        {mode === 'register' && (
          <input style={input} placeholder="Email" value={email} onChange={e => setEmail(e.target.value)} required />
        )}
        <input style={input} placeholder="Password" type="password" value={password} onChange={e => setPassword(e.target.value)} required />
        {error && <p style={{ color: '#f38ba8', fontSize: 13 }}>{error}</p>}
        <button style={btn} type="submit">{mode === 'login' ? 'Login' : 'Register'}</button>
        <button type="button" style={linkBtn} onClick={() => { setMode(m => m === 'login' ? 'register' : 'login'); setError('') }}>
          {mode === 'login' ? 'No account? Register' : 'Have account? Login'}
        </button>
      </form>
    </div>
  )
}

const container: React.CSSProperties = { display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '100vh', background: '#1e1e2e' }
const card: React.CSSProperties = { background: '#313244', padding: '2rem', borderRadius: 12, display: 'flex', flexDirection: 'column', gap: '1rem', minWidth: 320 }
const input: React.CSSProperties = { padding: '0.6rem', borderRadius: 6, border: '1px solid #45475a', background: '#1e1e2e', color: '#cdd6f4', fontSize: 15 }
const btn: React.CSSProperties = { padding: '0.6rem', borderRadius: 6, background: '#89b4fa', border: 'none', fontWeight: 700, cursor: 'pointer', fontSize: 15 }
const linkBtn: React.CSSProperties = { background: 'none', border: 'none', color: '#a6adc8', cursor: 'pointer', fontSize: 13, textDecoration: 'underline' }
