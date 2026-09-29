import { useState } from 'react'
import { ArrowRight, CheckCircle2, Eye, EyeOff, Plane, X } from 'lucide-react'
import { login, register, storeSession } from '../lib/api'

const initialRegistration = {
  fullName: '',
  email: '',
  phone: '',
  password: '',
  dateOfBirth: '',
  nationality: '',
  city: '',
  state: '',
  country: '',
}

function AuthModal({ initialMode = 'login', onClose, onAuthenticated }) {
  const [mode, setMode] = useState(initialMode)
  const [loginForm, setLoginForm] = useState({ email: '', password: '' })
  const [registrationForm, setRegistrationForm] = useState(initialRegistration)
  const [showPassword, setShowPassword] = useState(false)
  const [status, setStatus] = useState({ loading: false, error: '', success: '' })

  const isLogin = mode === 'login'

  function switchMode(nextMode) {
    setMode(nextMode)
    setStatus({ loading: false, error: '', success: '' })
  }

  async function handleSubmit(event) {
    event.preventDefault()
    setStatus({ loading: true, error: '', success: '' })
    try {
      const response = isLogin
        ? await login(loginForm)
        : await register(registrationForm)
      storeSession(response)
      setStatus({
        loading: false,
        error: '',
        success: isLogin
          ? `Welcome back, ${response.user.fullName}.`
          : 'Your candidate account is ready. Welcome aboard.',
      })
      onAuthenticated?.(response.user)
    } catch (error) {
      setStatus({ loading: false, error: error.message, success: '' })
    }
  }

  function updateRegistration(event) {
    setRegistrationForm((current) => ({ ...current, [event.target.name]: event.target.value }))
  }

  return (
    <div className="modal-backdrop" role="presentation" onMouseDown={onClose}>
      <section
        className="auth-modal"
        role="dialog"
        aria-modal="true"
        aria-labelledby="auth-title"
        onMouseDown={(event) => event.stopPropagation()}
      >
        <aside className="auth-story">
          <div className="auth-brand"><span><Plane size={19} /></span> AeroCadet</div>
          <div>
            <p>YOUR FLIGHT DECK WORKSPACE</p>
            <h2>Every milestone.<br />One clear journey.</h2>
            <ul>
              <li><CheckCircle2 /> Save and resume applications</li>
              <li><CheckCircle2 /> Track verification in real time</li>
              <li><CheckCircle2 /> Never miss a selection stage</li>
            </ul>
          </div>
          <small>Educational demo · Synthetic candidate data only</small>
        </aside>

        <div className="auth-form-panel">
          <button className="modal-close" type="button" onClick={onClose} aria-label="Close authentication dialog">
            <X size={19} />
          </button>
          <p className="auth-overline">{isLogin ? 'WELCOME BACK' : 'START YOUR JOURNEY'}</p>
          <h2 id="auth-title">{isLogin ? 'Sign in to AeroCadet' : 'Create candidate account'}</h2>
          <p className="auth-subtitle">
            {isLogin ? 'Continue managing your aviation application journey.' : 'Start with the essentials. You can complete your aviation profile next.'}
          </p>

          <div className="auth-tabs" role="tablist" aria-label="Authentication mode">
            <button type="button" role="tab" aria-selected={isLogin} className={isLogin ? 'active' : ''} onClick={() => switchMode('login')}>Sign in</button>
            <button type="button" role="tab" aria-selected={!isLogin} className={!isLogin ? 'active' : ''} onClick={() => switchMode('register')}>Register</button>
          </div>

          <form onSubmit={handleSubmit} className="auth-form">
            {!isLogin && (
              <>
                <label className="full-field">Full name<input required name="fullName" autoComplete="name" maxLength="120" value={registrationForm.fullName} onChange={updateRegistration} placeholder="Your full legal name" /></label>
                <label>Phone<input required name="phone" autoComplete="tel" value={registrationForm.phone} onChange={updateRegistration} placeholder="+91 90000 00000" /></label>
                <label>Date of birth<input required name="dateOfBirth" type="date" value={registrationForm.dateOfBirth} onChange={updateRegistration} /></label>
              </>
            )}

            <label className="full-field">Email address<input required name="email" type="email" autoComplete="email" value={isLogin ? loginForm.email : registrationForm.email} onChange={(event) => isLogin ? setLoginForm({ ...loginForm, email: event.target.value }) : updateRegistration(event)} placeholder="candidate@example.com" /></label>

            <label className="full-field">Password
              <span className="password-input">
                <input required name="password" type={showPassword ? 'text' : 'password'} minLength={isLogin ? undefined : 10} autoComplete={isLogin ? 'current-password' : 'new-password'} value={isLogin ? loginForm.password : registrationForm.password} onChange={(event) => isLogin ? setLoginForm({ ...loginForm, password: event.target.value }) : updateRegistration(event)} placeholder={isLogin ? 'Enter your password' : 'At least 10 characters'} />
                <button type="button" onClick={() => setShowPassword((visible) => !visible)} aria-label={showPassword ? 'Hide password' : 'Show password'}>{showPassword ? <EyeOff size={17} /> : <Eye size={17} />}</button>
              </span>
            </label>

            {!isLogin && (
              <>
                <label>Nationality<input required name="nationality" value={registrationForm.nationality} onChange={updateRegistration} placeholder="Nationality" /></label>
                <label>City<input required name="city" value={registrationForm.city} onChange={updateRegistration} placeholder="City" /></label>
                <label>State<input required name="state" value={registrationForm.state} onChange={updateRegistration} placeholder="State" /></label>
                <label>Country<input required name="country" value={registrationForm.country} onChange={updateRegistration} placeholder="Country" /></label>
              </>
            )}

            {status.error && <div className="auth-message error" role="alert">{status.error}</div>}
            {status.success && <div className="auth-message success" role="status">{status.success}</div>}

            <button className="auth-submit" type="submit" disabled={status.loading}>
              {status.loading ? 'Please wait…' : isLogin ? 'Sign in securely' : 'Create my account'}
              {!status.loading && <ArrowRight size={17} />}
            </button>
          </form>

          <p className="auth-switch">
            {isLogin ? 'New to AeroCadet?' : 'Already registered?'}{' '}
            <button type="button" onClick={() => switchMode(isLogin ? 'register' : 'login')}>
              {isLogin ? 'Create an account' : 'Sign in'}
            </button>
          </p>
        </div>
      </section>
    </div>
  )
}

export default AuthModal

