import { Link, useNavigate } from 'react-router-dom'
import { getUserInfo, logout as logoutUser } from '../utils/auth'

export default function Navbar() {
  const navigate = useNavigate()
  const user = getUserInfo()
  const role = user?.userRole

  const doLogout = () => {
    logoutUser()
    navigate('/')
  }

  return (
    <nav style={nav}>
      <Link to="/" style={link}>Home</Link>
      {role === 'USER' && <Link to="/my-orders" style={link}>My Orders</Link>}
      {role === 'STORE_MANAGER' && <>
        <Link to="/manage-store" style={link}>My Shop</Link>
        <Link to="/my-orders" style={link}>My Orders</Link>
        <Link to="/shops" style={link}>Browse Shops</Link>
      </>}
      {role === 'ADMIN' && <>
        <Link to="/users" style={link}>Users</Link>
        <Link to="/products" style={link}>Products</Link>
        <Link to="/shops" style={link}>Shops</Link>
        <Link to="/orders" style={link}>Orders</Link>
      </>}
      {user && <span style={userBadge}>{user.username} · {role}</span>}
      <button onClick={doLogout} style={{ marginLeft: 'auto', ...logoutBtn }}>Logout</button>
    </nav>
  )
}

const nav: React.CSSProperties = { display: 'flex', gap: '1.5rem', padding: '1rem 2rem', background: '#181825', alignItems: 'center', borderBottom: '1px solid #313244' }
const link: React.CSSProperties = { color: '#cdd6f4', textDecoration: 'none', fontWeight: 600 }
const userBadge: React.CSSProperties = { color: '#a6adc8', fontSize: 13 }
const logoutBtn: React.CSSProperties = { background: '#f38ba8', border: 'none', borderRadius: 6, padding: '0.4rem 1rem', cursor: 'pointer', fontWeight: 600 }
