import { useEffect, useState } from 'react'
import { getAll, getByPriceRange, type ProductResponse } from '../api/productsApi'
import Navbar from '../components/Navbar'
import { getUserInfo } from '../utils/auth'
import {
  CreateProductCommand,
  UpdatePriceCommand,
  UpdateDescriptionCommand,
  DeleteProductCommand
} from '../commands/ProductCommands'
import axios from 'axios'

const extractError = (e: unknown): string => {
  if (axios.isAxiosError(e)) {
    const d = e.response?.data
    return String(d?.message ?? d ?? `HTTP ${e.response?.status}`)
  }
  return 'Unexpected error'
}

const exportProducts = (
  format: 'json' | 'xml' | 'csv',
  sortBy: string,
  direction: string,
  minPrice: string,
  maxPrice: string
) => {
  const params = new URLSearchParams({ format })
  if (sortBy) { params.set('sortBy', sortBy); params.set('direction', direction) }
  if (minPrice && maxPrice) { params.set('min', minPrice); params.set('max', maxPrice) }
  const a = document.createElement('a')
  a.href = `http://localhost:8082/api/products/export?${params.toString()}`
  a.download = `products.${format}`
  a.click()
}

export default function ProductsPage() {
  const [products, setProducts] = useState<ProductResponse[]>([])
  const [sortBy, setSortBy] = useState('')
  const [direction, setDirection] = useState<'asc' | 'desc'>('asc')
  const [minPrice, setMinPrice] = useState('')
  const [maxPrice, setMaxPrice] = useState('')
  const [form, setForm] = useState({ name: '', description: '', price: '', shopId: '' })
  const [editPrice, setEditPrice] = useState<Record<number, string>>({})
  const [editDesc, setEditDesc] = useState<Record<number, string>>({})
  const [error, setError] = useState('')
  const [userEmail, setUserEmail] = useState<string>(
    getUserInfo()?.email || localStorage.getItem('notifEmail') || ''
  )

  useEffect(() => {
    if (userEmail) localStorage.setItem('notifEmail', userEmail)
  }, [userEmail])

  const load = async () => {
    try {
      if (minPrice || maxPrice) {
        if (isNaN(Number(minPrice)) || isNaN(Number(maxPrice))) { alert('Min/Max price must be numbers'); return }
      }
      const res = minPrice && maxPrice
        ? await getByPriceRange(+minPrice, +maxPrice, sortBy || undefined, direction)
        : await getAll(sortBy || undefined, direction)
      setProducts(res.data)
      setError('')
    } catch (e) { setError(extractError(e)) }
  }

  useEffect(() => { load() }, [sortBy, direction])

  const handleCreate = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!form.name.trim()) { alert('Name is required'); return }
    if (isNaN(Number(form.price)) || form.price === '') { alert('Price must be a valid number'); return }
    if (isNaN(Number(form.shopId)) || form.shopId === '') { alert('Shop ID must be a valid number'); return }
    try {
      await new CreateProductCommand(form.name, form.description, +form.price, +form.shopId, userEmail || undefined, load).execute()
      setForm({ name: '', description: '', price: '', shopId: '' })
    } catch (e) { alert(extractError(e)) }
  }

  const handleUpdatePrice = async (id: number) => {
    const val = editPrice[id]
    if (isNaN(Number(val)) || val === undefined || val === '') { alert('Price must be a valid number'); return }
    try { await new UpdatePriceCommand(id, +val, userEmail || undefined, load).execute() }
    catch (e) { alert(extractError(e)) }
  }

  const handleUpdateDesc = async (id: number) => {
    try { await new UpdateDescriptionCommand(id, editDesc[id], userEmail || undefined, load).execute() }
    catch (e) { alert(extractError(e)) }
  }

  const handleDelete = async (id: number) => {
    try { await new DeleteProductCommand(id, load).execute() }
    catch (e) { alert(extractError(e)) }
  }

  return (
    <div style={page}>
      <Navbar />
      <div style={content}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1rem' }}>
          <h2>Products</h2>
          <div style={{ display: 'flex', gap: '0.5rem' }}>
            <span style={{ color: '#a6adc8', fontSize: 13, alignSelf: 'center' }}>Export:</span>
            {(['json', 'csv', 'xml'] as const).map(fmt => (
              <button key={fmt} style={exportBtn} onClick={() => exportProducts(fmt, sortBy, direction, minPrice, maxPrice)}>{fmt.toUpperCase()}</button>
            ))}
          </div>
        </div>

        <div style={{ display: 'flex', alignItems: 'center', gap: '0.75rem', marginBottom: '1rem' }}>
          <label style={{ color: '#a6adc8', fontSize: 13 }}>Notification email:</label>
          <input style={{ ...input, width: 260 }} placeholder="your@email.com" value={userEmail}
            onChange={e => setUserEmail(e.target.value)} />
        </div>

        {error && <div style={errorBox}>{error}</div>}

        <form onSubmit={handleCreate} style={formRow}>
          <input style={input} placeholder="Name" value={form.name}
            onChange={e => setForm(p => ({ ...p, name: e.target.value }))} />
          <input style={input} placeholder="Description" value={form.description}
            onChange={e => setForm(p => ({ ...p, description: e.target.value }))} />
          <input style={input} placeholder="Price" value={form.price}
            onChange={e => setForm(p => ({ ...p, price: e.target.value }))} />
          <input style={input} placeholder="Shop ID" value={form.shopId}
            onChange={e => setForm(p => ({ ...p, shopId: e.target.value }))} />
          <button style={btn} type="submit">+ Create</button>
        </form>

        <div style={filterBar}>
          <input style={{ ...input, width: 100 }} placeholder="Min price" value={minPrice} onChange={e => setMinPrice(e.target.value)} />
          <input style={{ ...input, width: 100 }} placeholder="Max price" value={maxPrice} onChange={e => setMaxPrice(e.target.value)} />
          <button style={btn} onClick={load}>Filter</button>
          <select style={select} value={sortBy} onChange={e => setSortBy(e.target.value)}>
            <option value="">No sort</option>
            <option value="name">Name</option>
            <option value="price">Price</option>
          </select>
          <button style={btn} onClick={() => setDirection(d => d === 'asc' ? 'desc' : 'asc')}>
            {direction === 'asc' ? '↑ ASC' : '↓ DESC'}
          </button>
        </div>

        <table style={table}>
          <thead>
            <tr>{['ID', 'Name', 'Description', 'Price', 'ShopId', 'Actions'].map(h => <th key={h} style={th}>{h}</th>)}</tr>
          </thead>
          <tbody>
            {products.map(p => (
              <tr key={p.id}>
                <td style={td}>{p.id}</td>
                <td style={td}>{p.name}</td>
                <td style={td}>
                  <input style={{ ...input, width: 140 }} value={editDesc[p.id] ?? p.description}
                    onChange={e => setEditDesc(d => ({ ...d, [p.id]: e.target.value }))} />
                  <button style={smallBtn} onClick={() => handleUpdateDesc(p.id)}>Save</button>
                </td>
                <td style={td}>
                  <input style={{ ...input, width: 80 }} value={editPrice[p.id] ?? p.price}
                    onChange={e => setEditPrice(d => ({ ...d, [p.id]: e.target.value }))} />
                  <button style={smallBtn} onClick={() => handleUpdatePrice(p.id)}>Save</button>
                </td>
                <td style={td}>{p.shopId}</td>
                <td style={td}>
                  <button style={dangerBtn} onClick={() => handleDelete(p.id)}>Delete</button>
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
const errorBox: React.CSSProperties = { background: '#f38ba822', color: '#f38ba8', padding: '0.75rem', borderRadius: 6, marginBottom: '1rem' }
const filterBar: React.CSSProperties = { display: 'flex', gap: '0.75rem', marginBottom: '1rem', alignItems: 'center', flexWrap: 'wrap' }
const formRow: React.CSSProperties = { display: 'flex', gap: '0.75rem', marginBottom: '1rem', flexWrap: 'wrap', alignItems: 'center' }
const input: React.CSSProperties = { padding: '0.5rem', borderRadius: 6, background: '#313244', color: '#cdd6f4', border: '1px solid #45475a' }
const select: React.CSSProperties = { ...input }
const btn: React.CSSProperties = { padding: '0.5rem 1rem', borderRadius: 6, background: '#89b4fa', border: 'none', cursor: 'pointer', fontWeight: 600 }
const smallBtn: React.CSSProperties = { ...btn, padding: '0.3rem 0.6rem', marginLeft: 4, fontSize: 12 }
const dangerBtn: React.CSSProperties = { ...btn, background: '#f38ba8' }
const exportBtn: React.CSSProperties = { ...btn, background: '#a6e3a1', padding: '0.3rem 0.7rem', fontSize: 12 }
const table: React.CSSProperties = { width: '100%', borderCollapse: 'collapse' }
const th: React.CSSProperties = { textAlign: 'left', padding: '0.6rem 1rem', background: '#313244', borderBottom: '2px solid #45475a' }
const td: React.CSSProperties = { padding: '0.6rem 1rem', borderBottom: '1px solid #313244' }
