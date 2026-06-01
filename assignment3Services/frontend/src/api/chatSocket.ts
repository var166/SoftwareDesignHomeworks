export function createChatSocket(): WebSocket {
  const token = localStorage.getItem('token')
  if (!token) throw new Error('No JWT token found. User must log in.')
  const proto = window.location.protocol === 'https:' ? 'wss' : 'ws'
  const url = `${proto}://${window.location.host}/ws/chat?token=${encodeURIComponent(token)}`
  return new WebSocket(url)
}
