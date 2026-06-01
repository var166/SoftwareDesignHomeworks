import { Link, useNavigate } from 'react-router-dom'
import { getUserInfo, logout as logoutUser } from '../utils/auth'
import { useTranslation } from 'react-i18next'
import LanguageSwitcher from '../components/LanguageSwitcher'

export default function Navbar() {
  const navigate = useNavigate()
  const user = getUserInfo()
  const role = user?.userRole
  const { t } = useTranslation()

  const doLogout = () => {
    logoutUser()
    navigate('/')
  }

  return (
    <nav style={nav}>
      <Link to="/" style={link}>{t('nav.home')}</Link>
      {role === 'USER' && <Link to="/my-orders" style={link}>{t('nav.orders')}</Link>}
      {role === 'STORE_MANAGER' && <>
        <Link to="/manage-store" style={link}>{t('nav.myShop')}</Link>
        <Link to="/my-orders" style={link}>{t('nav.orders')}</Link>
        <Link to="/shops" style={link}>{t('nav.browseShops')}</Link>
      </>}
      {role === 'ADMIN' && <>
        <Link to="/users" style={link}>{t('nav.users')}</Link>
        <Link to="/products" style={link}>{t('nav.products')}</Link>
        <Link to="/shops" style={link}>{t('nav.shops')}</Link>
        <Link to="/orders" style={link}>{t('nav.orders')}</Link>
      </>}
      {user && <Link to="/chat" style={link}>{t('nav.chat')}</Link>}
      {user && <span style={userBadge}>{user.username} · {role}</span>}
      <LanguageSwitcher />
      <button onClick={doLogout} style={{ marginLeft: 'auto', ...logoutBtn }}>{t('nav.logout')}</button>
    </nav>
  )
}

const nav: React.CSSProperties = { display: 'flex', gap: '1.5rem', padding: '1rem 2rem', background: '#181825', alignItems: 'center', borderBottom: '1px solid #313244' }
const link: React.CSSProperties = { color: '#cdd6f4', textDecoration: 'none', fontWeight: 600 }
const userBadge: React.CSSProperties = { color: '#a6adc8', fontSize: 13 }
const logoutBtn: React.CSSProperties = { background: '#f38ba8', border: 'none', borderRadius: 6, padding: '0.4rem 1rem', cursor: 'pointer', fontWeight: 600 }
