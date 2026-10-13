import { useEffect, useState } from 'react'
import { getByAdminId, addProduct, removeProduct, updateStock, incrementStock, decrementStock, type ShopResponse } from '../api/shopsApi'
import Navbar from '../components/Navbar'
import { getUserInfo } from '../utils/auth'
import { useTranslation } from 'react-i18next'

export default function ManageStorePage() {
  const { t } = useTranslation()
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
    } catch {}
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
    } catch {}
  }

  const handleUpdateStock = async (productId: string) => {
    if (!shop) return
    const val = stockEdits[productId]
    if (isNaN(Number(val)) || val === undefined) { alert('Stock must be a number'); return }
    try { await updateStock(shop.id, +productId, +val); load() }
    catch {}
  }

  const handleIncrement = async (productId: string) => {
    if (!shop) return
    try { await incrementStock(shop.id, +productId, 1); load() }
    catch {}
  }

  const handleDecrement = async (productId: string) => {
    if (!shop) return
    try { await decrementStock(shop.id, +productId, 1); load() }
    catch {}
  }

  const handleRemove = async (productId: string) => {
    if (!shop) return
    try { await removeProduct(shop.id, +productId); load() }
    catch {}
  }

  if (noShop) return (
    <div style={page}>
      <Navbar />
      <div style={content}>
        <p style={{ color: '#a6adc8' }}>{t('manageStore.noShop')}</p>
      </div>
    </div>
  )

  if (!shop) return (
    <div style={page}>
      <Navbar />
      <div style={content}><p style={{ color: '#a6adc8' }}>{t('manageStore.loading')}</p></div>
    </div>
  )

  return (
    <div style={page}>
      <Navbar />
      <div style={content}>
        <h2 style={{ marginBottom: '0.25rem' }}>{shop.name}</h2>
        <p style={{ color: '#a6adc8', marginBottom: '2rem' }}>{shop.address} · {shop.email} · {shop.phone}</p>

        <h3 style={{ marginBottom: '1rem' }}>{t('manageStore.stockManagement')}</h3>
        <form onSubmit={handleAdd} style={formRow}>
          <input style={input} placeholder={t('manageStore.productIdPlaceholder')} value={newProductId} onChange={e => setNewProductId(e.target.value)} />
          <input style={input} placeholder={t('manageStore.initialStockPlaceholder')} value={newStock} onChange={e => setNewStock(e.target.value)} />
          <button style={btn} type="submit">{t('manageStore.addProduct')}</button>
        </form>

        <table style={table}>
          <thead>
            <tr>{[t('manageStore.colProductId'), t('manageStore.colStock'), t('manageStore.colUpdateStock'), t('manageStore.colActions')].map(h => <th key={h} style={th}>{h}</th>)}</tr>
          </thead>
          <tbody>
            {Object.entries(shop.productStock || {}).map(([pid, qty]) => (
              <tr key={pid}>
                <td style={td}>{pid}</td>
                <td style={td}>{qty}</td>
                <td style={td}>
                  <input style={{ ...input, width: 80 }} placeholder={t('manageStore.qtyPlaceholder')}
                    value={stockEdits[pid] ?? ''}
                    onChange={e => setStockEdits(s => ({ ...s, [pid]: e.target.value }))} />
                  <button style={smallBtn} onClick={() => handleUpdateStock(pid)}>{t('manageStore.set')}</button>
                  <button style={smallBtn} onClick={() => handleIncrement(pid)}>{t('manageStore.increment')}</button>
                  <button style={smallBtn} onClick={() => handleDecrement(pid)}>{t('manageStore.decrement')}</button>
                </td>
                <td style={td}>
                  <button style={dangerBtn} onClick={() => handleRemove(pid)}>{t('manageStore.remove')}</button>
                </td>
              </tr>
            ))}
            {Object.keys(shop.productStock || {}).length === 0 && (
              <tr><td colSpan={4} style={{ ...td, color: '#a6adc8', textAlign: 'center' }}>{t('manageStore.empty')}</td></tr>
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
