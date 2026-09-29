const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || '/api'

async function request(path, options = {}) {
  const response = await fetch(`${API_BASE_URL}${path}`, {
    ...options,
    headers: {
      'Content-Type': 'application/json',
      ...options.headers,
    },
  })

  const body = await response.json().catch(() => ({}))
  if (!response.ok) {
    throw new Error(body.message || 'The request could not be completed.')
  }
  return body
}

export function login(credentials) {
  return request('/auth/login', {
    method: 'POST',
    body: JSON.stringify(credentials),
  })
}

export function register(candidate) {
  return request('/auth/register', {
    method: 'POST',
    body: JSON.stringify(candidate),
  })
}

export function storeSession(authResponse) {
  sessionStorage.setItem('aerocadet.accessToken', authResponse.accessToken)
  sessionStorage.setItem('aerocadet.user', JSON.stringify(authResponse.user))
}

