import { useEffect, useState } from 'react'
import { getByUserId, createOrder, markAsPaid, deleteOrder, type OrderResponse, type OrderItemRequest } from '../api/ordersApi'
import { getAll as getProducts, type ProductResponse } from '../api/productsApi'
import { getAll as getShops } from '../api/shopsApi'
import Navbar from '../components/Navbar'
import { getUserInfo } from '../utils/auth'
import { useTranslation } from 'react-i18next'

interface AvailableProduct {
  product: ProductResponse
  stock: number
}

export default function UserOrdersPage() {
  const { t } = useTranslation()
  const [orders, setOrders] = useState<OrderResponse[]>([])
  const [expanded, setExpanded] = useState<number | null>(null)
  const [showCreate, setShowCreate] = useState(false)
  const [availableProducts, setAvailableProducts] = useState<AvailableProduct[]>([])
  const [quantities, setQuantities] = useState<Record<number, string>>({})
  const [loadingProducts, setLoadingProducts] = useState(false)
  const [error] = useState('')
  const user = getUserInfo()

  const load = async () => {
    if (!user) return
    try {
      const res = await getByUserId(user.id)
      setOrders(res.data)
    } catch {}
  }

  useEffect(() => { load() }, [])

  const loadAvailableProducts = async () => {
    setLoadingProducts(true)
    try {
      const [productsRes, shopsRes] = await Promise.all([getProducts(), getShops()])

      // aggregate stock across all shops: productId -> total stock
      const stockMap: Record<number, number> = {}
      for (const shop of shopsRes.data) {
        for (const [pid, qty] of Object.entries(shop.productStock || {})) {
          stockMap[+pid] = (stockMap[+pid] || 0) + qty
        }
      }

      const available = productsRes.data
        .filter(p => (stockMap[p.id] || 0) > 0)
        .map(p => ({ product: p, stock: stockMap[p.id] }))

      setAvailableProducts(available)
    } catch {}
    finally { setLoadingProducts(false) }
  }

  const openCreate = () => {
    setShowCreate(true)
    setQuantities({})
    loadAvailableProducts()
  }

  const handleMarkPaid = async (orderId: number, e: React.MouseEvent) => {
    e.stopPropagation()
    try { await markAsPaid(orderId); load() }
    catch {}
  }

  const handleDelete = async (orderId: number, e: React.MouseEvent) => {
    e.stopPropagation()
    try { await deleteOrder(orderId); load() }
    catch {}
  }

  const handleCreate = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!user) return

    const items: OrderItemRequest[] = []
    for (const { product } of availableProducts) {
      const qtyStr = quantities[product.id]
      if (!qtyStr || qtyStr === '0' || qtyStr === '') continue
      const qty = Number(qtyStr)
      if (isNaN(qty) || qty <= 0) { alert(`Invalid quantity for ${product.name}`); return }
      items.push({ productId: product.id, quantity: qty, pricePerUnit: product.price })
    }

    if (items.length === 0) { alert('Select at least one product with quantity > 0'); return }

    try {
      await createOrder(user.id, items)
      setShowCreate(false)
      setQuantities({})
      load()
    } catch {}
  }

  const total = availableProducts.reduce((sum, { product }) => {
    const qty = Number(quantities[product.id] || 0)
    return sum + (isNaN(qty) ? 0 : qty * product.price)
  }, 0)

  return (
    <div style={page}>
      <Navbar />
      <div style={content}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1rem' }}>
          <h2>{t('orders.myTitle')}</h2>
          {!showCreate
            ? <button style={btn} onClick={openCreate}>{t('orders.newOrder')}</button>
            : <button style={dangerBtn} onClick={() => setShowCreate(false)}>{t('orders.cancel')}</button>}
        </div>

        {error && <div style={errorBox}>{error}</div>}

        {showCreate && (
          <form onSubmit={handleCreate} style={createForm}>
            <h3 style={{ marginBottom: '1rem' }}>{t('orders.selectProducts')}</h3>
            {loadingProducts && <p style={{ color: '#a6adc8' }}>{t('orders.loadingProducts')}</p>}
            {!loadingProducts && availableProducts.length === 0 && (
              <p style={{ color: '#a6adc8' }}>{t('orders.noProductsInStock')}</p>
            )}
            {!loadingProducts && availableProducts.length > 0 && (
              <>
                <table style={table}>
                  <thead>
                    <tr>{[t('orders.colProduct'), t('orders.colPrice'), t('orders.colInStock'), t('orders.colQty')].map(h => <th key={h} style={th}>{h}</th>)}</tr>
                  </thead>
                  <tbody>
                    {availableProducts.map(({ product, stock }) => (
                      <tr key={product.id}>
                        <td style={td}>
                          <div style={{ fontWeight: 600 }}>{product.name}</div>
                          <div style={{ color: '#a6adc8', fontSize: 12 }}>{product.description}</div>
                        </td>
                        <td style={td}>${product.price.toFixed(2)}</td>
                        <td style={td}>{stock}</td>
                        <td style={td}>
                          <input
                            style={{ ...input, width: 70 }}
                            type="number"
                            min={0}
                            max={stock}
                            placeholder="0"
                            value={quantities[product.id] ?? ''}
                            onChange={e => setQuantities(q => ({ ...q, [product.id]: e.target.value }))}
                          />
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
                <div style={{ margin: '1rem 0', fontWeight: 700, fontSize: 16 }}>
                  {t('orders.total')}{total.toFixed(2)}
                </div>
                <button style={btn} type="submit">{t('orders.placeOrder')}</button>
              </>
            )}
          </form>
        )}

        <table style={table}>
          <thead>
            <tr>{[t('orders.colOrderId'), t('orders.colTotal'), t('orders.colPaid'), t('orders.colItems'), t('orders.colActions')].map(h => <th key={h} style={th}>{h}</th>)}</tr>
          </thead>
          <tbody>
            {orders.map(o => (
              <>
                <tr key={o.orderId} style={{ cursor: 'pointer' }} onClick={() => setExpanded(expanded === o.orderId ? null : o.orderId)}>
                  <td style={td}>{o.orderId}</td>
                  <td style={td}>${o.totalPrice?.toFixed(2)}</td>
                  <td style={td}>{o.isPaid ? '✅' : '❌'}</td>
                  <td style={td}>{o.items?.length ?? 0}</td>
                  <td style={td}>
                    {!o.isPaid && <button style={smallBtn} onClick={e => handleMarkPaid(o.orderId, e)}>{t('orders.markPaid')}</button>}
                    <button style={dangerBtn} onClick={e => handleDelete(o.orderId, e)}>{t('orders.delete')}</button>
                  </td>
                </tr>
                {expanded === o.orderId && (
                  <tr key={`${o.orderId}-items`}>
                    <td colSpan={5} style={{ ...td, background: '#181825' }}>
                      <table style={{ ...table, fontSize: 13 }}>
                        <thead><tr>{[t('orders.colItemId'), t('orders.colProductId'), t('orders.colQty'), t('orders.colPriceUnit')].map(h => <th key={h} style={th}>{h}</th>)}</tr></thead>
                        <tbody>
                          {(o.items || []).map(item => (
                            <tr key={item.id}>
                              <td style={td}>{item.id}</td>
                              <td style={td}>{item.productId}</td>
                              <td style={td}>{item.quantity}</td>
                              <td style={td}>${item.pricePerUnit}</td>
                            </tr>
                          ))}
                        </tbody>
                      </table>
                    </td>
                  </tr>
                )}
              </>
            ))}
            {orders.length === 0 && (
              <tr><td colSpan={5} style={{ ...td, color: '#a6adc8', textAlign: 'center' }}>{t('orders.empty')}</td></tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  )
}

const page: React.CSSProperties = { minHeight: '100vh', background: '#1e1e2e', color: '#cdd6f4' }
const content: React.CSSProperties = { padding: '2rem' }
const errorBox: React.CSSProperties = { background: '#f38ba822', color: '#f38ba8', padding: '0.75rem', borderRadius: 6, marginBottom: '1rem' }
const createForm: React.CSSProperties = { background: '#313244', padding: '1.5rem', borderRadius: 10, marginBottom: '1.5rem' }
const input: React.CSSProperties = { padding: '0.5rem', borderRadius: 6, background: '#1e1e2e', color: '#cdd6f4', border: '1px solid #45475a' }
const btn: React.CSSProperties = { padding: '0.5rem 1rem', borderRadius: 6, background: '#89b4fa', border: 'none', cursor: 'pointer', fontWeight: 600 }
const smallBtn: React.CSSProperties = { ...btn, padding: '0.3rem 0.6rem', marginRight: 6, fontSize: 12 }
const dangerBtn: React.CSSProperties = { ...smallBtn, background: '#f38ba8', marginRight: 0 }
const table: React.CSSProperties = { width: '100%', borderCollapse: 'collapse' }
const th: React.CSSProperties = { textAlign: 'left', padding: '0.6rem 1rem', background: '#1e1e2e', borderBottom: '2px solid #45475a' }
const td: React.CSSProperties = { padding: '0.6rem 1rem', borderBottom: '1px solid #313244' }
