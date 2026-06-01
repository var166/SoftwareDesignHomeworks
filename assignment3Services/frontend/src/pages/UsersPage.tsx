import { useEffect, useState } from 'react'
import { getAll, getByRole, updateRole, deleteUser, type UserResponse } from '../api/usersApi'
import Navbar from '../components/Navbar'
import { useTranslation } from 'react-i18next'

const ROLES = ['ALL', 'USER', 'ADMIN', 'STORE_MANAGER']
const SORT_FIELDS = ['username', 'email']

export default function UsersPage() {
  const { t } = useTranslation()
  const [users, setUsers] = useState<UserResponse[]>([])
  const [role, setRole] = useState('ALL')
  const [sortBy, setSortBy] = useState('')
  const [direction, setDirection] = useState<'asc' | 'desc'>('asc')
  const [roleEdits, setRoleEdits] = useState<Record<number, string>>({})

  const load = async () => {
    try {
      const res = role === 'ALL'
        ? await getAll(sortBy || undefined, direction)
        : await getByRole(role, sortBy || undefined, direction)
      setUsers(res.data)
    } catch {}
  }

  useEffect(() => { load() }, [role, sortBy, direction])

  const handleDelete = async (id: number) => {
    try { await deleteUser(id); load() }
    catch {}
  }

  const handleRoleChange = async (id: number) => {
    const newRole = roleEdits[id]
    if (!newRole) return
    try { await updateRole(id, newRole); load() }
    catch {}
  }

  return (
    <div style={page}>
      <Navbar />
      <div style={content}>
        <h2 style={title}>{t('users.title')}</h2>
        <div style={filterBar}>
          <select style={select} value={role} onChange={e => setRole(e.target.value)}>
            {ROLES.map(r => <option key={r}>{r}</option>)}
          </select>
          <select style={select} value={sortBy} onChange={e => setSortBy(e.target.value)}>
            <option value="">{t('users.noSort')}</option>
            {SORT_FIELDS.map(f => <option key={f}>{f}</option>)}
          </select>
          <button style={btn} onClick={() => setDirection(d => d === 'asc' ? 'desc' : 'asc')}>
            {direction === 'asc' ? t('users.asc') : t('users.desc')}
          </button>
        </div>
        <table style={table}>
          <thead>
            <tr>{[t('users.colId'), t('users.colUsername'), t('users.colRole'), t('users.colChangeRole'), t('users.colActions')].map(h => <th key={h} style={th}>{h}</th>)}</tr>
          </thead>
          <tbody>
            {users.map(u => (
              <tr key={u.id}>
                <td style={td}>{u.id}</td>
                <td style={td}>{u.username}</td>
                <td style={td}>{u.userRole}</td>
                <td style={td}>
                  <select style={select} value={roleEdits[u.id] ?? u.userRole}
                    onChange={e => setRoleEdits(r => ({ ...r, [u.id]: e.target.value }))}>
                    {['USER', 'ADMIN', 'STORE_MANAGER'].map(r => <option key={r}>{r}</option>)}
                  </select>
                  <button style={{ ...smallBtn, marginLeft: 6 }} onClick={() => handleRoleChange(u.id)}>{t('users.apply')}</button>
                </td>
                <td style={td}>
                  <button style={dangerBtn} onClick={() => handleDelete(u.id)}>{t('users.delete')}</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  )
}

const page: React.CSSProperties = { minHeight: '100vh', background: '#1e1e2e', color: '#cdd6f4' }
const content: React.CSSProperties = { padding: '2rem' }
const title: React.CSSProperties = { marginBottom: '1rem' }
const filterBar: React.CSSProperties = { display: 'flex', gap: '1rem', marginBottom: '1rem', alignItems: 'center' }
const select: React.CSSProperties = { padding: '0.5rem', borderRadius: 6, background: '#313244', color: '#cdd6f4', border: '1px solid #45475a' }
const btn: React.CSSProperties = { padding: '0.5rem 1rem', borderRadius: 6, background: '#89b4fa', border: 'none', cursor: 'pointer', fontWeight: 600 }
const smallBtn: React.CSSProperties = { ...btn, padding: '0.3rem 0.6rem', fontSize: 12 }
const dangerBtn: React.CSSProperties = { ...smallBtn, background: '#f38ba8' }
const table: React.CSSProperties = { width: '100%', borderCollapse: 'collapse' }
const th: React.CSSProperties = { textAlign: 'left', padding: '0.6rem 1rem', background: '#313244', borderBottom: '2px solid #45475a' }
const td: React.CSSProperties = { padding: '0.6rem 1rem', borderBottom: '1px solid #313244' }
