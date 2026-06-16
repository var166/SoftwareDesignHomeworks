import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { createOrder, type OrderItemRequest } from '../api/ordersApi'
import Navbar from '../components/Navbar'

export default function CreateOrderPage() {
  const [userId, setUserId] = useState('')
  const [items, setItems] = useState<OrderItemRequest[]>([{ productId: 0, quantity: 1, pricePerUnit: 0 }])
  const navigate = useNavigate()

  const addRow = () => setItems(i => [...i, { productId: 0, quantity: 1, pricePerUnit: 0 }])
  const removeRow = (idx: number) => setItems(i => i.filter((_, j) => j !== idx))
  const updateRow = (idx: number, field: keyof OrderItemRequest, value: string) =>
    setItems(i => i.map((row, j) => j === idx ? { ...row, [field]: +value } : row))

  const total = items.reduce((sum, i) => sum + i.quantity * i.pricePerUnit, 0)

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    await createOrder(+userId, items)
    navigate('/orders')
  }

  return (
    <div style={page}>
      <Navbar />
      <div style={content}>
        <h2 style={{ marginBottom: '1.5rem' }}>Create Order</h2>
        <form onSubmit={handleSubmit}>
          <label style={label}>User ID</label>
          <input style={input} placeholder="User ID" value={userId} onChange={e => setUserId(e.target.value)} />

          <h3 style={{ margin: '1.5rem 0 0.75rem' }}>Items</h3>
          {items.map((item, idx) => (
            <div key={idx} style={rowStyle}>
              <input style={input} placeholder="Product ID" value={item.productId || ''}
                onChange={e => updateRow(idx, 'productId', e.target.value)} />
              <input style={input} placeholder="Qty" value={item.quantity}
                onChange={e => updateRow(idx, 'quantity', e.target.value)} />
              <input style={input} placeholder="Price/unit" value={item.pricePerUnit || ''}
                onChange={e => updateRow(idx, 'pricePerUnit', e.target.value)} />
              <button type="button" style={dangerBtn} onClick={() => removeRow(idx)}>✕</button>
            </div>
          ))}

          <button type="button" style={{ ...btn, marginTop: '0.5rem' }} onClick={addRow}>+ Add Item</button>

          <div style={{ margin: '1.5rem 0', fontSize: 18, fontWeight: 700 }}>
            Total: ${total.toFixed(2)}
          </div>

          <div style={{ display: 'flex', gap: '1rem' }}>
            <button style={btn} type="submit">Create Order</button>
            <button style={dangerBtn} type="button" onClick={() => navigate('/orders')}>Cancel</button>
          </div>
        </form>
      </div>
    </div>
  )
}

const page: React.CSSProperties = { minHeight: '100vh', background: '#1e1e2e', color: '#cdd6f4' }
const content: React.CSSProperties = { padding: '2rem', maxWidth: 600 }
const label: React.CSSProperties = { display: 'block', marginBottom: '0.4rem', color: '#a6adc8' }
const input: React.CSSProperties = { padding: '0.5rem', borderRadius: 6, background: '#313244', color: '#cdd6f4', border: '1px solid #45475a' }
const btn: React.CSSProperties = { padding: '0.5rem 1.2rem', borderRadius: 6, background: '#89b4fa', border: 'none', cursor: 'pointer', fontWeight: 600 }
const dangerBtn: React.CSSProperties = { ...btn, background: '#f38ba8' }
const rowStyle: React.CSSProperties = { display: 'flex', gap: '0.75rem', marginBottom: '0.5rem', alignItems: 'center' }
