import axios from 'axios'

const api = axios.create({ baseURL: '' })

export interface ShopResponse {
  id: number
  name: string
  address: string
  phone: string
  email: string
  description: string
  adminId: number
  productStock: Record<string, number>
}

export const getAll = (sortBy?: string, direction = 'asc') =>
  api.get<ShopResponse[]>('/api/shops/findAll', { params: sortBy ? { sortBy, direction } : {} })

export const getById = (id: number) =>
  api.get<ShopResponse>(`/api/shops/findById/${id}`)

export const getByAdminId = (adminId: number, sortBy?: string, direction = 'asc') =>
  api.get<ShopResponse[]>(`/api/shops/findByAdminId/${adminId}`, { params: sortBy ? { sortBy, direction } : {} })

export const create = (name: string, address: string, phone: string, email: string, description: string, adminId: number) =>
  api.post('/api/shops', { name, address, phone, email, description, adminId })

export const deleteShop = (id: number) =>
  api.delete(`/api/shops/delete/${id}`)

export const addProduct = (shopId: number, productId: number, initialStock: number) =>
  api.post('/api/shops/addProduct', null, { params: { shopId, productId, initialStock } })

export const removeProduct = (shopId: number, productId: number) =>
  api.delete('/api/shops/removeProduct', { params: { shopId, productId } })

export const updateStock = (shopId: number, productId: number, quantity: number) =>
  api.put('/api/shops/updateStock', null, { params: { shopId, productId, quantity } })

export const incrementStock = (shopId: number, productId: number, amount: number) =>
  api.put('/api/shops/incrementStock', null, { params: { shopId, productId, amount } })

export const decrementStock = (shopId: number, productId: number, amount: number) =>
  api.put('/api/shops/decrementStock', null, { params: { shopId, productId, amount } })
