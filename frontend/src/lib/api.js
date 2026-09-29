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

async function authenticatedRequest(path, options = {}) {
  const token = sessionStorage.getItem('aerocadet.accessToken')
  return request(path, {
    ...options,
    headers: {
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
      ...options.headers,
    },
  })
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

export function getPrograms(params = {}) {
  const query = new URLSearchParams(params).toString()
  return request(`/programs${query ? `?${query}` : ''}`)
}

export function storeSession(authResponse) {
  sessionStorage.setItem('aerocadet.accessToken', authResponse.accessToken)
  sessionStorage.setItem('aerocadet.user', JSON.stringify(authResponse.user))
}

export function getStoredUser() {
  try {
    return JSON.parse(sessionStorage.getItem('aerocadet.user'))
  } catch {
    return null
  }
}

export function clearSession() {
  sessionStorage.removeItem('aerocadet.accessToken')
  sessionStorage.removeItem('aerocadet.user')
}

export const getMyApplications = () => authenticatedRequest('/applications')
export const getNotifications = () => authenticatedRequest('/notifications')
export const getInterviews = () => authenticatedRequest('/interviews')
export const getCandidateProfile = () => authenticatedRequest('/candidates/profile')
export const getRecruiterDashboard = () => authenticatedRequest('/recruiter/dashboard')
export const getAdminAnalytics = () => authenticatedRequest('/analytics/summary')

