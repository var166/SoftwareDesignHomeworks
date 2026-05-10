import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { getAll, create, deleteShop, type ShopResponse } from '../api/shopsApi'
import Navbar from '../components/Navbar'
import axios from 'axios'

const extractError = (e: unknown): string => {
  if (axios.isAxiosError(e)) {
    const d = e.response?.data
    return String(d?.message ?? d ?? `HTTP ${e.response?.status}`)
  }
  return 'Unexpected error'
}

export default function ShopsPage() {
  const [shops, setShops] = useState<ShopResponse[]>([])
  const [sortBy, setSortBy] = useState('')
  const [direction, setDirection] = useState<'asc' | 'desc'>('asc')
  const [form, setForm] = useState({ name: '', address: '', phone: '', email: '', description: '', adminId: '' })
  const navigate = useNavigate()

  const load = async () => {
    try {
      const res = await getAll(sortBy || undefined, direction)
      setShops(res.data)
    } catch (e) { console.error(e) }
  }

  useEffect(() => { load() }, [sortBy, direction])

  const handleCreate = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!form.name.trim()) { alert('Name is required'); return }
    if (isNaN(Number(form.adminId)) || form.adminId === '') { alert('Admin ID must be a number'); return }
    try {
      await create(form.name, form.address, form.phone, form.email, form.description, +form.adminId)
      setForm({ name: '', address: '', phone: '', email: '', description: '', adminId: '' })
      load()
    } catch (e) { alert(extractError(e)) }
  }

  const handleDelete = async (id: number, e: React.MouseEvent) => {
    e.stopPropagation()
    try { await deleteShop(id); load() }
    catch (e) { alert(extractError(e)) }
  }

  return (
    <div style={page}>
      <Navbar />
      <div style={content}>
        <h2 style={title}>Shops</h2>

        <form onSubmit={handleCreate} style={formRow}>
          {(['name', 'address', 'phone', 'email', 'description', 'adminId'] as const).map(f => (
            <input key={f} style={input} placeholder={f} value={form[f]}
              onChange={e => setForm(p => ({ ...p, [f]: e.target.value }))} />
          ))}
          <button style={btn} type="submit">+ Create</button>
        </form>

        <div style={filterBar}>
          <select style={select} value={sortBy} onChange={e => setSortBy(e.target.value)}>
            <option value="">No sort</option>
            <option value="name">Name</option>
          </select>
          <button style={btn} onClick={() => setDirection(d => d === 'asc' ? 'desc' : 'asc')}>
            {direction === 'asc' ? '↑ ASC' : '↓ DESC'}
          </button>
        </div>

        <table style={table}>
          <thead>
            <tr>{['ID', 'Name', 'Address', 'Admin ID', 'Products', 'Actions'].map(h => <th key={h} style={th}>{h}</th>)}</tr>
          </thead>
          <tbody>
            {shops.map(s => (
              <tr key={s.id} style={{ cursor: 'pointer' }} onClick={() => navigate(`/shops/${s.id}`)}>
                <td style={td}>{s.id}</td>
                <td style={td}>{s.name}</td>
                <td style={td}>{s.address}</td>
                <td style={td}>{s.adminId}</td>
                <td style={td}>{Object.keys(s.productStock || {}).length} products</td>
                <td style={td}>
                  <button style={dangerBtn} onClick={e => handleDelete(s.id, e)}>Delete</button>
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
const filterBar: React.CSSProperties = { display: 'flex', gap: '0.75rem', marginBottom: '1rem', alignItems: 'center' }
const formRow: React.CSSProperties = { display: 'flex', gap: '0.75rem', marginBottom: '1rem', flexWrap: 'wrap', alignItems: 'center' }
const input: React.CSSProperties = { padding: '0.5rem', borderRadius: 6, background: '#313244', color: '#cdd6f4', border: '1px solid #45475a' }
const select: React.CSSProperties = { ...input }
const btn: React.CSSProperties = { padding: '0.5rem 1rem', borderRadius: 6, background: '#89b4fa', border: 'none', cursor: 'pointer', fontWeight: 600 }
const dangerBtn: React.CSSProperties = { ...btn, background: '#f38ba8' }
const table: React.CSSProperties = { width: '100%', borderCollapse: 'collapse' }
const th: React.CSSProperties = { textAlign: 'left', padding: '0.6rem 1rem', background: '#313244', borderBottom: '2px solid #45475a' }
const td: React.CSSProperties = { padding: '0.6rem 1rem', borderBottom: '1px solid #313244' }
