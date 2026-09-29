import { cleanup, fireEvent, render, screen } from '@testing-library/react'
import { afterEach, describe, expect, it } from 'vitest'
import App from './App'

afterEach(cleanup)

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

