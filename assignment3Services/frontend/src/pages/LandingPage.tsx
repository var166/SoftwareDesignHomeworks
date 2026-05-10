import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { getAll as getProducts, type ProductResponse } from '../api/productsApi'
import { getAll as getShops, type ShopResponse } from '../api/shopsApi'
import { getToken, getUserInfo } from '../utils/auth'

export default function LandingPage() {
  const [products, setProducts] = useState<ProductResponse[]>([])
  const [shops, setShops] = useState<ShopResponse[]>([])
  const [error, setError] = useState('')
  const navigate = useNavigate()
  const user = getUserInfo()

  useEffect(() => {
    getProducts().then(r => setProducts(r.data)).catch(() => setError('Failed to load products'))
    getShops().then(r => setShops(r.data)).catch(() => setError('Failed to load shops'))
  }, [])

  const goToDashboard = () => {
    if (!user) { navigate('/login'); return }
    if (user.userRole === 'ADMIN') navigate('/users')
    else if (user.userRole === 'STORE_MANAGER') navigate('/manage-store')
    else navigate('/my-orders')
  }

  return (
    <div style={page}>
      <header style={header}>
        <span style={logo}>Marketplace</span>
        <div style={{ display: 'flex', gap: '1rem', alignItems: 'center' }}>
          {user && <span style={badge}>{user.username} · {user.userRole}</span>}
          {user
            ? <button style={btn} onClick={goToDashboard}>Dashboard</button>
            : <button style={btn} onClick={() => navigate('/login')}>Login</button>}
        </div>
      </header>

      {error && <div style={errorBox}>{error}</div>}

      <main style={main}>
        <section style={section}>
          <h2 style={sectionTitle}>Products</h2>
          <div style={grid}>
            {products.map(p => (
              <div key={p.id} style={card}>
                <div style={cardName}>{p.name}</div>
                <div style={cardDesc}>{p.description}</div>
                <div style={cardPrice}>${p.price?.toFixed(2)}</div>
                <div style={cardMeta}>Shop #{p.shopId}</div>
              </div>
            ))}
            {products.length === 0 && <p style={{ color: '#a6adc8' }}>No products found.</p>}
          </div>
        </section>

        <section style={section}>
          <h2 style={sectionTitle}>Shops</h2>
          <div style={grid}>
            {shops.map(s => (
              <div key={s.id} style={card}>
                <div style={cardName}>{s.name}</div>
                <div style={cardDesc}>{s.address}</div>
                <div style={cardMeta}>{Object.keys(s.productStock || {}).length} products in stock</div>
                <div style={cardMeta}>{s.email}</div>
              </div>
            ))}
            {shops.length === 0 && <p style={{ color: '#a6adc8' }}>No shops found.</p>}
          </div>
        </section>
      </main>
    </div>
  )
}

const page: React.CSSProperties = { minHeight: '100vh', background: '#1e1e2e', color: '#cdd6f4' }
const header: React.CSSProperties = { display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '1rem 2rem', background: '#181825', borderBottom: '1px solid #313244' }
const logo: React.CSSProperties = { fontSize: 22, fontWeight: 700, color: '#89b4fa' }
const badge: React.CSSProperties = { color: '#a6adc8', fontSize: 13 }
const btn: React.CSSProperties = { padding: '0.5rem 1.2rem', borderRadius: 6, background: '#89b4fa', border: 'none', cursor: 'pointer', fontWeight: 700 }
const errorBox: React.CSSProperties = { background: '#f38ba822', color: '#f38ba8', padding: '0.75rem 2rem', borderBottom: '1px solid #f38ba8' }
const main: React.CSSProperties = { padding: '2rem' }
const section: React.CSSProperties = { marginBottom: '3rem' }
const sectionTitle: React.CSSProperties = { marginBottom: '1rem', fontSize: 20 }
const grid: React.CSSProperties = { display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(220px, 1fr))', gap: '1rem' }
const card: React.CSSProperties = { background: '#313244', borderRadius: 10, padding: '1rem', display: 'flex', flexDirection: 'column', gap: '0.4rem' }
const cardName: React.CSSProperties = { fontWeight: 700, fontSize: 16 }
const cardDesc: React.CSSProperties = { color: '#a6adc8', fontSize: 13 }
const cardPrice: React.CSSProperties = { color: '#a6e3a1', fontWeight: 700 }
const cardMeta: React.CSSProperties = { color: '#6c7086', fontSize: 12 }
