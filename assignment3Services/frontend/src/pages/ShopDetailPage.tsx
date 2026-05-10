import { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import { getById, addProduct, removeProduct, updateStock, incrementStock, decrementStock, type ShopResponse } from '../api/shopsApi'
import Navbar from '../components/Navbar'

export default function ShopDetailPage() {
  const { id } = useParams<{ id: string }>()
  const [shop, setShop] = useState<ShopResponse | null>(null)
  const [newProductId, setNewProductId] = useState('')
  const [newStock, setNewStock] = useState('')
  const [stockEdits, setStockEdits] = useState<Record<string, string>>({})

  const load = async () => {
    try {
      const res = await getById(+id!)
      setShop(res.data)
    } catch (e) { console.error(e) }
  }

  useEffect(() => { load() }, [id])

  const handleAdd = async (e: React.FormEvent) => {
    e.preventDefault()
    await addProduct(+id!, +newProductId, +newStock)
    setNewProductId('')
    setNewStock('')
    load()
  }

  const handleRemove = async (productId: string) => {
    await removeProduct(+id!, +productId)
    load()
  }

  const handleUpdateStock = async (productId: string) => {
    await updateStock(+id!, +productId, +stockEdits[productId])
    load()
  }

  const handleIncrement = async (productId: string) => {
    await incrementStock(+id!, +productId, 1)
    load()
  }

  const handleDecrement = async (productId: string) => {
    await decrementStock(+id!, +productId, 1)
    load()
  }

  if (!shop) return <div style={{ color: '#cdd6f4', padding: '2rem' }}>Loading...</div>

  return (
    <div style={page}>
      <Navbar />
      <div style={content}>
        <h2 style={title}>{shop.name}</h2>
        <p style={{ color: '#a6adc8' }}>{shop.address} · {shop.email} · {shop.phone}</p>

        <h3 style={{ marginTop: '2rem', marginBottom: '1rem' }}>Stock</h3>
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
          </tbody>
        </table>
      </div>
    </div>
  )
}

const page: React.CSSProperties = { minHeight: '100vh', background: '#1e1e2e', color: '#cdd6f4' }
const content: React.CSSProperties = { padding: '2rem' }
const title: React.CSSProperties = { marginBottom: '0.25rem' }
const formRow: React.CSSProperties = { display: 'flex', gap: '0.75rem', marginBottom: '1rem', alignItems: 'center' }
const input: React.CSSProperties = { padding: '0.5rem', borderRadius: 6, background: '#313244', color: '#cdd6f4', border: '1px solid #45475a' }
const btn: React.CSSProperties = { padding: '0.5rem 1rem', borderRadius: 6, background: '#89b4fa', border: 'none', cursor: 'pointer', fontWeight: 600 }
const smallBtn: React.CSSProperties = { ...btn, padding: '0.3rem 0.6rem', marginLeft: 4, fontSize: 12 }
const dangerBtn: React.CSSProperties = { ...btn, background: '#f38ba8' }
const table: React.CSSProperties = { width: '100%', borderCollapse: 'collapse' }
const th: React.CSSProperties = { textAlign: 'left', padding: '0.6rem 1rem', background: '#313244', borderBottom: '2px solid #45475a' }
const td: React.CSSProperties = { padding: '0.6rem 1rem', borderBottom: '1px solid #313244' }
