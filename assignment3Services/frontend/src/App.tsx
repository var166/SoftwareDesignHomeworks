import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import LoginPage from './pages/LoginPage'
import LandingPage from './pages/LandingPage'
import UsersPage from './pages/UsersPage'
import ProductsPage from './pages/ProductsPage'
import ShopsPage from './pages/ShopsPage'
import ShopDetailPage from './pages/ShopDetailPage'
import OrdersPage from './pages/OrdersPage'
import CreateOrderPage from './pages/CreateOrderPage'
import UserOrdersPage from './pages/UserOrdersPage'
import ManageStorePage from './pages/ManageStorePage'
import ProtectedRoute from './components/ProtectedRoute'

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<LandingPage />} />
        <Route path="/login" element={<LoginPage />} />

        {/* USER role */}
        <Route path="/my-orders" element={
          <ProtectedRoute roles={['USER', 'STORE_MANAGER', 'ADMIN']}>
            <UserOrdersPage />
          </ProtectedRoute>
        } />

        {/* STORE_MANAGER role */}
        <Route path="/manage-store" element={
          <ProtectedRoute roles={['STORE_MANAGER', 'ADMIN']}>
            <ManageStorePage />
          </ProtectedRoute>
        } />

        {/* ADMIN only */}
        <Route path="/users" element={
          <ProtectedRoute roles={['ADMIN']}>
            <UsersPage />
          </ProtectedRoute>
        } />
        <Route path="/products" element={
          <ProtectedRoute roles={['ADMIN']}>
            <ProductsPage />
          </ProtectedRoute>
        } />
        <Route path="/shops" element={
          <ProtectedRoute roles={['ADMIN', 'STORE_MANAGER']}>
            <ShopsPage />
          </ProtectedRoute>
        } />
        <Route path="/shops/:id" element={
          <ProtectedRoute roles={['ADMIN', 'STORE_MANAGER']}>
            <ShopDetailPage />
          </ProtectedRoute>
        } />
        <Route path="/orders" element={
          <ProtectedRoute roles={['ADMIN']}>
            <OrdersPage />
          </ProtectedRoute>
        } />
        <Route path="/orders/create" element={
          <ProtectedRoute roles={['ADMIN']}>
            <CreateOrderPage />
          </ProtectedRoute>
        } />

        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </BrowserRouter>
  )
}
