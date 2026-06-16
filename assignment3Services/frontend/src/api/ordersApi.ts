import axios from 'axios'

const api = axios.create({ baseURL: 'http://localhost:8084' })

export interface OrderItemResponse {
  id: number
  productId: number
  quantity: number
  pricePerUnit: number
}

export interface OrderResponse {
  orderId: number
  userId: number
  totalPrice: number
  isPaid: boolean
  items: OrderItemResponse[]
}

export interface OrderItemRequest {
  productId: number
  quantity: number
  pricePerUnit: number
}

export const getAll = (sortBy?: string, direction = 'asc') =>
  api.get<OrderResponse[]>('/api/orders/findAll', { params: sortBy ? { sortBy, direction } : {} })

export const getById = (orderId: number) =>
  api.get<OrderResponse>(`/api/orders/findById/${orderId}`)

export const getByUserId = (userId: number, sortBy?: string, direction = 'asc') =>
  api.get<OrderResponse[]>(`/api/orders/findByUserId/${userId}`, { params: sortBy ? { sortBy, direction } : {} })

export const getByIsPaid = (isPaid: boolean, sortBy?: string, direction = 'asc') =>
  api.get<OrderResponse[]>('/api/orders/findByIsPaid', { params: sortBy ? { isPaid, sortBy, direction } : { isPaid } })

export const getByUserIdAndIsPaid = (userId: number, isPaid: boolean) =>
  api.get<OrderResponse[]>(`/api/orders/findByUserIdAndIsPaid/${userId}`, { params: { isPaid } })

export const createOrder = (userId: number, items: OrderItemRequest[]) =>
  api.post('/api/orders', { userId, items })

export const markAsPaid = (orderId: number) =>
  api.put(`/api/orders/markAsPaid/${orderId}`)

export const deleteOrder = (orderId: number) =>
  api.delete(`/api/orders/delete/${orderId}`)

export const addItem = (orderId: number, item: OrderItemRequest) =>
  api.post('/api/orders/addItem', item, { params: { orderId } })

export const removeItem = (itemId: number) =>
  api.delete(`/api/orders/removeItem/${itemId}`)
