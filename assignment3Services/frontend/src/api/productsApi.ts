import axios from 'axios'

const api = axios.create({ baseURL: '' })

export interface ProductResponse {
  id: number
  name: string
  description: string
  price: number
  shopId: number
}

export const getAll = (sortBy?: string, direction = 'asc') =>
  api.get<ProductResponse[]>('/api/products/findAll', { params: sortBy ? { sortBy, direction } : {} })

export const getByShopId = (shopId: number, sortBy?: string, direction = 'asc') =>
  api.get<ProductResponse[]>(`/api/products/findByShopId/${shopId}`, { params: sortBy ? { sortBy, direction } : {} })

export const getByPriceRange = (min: number, max: number, sortBy?: string, direction = 'asc') =>
  api.get<ProductResponse[]>('/api/products/findByPriceRange', { params: sortBy ? { min, max, sortBy, direction } : { min, max } })

export const create = (name: string, description: string, price: number, shopId: number, userEmail?: string) =>
  api.post('/api/products', { name, description, price, shopId }, { params: { userEmail } })

export const updatePrice = (id: number, price: number, userEmail?: string) =>
  api.put('/api/products/updatePrice', null, { params: { id, price, userEmail } })

export const updateDescription = (id: number, description: string, userEmail?: string) =>
  api.put('/api/products/updateDescription', null, { params: { id, description, userEmail } })

export const deleteProduct = (id: number) =>
  api.delete(`/api/products/delete/${id}`)
