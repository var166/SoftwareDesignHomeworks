import { useEffect, useRef, useState } from 'react'
import { useTranslation } from 'react-i18next'
import { createChatSocket } from '../api/chatSocket'
import Navbar from '../components/Navbar'

interface ChatMessage {
  sender: string
  content: string
  timestamp: string
}

export default function ChatPage() {
  const { t } = useTranslation()
  const [messages, setMessages] = useState<ChatMessage[]>([])
  const [input, setInput] = useState('')
  const [status, setStatus] = useState('connecting')
  const wsRef = useRef<WebSocket | null>(null)
  const listRef = useRef<HTMLDivElement>(null)

  useEffect(() => {
    let ws: WebSocket
    try {
      ws = createChatSocket()
    } catch {
      setStatus('not-authenticated')
      return
    }
    wsRef.current = ws
    ws.onopen = () => setStatus('connected')
    ws.onclose = () => setStatus('disconnected')
    ws.onerror = () => setStatus('error')
    ws.onmessage = (e) => {
      try {
        const msg: ChatMessage = JSON.parse(e.data)
        setMessages((prev) => [...prev, msg])
      } catch {}
    }
    return () => { try { ws.close() } catch {} }
  }, [])

  useEffect(() => {
    if (listRef.current) listRef.current.scrollTop = listRef.current.scrollHeight
  }, [messages])

  const send = () => {
    const text = input.trim()
    if (!text) return
    if (wsRef.current?.readyState !== WebSocket.OPEN) return
    wsRef.current.send(text)
    setInput('')
  }

  return (
    <>
      <Navbar />
      <div style={{ maxWidth: 700, margin: '20px auto', fontFamily: 'system-ui', padding: '0 1rem' }}>
        <h2>{t('chat.title')}</h2>
        <div style={{ fontSize: 12, color: '#a6adc8', marginBottom: 8 }}>
          {t('chat.status')}: {status}
        </div>
        <div ref={listRef} style={{
          height: 400, overflowY: 'auto',
          border: '1px solid #313244', padding: 12, borderRadius: 6, background: '#1e1e2e'
        }}>
          {messages.map((m, i) => (
            <div key={i} style={{ marginBottom: 8, color: '#cdd6f4' }}>
              <b style={{ color: '#89b4fa' }}>{m.sender}</b>
              <span style={{ color: '#585b70', fontSize: 11, marginLeft: 6 }}>
                {new Date(m.timestamp).toLocaleTimeString()}
              </span>
              <div>{m.content}</div>
            </div>
          ))}
        </div>
        <div style={{ display: 'flex', gap: 8, marginTop: 8 }}>
          <input
            style={{
              flex: 1, padding: 8,
              border: '1px solid #313244', borderRadius: 4,
              background: '#181825', color: '#cdd6f4'
            }}
            value={input}
            onChange={(e) => setInput(e.target.value)}
            onKeyDown={(e) => e.key === 'Enter' && send()}
            placeholder={t('chat.placeholder')}
            disabled={status !== 'connected'}
          />
          <button
            onClick={send}
            disabled={status !== 'connected'}
            style={{
              background: '#89b4fa', border: 'none', borderRadius: 6,
              padding: '0.4rem 1rem', cursor: 'pointer', fontWeight: 600
            }}
          >
            {t('chat.send')}
          </button>
        </div>
      </div>
    </>
  )
}
