import { useEffect, useState } from 'react'
import {
  ArrowRight,
  BadgeCheck,
  CalendarDays,
  CheckCircle2,
  ChevronRight,
  Clock3,
  FileCheck2,
  GraduationCap,
  MapPin,
  Menu,
  Plane,
  ShieldCheck,
  Sparkles,
} from 'lucide-react'
import AuthModal from './components/AuthModal'
import { getPrograms } from './lib/api'

const fallbackPrograms = [
  {
    code: 'ACP-01',
    title: 'Airline Cadet Pilot Program',
    organisation: 'AeroVista Training Academy · Demo',
    location: 'Hyderabad, India',
    duration: '18 months',
    deadline: '30 Nov 2026',
    accent: 'sky',
  },
  {
    code: 'ICPP-02',
    title: 'Integrated Commercial Pilot Program',
    organisation: 'BlueWing Flight School · Demo',
    location: 'Bengaluru, India',
    duration: '20 months',
    deadline: '15 Dec 2026',
    accent: 'violet',
  },
  {
    code: 'IFTP-03',
    title: 'International Flight Training Program',
    organisation: 'Horizon Aviation Institute · Demo',
    location: 'Dubai, UAE',
    duration: '22 months',
    deadline: '12 Jan 2027',
    accent: 'cyan',
  },
]

const steps = [
  ['01', 'Discover', 'Explore fictional cadet programs and compare entry requirements.'],
  ['02', 'Check eligibility', 'Get a clear rule-by-rule indication before you apply.'],
  ['03', 'Build your application', 'Complete your profile and securely add dummy documents.'],
  ['04', 'Track every stage', 'Follow verification, assessment and interview milestones.'],
]

function BrandMark() {
  return (
    <span className="brand-mark" aria-hidden="true">
      <Plane size={20} strokeWidth={2.4} />
    </span>
  )
}

function App() {
  const [authMode, setAuthMode] = useState(null)
  const [programs, setPrograms] = useState(fallbackPrograms)

  useEffect(() => {
    let active = true
    async function loadPrograms() {
      try {
        const result = await getPrograms({ status: 'OPEN', size: 3 })
        if (active && result.content?.length) {
          setPrograms(result.content.map((program, index) => ({
            code: program.code,
            title: program.name,
            organisation: program.organisation,
            location: program.trainingLocation,
            duration: `${program.durationMonths} months`,
            deadline: new Date(`${program.applicationDeadline}T00:00:00`).toLocaleDateString('en-GB', {
              day: '2-digit',
              month: 'short',
              year: 'numeric',
            }),
            accent: ['sky', 'violet', 'cyan'][index % 3],
          })))
        }
      } catch {
        // The curated fallback keeps the landing page useful when the API is offline.
      }
    }
    loadPrograms()
    return () => { active = false }
  }, [])

  return (
    <main>
      <header className="site-header">
        <a className="brand" href="#top" aria-label="AeroCadet home">
          <BrandMark />
          <span>Aero<span>Cadet</span></span>
        </a>
        <nav className="desktop-nav" aria-label="Primary navigation">
          <a href="#programs">Programs</a>
          <a href="#journey">How it works</a>
          <a href="#eligibility">Eligibility</a>
          <a href="#about">Why AeroCadet</a>
        </nav>
        <div className="header-actions">
          <button className="text-button" type="button" onClick={() => setAuthMode('login')}>Sign in</button>
          <button className="primary-button compact" type="button" onClick={() => setAuthMode('register')}>
            Create account <ArrowRight size={15} />
          </button>
          <button className="menu-button" type="button" aria-label="Open menu"><Menu /></button>
        </div>
      </header>

      <section className="hero" id="top">
        <div className="hero-grid" aria-hidden="true" />
        <div className="hero-glow hero-glow-one" aria-hidden="true" />
        <div className="hero-glow hero-glow-two" aria-hidden="true" />
        <div className="hero-copy">
          <div className="eyebrow"><Sparkles size={14} /> Applications for the 2026 demo intake are open</div>
          <p className="hero-kicker">FLIGHT / CADET APPLICATION MANAGEMENT</p>
          <h1>Your flight career<br /><span>starts here.</span></h1>
          <p className="hero-lede">
            Discover aviation cadet programs, understand your eligibility and manage every step of your application journey in one place.
          </p>
          <div className="hero-actions">
            <button className="primary-button" type="button">Explore programs <ArrowRight size={18} /></button>
            <button className="secondary-button" type="button">Check my eligibility</button>
          </div>
          <div className="trust-row">
            <span><ShieldCheck size={17} /> Secure applications</span>
            <span><BadgeCheck size={17} /> Clear eligibility</span>
            <span><FileCheck2 size={17} /> Stage-by-stage tracking</span>
          </div>
        </div>

        <div className="flight-panel" aria-label="Example application journey">
          <div className="panel-topline">
            <div>
              <span className="micro-label">ACTIVE APPLICATION</span>
              <strong>AC-2026-00124</strong>
            </div>
            <span className="status-pill">ON TRACK</span>
          </div>
          <div className="route-visual">
            <div className="route-code"><b>BLR</b><small>Bengaluru</small></div>
            <div className="route-line"><span /><Plane size={21} /><span /></div>
            <div className="route-code right"><b>CADET</b><small>Flight deck</small></div>
          </div>
          <div className="progress-heading"><span>Selection journey</span><b>4 of 8 stages</b></div>
          <div className="progress-track"><span /></div>
          <div className="timeline-card active">
            <span className="timeline-icon"><CheckCircle2 size={17} /></span>
            <div><b>Documents verified</b><small>Completed 26 Sep 2026</small></div>
          </div>
          <div className="timeline-card upcoming">
            <span className="timeline-icon"><CalendarDays size={17} /></span>
            <div><b>Aptitude assessment</b><small>12 Oct 2026 · 10:00 IST</small></div>
            <ChevronRight size={18} />
          </div>
          <div className="panel-note"><span className="pulse" /> Your next action is ready</div>
        </div>
      </section>

      <section className="proof-strip" aria-label="Platform highlights">
        <div><strong>10+</strong><span>Demo programs</span></div>
        <div><strong>7</strong><span>Tracked application stages</span></div>
        <div><strong>24/7</strong><span>Application visibility</span></div>
        <div><strong>100%</strong><span>Synthetic demo data</span></div>
      </section>

      <section className="section programs-section" id="programs">
        <div className="section-heading-row">
          <div>
            <p className="section-kicker">EXPLORE OPPORTUNITIES</p>
            <h2>Find a path to the flight deck</h2>
            <p>Compare structured, fictional training pathways created for this educational demonstration.</p>
          </div>
          <a href="#programs">View all programs <ArrowRight size={17} /></a>
        </div>
        <div className="program-grid">
          {programs.map((program) => (
            <article className={`program-card ${program.accent}`} key={program.code}>
              <div className="card-bar"><span>{program.code}</span><span>OPEN</span></div>
              <div className="program-icon"><GraduationCap size={25} /></div>
              <p className="organisation">{program.organisation}</p>
              <h3>{program.title}</h3>
              <div className="program-meta">
                <span><MapPin size={15} /> {program.location}</span>
                <span><Clock3 size={15} /> {program.duration}</span>
                <span><CalendarDays size={15} /> Apply by {program.deadline}</span>
              </div>
              <div className="card-actions">
                <button type="button">View details <ChevronRight size={16} /></button>
                <button type="button" className="ghost">Check eligibility</button>
              </div>
            </article>
          ))}
        </div>
        <p className="demo-disclaimer">Program names, organisations, dates and costs on AeroCadet are fictional demonstration content.</p>
      </section>

      <section className="journey-section" id="journey">
        <div className="section centered">
          <p className="section-kicker">ONE CLEAR JOURNEY</p>
          <h2>From ambition to application</h2>
          <p className="section-intro">A structured workspace replaces scattered forms and uncertain updates.</p>
          <div className="steps-grid">
            {steps.map(([number, title, description], index) => (
              <article className="step" key={number}>
                <div className="step-number">{number}</div>
                {index < steps.length - 1 && <span className="step-connector" />}
                <h3>{title}</h3>
                <p>{description}</p>
              </article>
            ))}
          </div>
        </div>
      </section>

      <section className="section eligibility-section" id="eligibility">
        <div className="eligibility-copy">
          <p className="section-kicker">KNOW BEFORE YOU APPLY</p>
          <h2>Eligibility without the guesswork</h2>
          <p>AeroCadet evaluates transparent academic and administrative rules, while keeping medical decisions with qualified professionals.</p>
          <ul>
            <li><CheckCircle2 /> Age and academic requirements</li>
            <li><CheckCircle2 /> Physics, mathematics and English criteria</li>
            <li><CheckCircle2 /> Passport and document readiness</li>
            <li><CheckCircle2 /> Medical document status—not a fitness decision</li>
          </ul>
          <button className="primary-button dark" type="button">Start eligibility check <ArrowRight size={18} /></button>
        </div>
        <div className="eligibility-result">
          <div className="result-header">
            <span>ELIGIBILITY RESULT</span>
            <ShieldCheck size={24} />
          </div>
          <h3>Eligible with pending verification</h3>
          <div className="result-list">
            {['Age requirement', 'Academic requirement', 'Mathematics', 'Physics', 'English', 'Passport'].map((item) => (
              <div key={item}><CheckCircle2 size={18} /><span>{item}</span><b>Meets</b></div>
            ))}
            <div className="pending"><Clock3 size={18} /><span>Medical document</span><b>Pending</b></div>
          </div>
          <p>This is an administrative pre-check. Final verification is completed by the recruitment team.</p>
        </div>
      </section>

      <section className="cta" id="about">
        <div>
          <BrandMark />
          <p className="section-kicker">READY FOR TAKE-OFF?</p>
          <h2>Build your cadet journey with confidence.</h2>
          <p>Create your candidate profile, explore demo programs and keep every application milestone visible.</p>
        </div>
        <button className="primary-button light" type="button" onClick={() => setAuthMode('register')}>Create your profile <ArrowRight size={18} /></button>
      </section>

      <footer>
        <div className="brand"><BrandMark /><span>Aero<span>Cadet</span></span></div>
        <p>Educational DevOps laboratory project · Synthetic data only · Not affiliated with an airline.</p>
        <span>© 2026 AeroCadet</span>
      </footer>
      {authMode && <AuthModal initialMode={authMode} onClose={() => setAuthMode(null)} />}
    </main>
  )
}

export default App

