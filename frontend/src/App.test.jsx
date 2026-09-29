import { cleanup, fireEvent, render, screen } from '@testing-library/react'
import { afterEach, beforeEach, describe, expect, it } from 'vitest'
import App from './App'

afterEach(cleanup)
beforeEach(() => sessionStorage.clear())

describe('AeroCadet landing page', () => {
  it('presents the aviation recruitment value proposition', () => {
    render(<App />)

    expect(screen.getByRole('heading', { name: /your flight career starts here/i })).toBeInTheDocument()
    expect(screen.getByRole('button', { name: /explore programs/i })).toBeInTheDocument()
    expect(screen.getByText(/synthetic demo data/i)).toBeInTheDocument()
  })

  it('labels every featured program as demonstration content', () => {
    render(<App />)

    expect(
      screen.getAllByText((content, element) =>
        element.classList.contains('organisation') && content.includes('· Demo'),
      ),
    ).toHaveLength(3)
    expect(screen.getByText(/program names, organisations, dates and costs.*fictional/i)).toBeInTheDocument()
  })

  it('opens sign-in and registration forms from the landing page', () => {
    render(<App />)

    fireEvent.click(screen.getByRole('button', { name: /^sign in$/i }))
    expect(screen.getByRole('dialog', { name: /sign in to aerocadet/i })).toBeInTheDocument()
    expect(screen.getByLabelText(/email address/i)).toBeInTheDocument()

    fireEvent.click(screen.getByRole('tab', { name: /register/i }))
    expect(screen.getByRole('dialog', { name: /create candidate account/i })).toBeInTheDocument()
    expect(screen.getByLabelText(/full name/i)).toBeInTheDocument()
    expect(screen.getByLabelText(/date of birth/i)).toBeInTheDocument()
  })
})

describe('role dashboards', () => {
  it('restores a candidate session into the application workspace', () => {
    sessionStorage.setItem('aerocadet.user', JSON.stringify({ fullName: 'Ananya Rao', roles: ['CANDIDATE'] }))
    render(<App />)
    expect(screen.getByRole('heading', { name: /ready for your next milestone/i })).toBeInTheDocument()
    expect(screen.getByRole('heading', { name: /my applications/i })).toBeInTheDocument()
  })

  it('shows recruitment operations for recruiter accounts', () => {
    sessionStorage.setItem('aerocadet.user', JSON.stringify({ fullName: 'Rohan Recruiter', roles: ['RECRUITER'] }))
    render(<App />)
    expect(screen.getByRole('heading', { name: /today’s selection workspace/i })).toBeInTheDocument()
    expect(screen.getByText(/candidates requiring attention/i)).toBeInTheDocument()
  })

  it('shows analytics for administrator accounts', () => {
    sessionStorage.setItem('aerocadet.user', JSON.stringify({ fullName: 'Aditi Admin', roles: ['ADMIN'] }))
    render(<App />)
    expect(screen.getByRole('heading', { name: /recruitment performance overview/i })).toBeInTheDocument()
    expect(screen.getByText(/selection conversion/i)).toBeInTheDocument()
  })
})

