import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { getAll, getByUserId, getByIsPaid, getByUserIdAndIsPaid, markAsPaid, deleteOrder, type OrderResponse } from '../api/ordersApi'
import Navbar from '../components/Navbar'
import { useTranslation } from 'react-i18next'

export default function OrdersPage() {
  const { t } = useTranslation()
  const [orders, setOrders] = useState<OrderResponse[]>([])
  const [userId, setUserId] = useState('')
  const [isPaidFilter, setIsPaidFilter] = useState<'all' | 'true' | 'false'>('all')
  const [sortBy, setSortBy] = useState('')
  const [direction, setDirection] = useState<'asc' | 'desc'>('asc')
  const [expanded, setExpanded] = useState<number | null>(null)
  const navigate = useNavigate()

  const load = async () => {
    try {
      let res
      const sort = sortBy || undefined
      if (userId && isPaidFilter !== 'all') {
        res = await getByUserIdAndIsPaid(+userId, isPaidFilter === 'true')
      } else if (userId) {
        res = await getByUserId(+userId, sort, direction)
      } else if (isPaidFilter !== 'all') {
        res = await getByIsPaid(isPaidFilter === 'true', sort, direction)
      } else {
        res = await getAll(sort, direction)
      }
      setOrders(res.data)
    } catch {}
  }

  useEffect(() => { load() }, [sortBy, direction])

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

  return (
    <div style={page}>
      <Navbar />
      <div style={content}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1rem' }}>
          <h2>{t('orders.title')}</h2>
          <button style={btn} onClick={() => navigate('/orders/create')}>{t('orders.newOrder')}</button>
        </div>

        <div style={filterBar}>
          <input style={input} placeholder={t('orders.userId')} value={userId} onChange={e => setUserId(e.target.value)} />
          <select style={select} value={isPaidFilter} onChange={e => setIsPaidFilter(e.target.value as 'all' | 'true' | 'false')}>
            <option value="all">{t('orders.all')}</option>
            <option value="true">{t('orders.paid')}</option>
            <option value="false">{t('orders.unpaid')}</option>
          </select>
          <button style={btn} onClick={load}>{t('orders.filter')}</button>
          <select style={select} value={sortBy} onChange={e => setSortBy(e.target.value)}>
            <option value="">{t('orders.noSort')}</option>
            <option value="totalPrice">{t('orders.sortTotalPrice')}</option>
            <option value="orderId">{t('orders.sortOrderId')}</option>
          </select>
          <button style={btn} onClick={() => setDirection(d => d === 'asc' ? 'desc' : 'asc')}>
            {direction === 'asc' ? t('orders.asc') : t('orders.desc')}
          </button>
        </div>

        <table style={table}>
          <thead>
            <tr>{[t('orders.colOrderId'), t('orders.colUserId'), t('orders.colTotal'), t('orders.colPaid'), t('orders.colItems'), t('orders.colActions')].map(h => <th key={h} style={th}>{h}</th>)}</tr>
          </thead>
          <tbody>
            {orders.map(o => (
              <>
                <tr key={o.orderId} style={{ cursor: 'pointer' }} onClick={() => setExpanded(expanded === o.orderId ? null : o.orderId)}>
                  <td style={td}>{o.orderId}</td>
                  <td style={td}>{o.userId}</td>
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
                    <td colSpan={6} style={{ ...td, background: '#181825' }}>
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
          </tbody>
        </table>
      </div>
    </div>
  )
}

const page: React.CSSProperties = { minHeight: '100vh', background: '#1e1e2e', color: '#cdd6f4' }
const content: React.CSSProperties = { padding: '2rem' }
const filterBar: React.CSSProperties = { display: 'flex', gap: '0.75rem', marginBottom: '1rem', alignItems: 'center', flexWrap: 'wrap' }
const input: React.CSSProperties = { padding: '0.5rem', borderRadius: 6, background: '#313244', color: '#cdd6f4', border: '1px solid #45475a' }
const select: React.CSSProperties = { ...input }
const btn: React.CSSProperties = { padding: '0.5rem 1rem', borderRadius: 6, background: '#89b4fa', border: 'none', cursor: 'pointer', fontWeight: 600 }
const smallBtn: React.CSSProperties = { ...btn, padding: '0.3rem 0.6rem', marginRight: 6, fontSize: 12 }
const dangerBtn: React.CSSProperties = { ...smallBtn, background: '#f38ba8' }
const table: React.CSSProperties = { width: '100%', borderCollapse: 'collapse' }
const th: React.CSSProperties = { textAlign: 'left', padding: '0.6rem 1rem', background: '#313244', borderBottom: '2px solid #45475a' }
const td: React.CSSProperties = { padding: '0.6rem 1rem', borderBottom: '1px solid #313244' }
