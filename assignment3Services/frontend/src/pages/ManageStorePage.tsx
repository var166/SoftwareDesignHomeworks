import { useEffect, useState } from 'react'
import { getByAdminId, addProduct, removeProduct, updateStock, incrementStock, decrementStock, type ShopResponse } from '../api/shopsApi'
import Navbar from '../components/Navbar'
import { getUserInfo } from '../utils/auth'
import axios from 'axios'

const extractError = (e: unknown): string => {
  if (axios.isAxiosError(e)) {
    const d = e.response?.data
    return String(d?.message ?? d ?? `HTTP ${e.response?.status}`)
  }
  return 'Unexpected error'
}

export default function ManageStorePage() {
  const [shop, setShop] = useState<ShopResponse | null>(null)
  const [noShop, setNoShop] = useState(false)
  const [newProductId, setNewProductId] = useState('')
  const [newStock, setNewStock] = useState('')
  const [stockEdits, setStockEdits] = useState<Record<string, string>>({})
  const user = getUserInfo()

  const load = async () => {
    if (!user) return
    try {
      const res = await getByAdminId(user.id)
      if (res.data.length === 0) { setNoShop(true); return }
      setShop(res.data[0])
    } catch (e) { alert(extractError(e)) }
  }

  useEffect(() => { load() }, [])

  const handleAdd = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!shop) return
    if (isNaN(Number(newProductId)) || newProductId === '') { alert('Product ID must be a number'); return }
    if (isNaN(Number(newStock)) || newStock === '') { alert('Initial stock must be a number'); return }
    try {
      await addProduct(shop.id, +newProductId, +newStock)
      setNewProductId(''); setNewStock('')
      load()
    } catch (e) { alert(extractError(e)) }
  }

  const handleUpdateStock = async (productId: string) => {
    if (!shop) return
    const val = stockEdits[productId]
    if (isNaN(Number(val)) || val === undefined) { alert('Stock must be a number'); return }
    try { await updateStock(shop.id, +productId, +val); load() }
    catch (e) { alert(extractError(e)) }
  }

  const handleIncrement = async (productId: string) => {
    if (!shop) return
    try { await incrementStock(shop.id, +productId, 1); load() }
    catch (e) { alert(extractError(e)) }
  }

  const handleDecrement = async (productId: string) => {
    if (!shop) return
    try { await decrementStock(shop.id, +productId, 1); load() }
    catch (e) { alert(extractError(e)) }
  }

  const handleRemove = async (productId: string) => {
    if (!shop) return
    try { await removeProduct(shop.id, +productId); load() }
    catch (e) { alert(extractError(e)) }
  }

  if (noShop) return (
    <div style={page}>
      <Navbar />
      <div style={content}>
        <p style={{ color: '#a6adc8' }}>You don't manage any shop yet. Ask an admin to assign one.</p>
      </div>
    </div>
  )

  if (!shop) return (
    <div style={page}>
      <Navbar />
      <div style={content}><p style={{ color: '#a6adc8' }}>Loading...</p></div>
    </div>
  )

  return (
    <div style={page}>
      <Navbar />
      <div style={content}>
        <h2 style={{ marginBottom: '0.25rem' }}>{shop.name}</h2>
        <p style={{ color: '#a6adc8', marginBottom: '2rem' }}>{shop.address} · {shop.email} · {shop.phone}</p>

        <h3 style={{ marginBottom: '1rem' }}>Stock Management</h3>
        <form onSubmit={handleAdd} style={formRow}>
          <input style={input} placeholder="Product ID" value={newProductId} onChange={e => setNewProductId(e.target.value)} />
          <input style={input} placeholder="Initial stock" value={newStock} onChange={e => setNewStock(e.target.value)} />
          <button style={btn} type="submit">+ Add Product</button>
        </form>

        <table style={table}>
          <thead>
            <tr>{['Product ID', 'Stock', 'Update Stock', 'Actions'].map(h => <th key={h} style={th}>{h}</th>)}</tr>
          </thead>
          <tbody>
            {Object.entries(shop.productStock || {}).map(([pid, qty]) => (
              <tr key={pid}>
                <td style={td}>{pid}</td>
                <td style={td}>{qty}</td>
                <td style={td}>
                  <input style={{ ...input, width: 80 }} placeholder="qty"
                    value={stockEdits[pid] ?? ''}
                    onChange={e => setStockEdits(s => ({ ...s, [pid]: e.target.value }))} />
                  <button style={smallBtn} onClick={() => handleUpdateStock(pid)}>Set</button>
                  <button style={smallBtn} onClick={() => handleIncrement(pid)}>+1</button>
                  <button style={smallBtn} onClick={() => handleDecrement(pid)}>-1</button>
                </td>
                <td style={td}>
                  <button style={dangerBtn} onClick={() => handleRemove(pid)}>Remove</button>
                </td>
              </tr>
            ))}
            {Object.keys(shop.productStock || {}).length === 0 && (
              <tr><td colSpan={4} style={{ ...td, color: '#a6adc8', textAlign: 'center' }}>No products yet.</td></tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  )
}

const page: React.CSSProperties = { minHeight: '100vh', background: '#1e1e2e', color: '#cdd6f4' }
const content: React.CSSProperties = { padding: '2rem' }
const formRow: React.CSSProperties = { display: 'flex', gap: '0.75rem', marginBottom: '1rem', alignItems: 'center' }
const input: React.CSSProperties = { padding: '0.5rem', borderRadius: 6, background: '#313244', color: '#cdd6f4', border: '1px solid #45475a' }
const btn: React.CSSProperties = { padding: '0.5rem 1rem', borderRadius: 6, background: '#89b4fa', border: 'none', cursor: 'pointer', fontWeight: 600 }
const smallBtn: React.CSSProperties = { ...btn, padding: '0.3rem 0.6rem', marginLeft: 4, fontSize: 12 }
const dangerBtn: React.CSSProperties = { ...btn, background: '#f38ba8' }
const table: React.CSSProperties = { width: '100%', borderCollapse: 'collapse' }
const th: React.CSSProperties = { textAlign: 'left', padding: '0.6rem 1rem', background: '#313244', borderBottom: '2px solid #45475a' }
const td: React.CSSProperties = { padding: '0.6rem 1rem', borderBottom: '1px solid #313244' }
