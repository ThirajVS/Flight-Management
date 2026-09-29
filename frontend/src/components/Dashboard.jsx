import { useEffect, useMemo, useState } from 'react'
import {
  ArrowRight, BarChart3, Bell, BookOpen, BriefcaseBusiness, CalendarDays,
  CheckCircle2, ChevronRight, ClipboardCheck, ClipboardList, FileCheck2,
  FileText, Gauge, LayoutDashboard, LogOut, Menu, MoreHorizontal, Plane,
  Search, Settings, ShieldCheck, UserRoundCheck, Users, Video,
} from 'lucide-react'
import {
  clearSession, getAdminAnalytics, getCandidateProfile, getInterviews,
  getMyApplications, getNotifications, getRecruiterDashboard,
} from '../lib/api'

const candidateFallback = [
  { applicationNumber: 'AC-2026-00124', programName: 'Airline Cadet Pilot Program', status: 'ASSESSMENT', updatedAt: '2026-09-26' },
  { applicationNumber: 'AC-2026-00141', programName: 'International Flight Training Program', status: 'DOCUMENT_VERIFICATION', updatedAt: '2026-09-24' },
]

const recruiterRows = [
  ['AC-2026-00124', 'Ananya Rao', 'Airline Cadet Pilot Program', 'Assessment', 'Today, 09:20'],
  ['AC-2026-00131', 'Arjun Mehta', 'Integrated Commercial Pilot', 'Document review', 'Today, 08:45'],
  ['AC-2026-00142', 'Maya Nair', 'International Flight Training', 'Interview', 'Yesterday'],
  ['AC-2026-00157', 'Kabir Singh', 'Airline Cadet Pilot Program', 'Shortlisted', '28 Sep'],
]

const timeline = [
  ['Application submitted', 'Completed 18 Sep 2026', true],
  ['Eligibility verified', 'Completed 20 Sep 2026', true],
  ['Documents verified', 'Completed 26 Sep 2026', true],
  ['Aptitude assessment', '12 Oct 2026 · 10:00 IST', false],
  ['Interview', 'Scheduled after assessment', false],
]

function roleFor(user) {
  const roles = user.roles || []
  if (roles.includes('ADMIN')) return 'ADMIN'
  if (roles.includes('RECRUITER')) return 'RECRUITER'
  return 'CANDIDATE'
}

function Sidebar({ role, open, onClose, onSignOut }) {
  const common = [{ icon: LayoutDashboard, label: 'Overview' }]
  const items = role === 'CANDIDATE'
    ? [...common, { icon: BriefcaseBusiness, label: 'Programs' }, { icon: ClipboardList, label: 'My applications' }, { icon: FileText, label: 'Documents' }, { icon: CalendarDays, label: 'Assessments' }, { icon: Bell, label: 'Notifications', badge: 3 }]
    : role === 'RECRUITER'
      ? [...common, { icon: Users, label: 'Applications' }, { icon: FileCheck2, label: 'Document review', badge: 7 }, { icon: CalendarDays, label: 'Interviews' }, { icon: ClipboardCheck, label: 'Selection board' }]
      : [...common, { icon: Users, label: 'Users' }, { icon: BriefcaseBusiness, label: 'Programs' }, { icon: BarChart3, label: 'Analytics' }, { icon: ShieldCheck, label: 'Audit log' }]

  return (
    <aside className={`dashboard-sidebar ${open ? 'open' : ''}`}>
      <button className="sidebar-close" type="button" onClick={onClose}>×</button>
      <div className="dashboard-brand"><span><Plane size={19} /></span> Aero<b>Cadet</b></div>
      <p className="workspace-label">{role.toLowerCase()} workspace</p>
      <nav>
        {items.map(({ icon: Icon, label, badge }, index) => (
          <button className={index === 0 ? 'active' : ''} type="button" key={label}>
            <Icon size={18} /><span>{label}</span>{badge && <em>{badge}</em>}
          </button>
        ))}
      </nav>
      <div className="sidebar-bottom">
        <button type="button"><Settings size={18} /> Settings</button>
        <button type="button" onClick={onSignOut}><LogOut size={18} /> Sign out</button>
        <small>Educational demo<br />Synthetic data only</small>
      </div>
    </aside>
  )
}

function StatCard({ icon: Icon, label, value, note, tone = 'blue' }) {
  return <article className="dash-stat"><span className={`stat-icon ${tone}`}><Icon size={20} /></span><div><p>{label}</p><strong>{value}</strong><small>{note}</small></div></article>
}

function CandidateOverview({ user, data }) {
  const applications = data.applications.length ? data.applications : candidateFallback
  const profileScore = data.profile?.completionPercentage || 72
  return <>
    <section className="dash-welcome candidate-welcome">
      <div><p>GOOD AFTERNOON, CADET</p><h1>Ready for your next milestone, {user.fullName?.split(' ')[0]}?</h1><span>Your aviation journey is moving forward. One action is ready for you.</span></div>
      <button type="button">Continue application <ArrowRight size={17} /></button>
    </section>
    <div className="dash-stats">
      <StatCard icon={Gauge} label="Profile completion" value={`${profileScore}%`} note="Add education details" tone="blue" />
      <StatCard icon={ClipboardList} label="Active applications" value={applications.length} note="1 action required" tone="violet" />
      <StatCard icon={FileCheck2} label="Verified documents" value="4 / 6" note="2 pending review" tone="green" />
      <StatCard icon={CalendarDays} label="Next milestone" value="12 Oct" note="Aptitude assessment" tone="amber" />
    </div>
    <div className="dashboard-grid candidate-grid">
      <section className="dash-card application-focus">
        <div className="card-heading"><div><p>PRIMARY APPLICATION</p><h2>Airline Cadet Pilot Program</h2></div><span className="dash-pill blue">ON TRACK</span></div>
        <div className="journey-progress"><span style={{ width: '56%' }} /></div>
        <div className="journey-label"><span>Application received</span><b>4 of 7 stages</b><span>Final decision</span></div>
        <div className="stage-list">{timeline.map(([title, detail, complete]) => <div className={complete ? 'complete' : 'future'} key={title}><span>{complete ? <CheckCircle2 size={16} /> : <span className="stage-dot" />}</span><div><b>{title}</b><small>{detail}</small></div>{!complete && title === 'Aptitude assessment' && <button type="button">View details</button>}</div>)}</div>
      </section>
      <aside className="dash-card action-card"><p>NEXT ACTION</p><span className="action-icon"><BookOpen /></span><h2>Aptitude assessment</h2><span>Complete the online assessment before the deadline.</span><div><CalendarDays size={16} /> 12 Oct 2026 · 10:00 IST</div><button type="button">Prepare now <ChevronRight size={17} /></button></aside>
    </div>
    <section className="dash-card table-card"><div className="card-heading"><div><p>APPLICATION PORTFOLIO</p><h2>My applications</h2></div><button className="link-button" type="button">View all <ArrowRight size={15} /></button></div><div className="dash-table"><div className="table-row head"><span>Reference</span><span>Program</span><span>Status</span><span>Last updated</span><span /></div>{applications.map((app) => <div className="table-row" key={app.applicationNumber}><b>{app.applicationNumber}</b><span>{app.programName}</span><span><em className="status-tag">{app.status?.replaceAll('_', ' ')}</em></span><span>{new Date(app.updatedAt).toLocaleDateString('en-GB', { day: '2-digit', month: 'short', year: 'numeric' })}</span><ChevronRight size={16} /></div>)}</div></section>
  </>
}

function RecruiterOverview({ data }) {
  const stats = data.stats || { newApplications: 14, pendingDocuments: 7, shortlisted: 12, upcomingInterviews: 5 }
  return <>
    <section className="dash-welcome"><div><p>RECRUITMENT OPERATIONS</p><h1>Today’s selection workspace</h1><span>Review applications, clear documents and keep interviews moving.</span></div><button type="button">Export summary <ArrowRight size={17} /></button></section>
    <div className="dash-stats"><StatCard icon={ClipboardList} label="New applications" value={stats.newApplications} note="+8 since yesterday" /><StatCard icon={FileCheck2} label="Pending documents" value={stats.pendingDocuments} note="Requires review" tone="amber" /><StatCard icon={UserRoundCheck} label="Shortlisted" value={stats.shortlisted} note="Across 6 programs" tone="green" /><StatCard icon={Video} label="Upcoming interviews" value={stats.upcomingInterviews} note="Next 7 days" tone="violet" /></div>
    <div className="dashboard-grid recruiter-grid"><section className="dash-card table-card"><div className="card-heading"><div><p>APPLICATION QUEUE</p><h2>Candidates requiring attention</h2></div><div className="table-tools"><button type="button"><Search size={16} /></button><button type="button">All programs <ChevronRight size={14} /></button></div></div><div className="dash-table recruiter-table"><div className="table-row head"><span>Reference</span><span>Candidate</span><span>Program</span><span>Stage</span><span>Updated</span></div>{recruiterRows.map((row) => <div className="table-row" key={row[0]}>{row.map((cell, i) => i === 3 ? <span key={cell}><em className="status-tag">{cell}</em></span> : i === 0 ? <b key={cell}>{cell}</b> : <span key={cell}>{cell}</span>)}</div>)}</div></section><aside className="dash-card workload"><div className="card-heading"><div><p>TEAM WORKLOAD</p><h2>Review pipeline</h2></div><MoreHorizontal size={19} /></div>{[['Document verification', 7, 72], ['Eligibility review', 5, 54], ['Assessment results', 3, 36], ['Interview feedback', 2, 24]].map(([name, count, width]) => <div className="workload-row" key={name}><div><span>{name}</span><b>{count}</b></div><div><span style={{ width: `${width}%` }} /></div></div>)}<button type="button">Open team queue <ArrowRight size={16} /></button></aside></div>
  </>
}

function AdminOverview({ data }) {
  const analytics = data.analytics || { totalCandidates: 120, activePrograms: 10, totalApplications: 30, shortlisted: 12, upcomingInterviews: 8, finalSelected: 4, applicationsByProgram: { 'Airline Cadet Pilot': 11, 'Integrated Commercial Pilot': 8, 'International Flight Training': 6, 'Other programs': 5 }, candidateFunnel: { Registered: 120, Applied: 30, Eligible: 24, Shortlisted: 12, Assessment: 10, Interview: 8, Selected: 4 } }
  const max = Math.max(...Object.values(analytics.applicationsByProgram || { total: 1 }))
  return <>
    <section className="dash-welcome"><div><p>PLATFORM CONTROL CENTRE</p><h1>Recruitment performance overview</h1><span>Live operational indicators across candidates, programs and selection activity.</span></div><button type="button">Generate report <ArrowRight size={17} /></button></section>
    <div className="dash-stats"><StatCard icon={Users} label="Registered candidates" value={analytics.totalCandidates} note="Synthetic profiles" /><StatCard icon={BriefcaseBusiness} label="Active programs" value={analytics.activePrograms} note="All demo content" tone="violet" /><StatCard icon={ClipboardList} label="Applications" value={analytics.totalApplications} note="Current intake" tone="green" /><StatCard icon={Video} label="Interviews scheduled" value={analytics.upcomingInterviews} note="Upcoming" tone="amber" /></div>
    <div className="dashboard-grid admin-grid"><section className="dash-card"><div className="card-heading"><div><p>PROGRAM DEMAND</p><h2>Applications by program</h2></div><BarChart3 size={20} /></div><div className="bar-chart">{Object.entries(analytics.applicationsByProgram || {}).map(([label, value]) => <div key={label}><span>{label}</span><div><i style={{ width: `${Math.max(10, (value / max) * 100)}%` }} /></div><b>{value}</b></div>)}</div></section><section className="dash-card"><div className="card-heading"><div><p>CANDIDATE FUNNEL</p><h2>Selection conversion</h2></div><span className="dash-pill green">LIVE</span></div><div className="funnel">{Object.entries(analytics.candidateFunnel || {}).map(([label, value], index) => <div key={label} style={{ width: `${100 - index * 8}%` }}><span>{label}</span><b>{value}</b></div>)}</div></section></div>
    <section className="dash-card governance"><div className="card-heading"><div><p>GOVERNANCE</p><h2>Platform safeguards</h2></div></div><div><span><ShieldCheck /> Role-based access</span><b>Healthy</b></div><div><span><FileCheck2 /> Audit trail coverage</span><b>100%</b></div><div><span><Gauge /> Service availability</span><b>Operational</b></div><div><span><CheckCircle2 /> Synthetic-data policy</span><b>Enforced</b></div></section>
  </>
}

export default function Dashboard({ user, onSignedOut }) {
  const role = useMemo(() => roleFor(user), [user])
  const [menuOpen, setMenuOpen] = useState(false)
  const [data, setData] = useState({ applications: [], notifications: [], interviews: [] })

  useEffect(() => {
    let active = true
    async function load() {
      try {
        if (role === 'CANDIDATE') {
          const [applications, notifications, interviews, profile] = await Promise.all([getMyApplications(), getNotifications(), getInterviews(), getCandidateProfile()])
          if (active) setData({ applications, notifications, interviews, profile })
        } else if (role === 'RECRUITER') {
          const stats = await getRecruiterDashboard()
          if (active) setData({ stats })
        } else {
          const analytics = await getAdminAnalytics()
          if (active) setData({ analytics })
        }
      } catch {
        // Dashboards retain representative synthetic data while the API starts.
      }
    }
    load()
    return () => { active = false }
  }, [role])

  function signOut() { clearSession(); onSignedOut() }

  return <main className="dashboard-shell"><Sidebar role={role} open={menuOpen} onClose={() => setMenuOpen(false)} onSignOut={signOut} /><div className="dashboard-main"><header className="dashboard-topbar"><button className="dash-menu" type="button" onClick={() => setMenuOpen(true)}><Menu /></button><div className="dash-search"><Search size={16} /><span>Search candidates, programs, applications…</span></div><div className="topbar-actions"><button type="button" aria-label="Notifications"><Bell size={18} /><i /></button><div className="user-avatar">{user.fullName?.split(' ').map((part) => part[0]).slice(0, 2).join('')}</div><div><b>{user.fullName}</b><span>{role.toLowerCase()}</span></div></div></header><div className="dashboard-content">{role === 'CANDIDATE' ? <CandidateOverview user={user} data={data} /> : role === 'RECRUITER' ? <RecruiterOverview data={data} /> : <AdminOverview data={data} />}</div></div></main>
}
