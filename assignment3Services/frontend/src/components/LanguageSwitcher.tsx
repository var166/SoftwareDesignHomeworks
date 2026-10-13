import { useTranslation } from 'react-i18next'

export default function LanguageSwitcher() {
  const { i18n } = useTranslation()
  const current = i18n.resolvedLanguage ?? 'en'

  const handleClick = (lang: 'en' | 'ro') => {
    void i18n.changeLanguage(lang)
  }

  const buttonStyle = (lang: string): React.CSSProperties => ({
    fontWeight: current === lang ? 700 : 400,
    textDecoration: current === lang ? 'underline' : 'none',
    background: 'none',
    border: '1px solid #585b70',
    borderRadius: 4,
    padding: '4px 8px',
    marginLeft: 4,
    cursor: 'pointer',
    color: '#cdd6f4',
    fontSize: 13,
  })

  return (
    <div style={{ display: 'inline-flex', alignItems: 'center' }}>
      <button type="button" onClick={() => handleClick('en')} style={buttonStyle('en')} aria-label="Switch to English">
        EN
      </button>
      <button type="button" onClick={() => handleClick('ro')} style={buttonStyle('ro')} aria-label="Switch to Romanian">
        RO
      </button>
    </div>
  )
}
