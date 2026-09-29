# AeroCadet

> Flight/Cadet Application Management System

AeroCadet is a full-stack, aviation-themed cadet recruitment and application portal built as a complete DevOps laboratory project. It combines a real React interface, Java/Spring Boot REST API, PostgreSQL data model, automated tests, Jenkins CI, Docker Compose deployment, Nginx routing, and Ansible convergence.

All candidates, programs, documents, scores, interviews, and organisations are fictional demonstration data. AeroCadet is educational software, not production-certified aviation, medical, or recruitment software.

## Project status

| Area | Verified result |
|---|---|
| Application | Landing page plus candidate, recruiter, and admin workspaces render through Nginx |
| Backend tests | 13 passed |
| Frontend tests | 6 passed |
| Jenkins Freestyle | `AeroCadet-Freestyle #1` — SUCCESS |
| Jenkins Pipeline | `#4` real failure gate; `#5` corrected SUCCESS |
| Docker | Four services healthy |
| Persistence | 28 users remained after PostgreSQL container recreation |
| Ansible | `ok=14 changed=1 failed=0`; second run `changed=0` |
| Health | `http://localhost:8090/actuator/health` returns `UP` |
| GitHub webhook | Not claimed: Jenkins is localhost-only; Poll SCM is active |

## Features

- Candidate registration, login/logout, BCrypt password hashes, expiring JWTs, protected routes, and role authorization.
- Detailed personal, academic, aviation, passport, medical-document-status, and preference profiles.
- Ten searchable and paginated fictional cadet programs with requirements and selection stages.
- Rule-by-rule eligibility evaluation with eligible, not eligible, and pending verification outcomes.
- Seven-step draft/application workflow, unique application IDs, submission, and status history.
- Dummy-document upload metadata plus recruiter verification, rejection, and re-upload states.
- Application, eligibility, aptitude, technical, interview, medical, and final-selection timeline.
- Synthetic aptitude questions, timed attempts, scores, and qualification results.
- Interview scheduling with date, mode, location/link, interviewer, remarks, and status.
- Internal notifications, unread state, recruiter work queues, admin analytics, and audit logs.
- Polished responsive aviation UI with role-specific dashboards, tables, cards, funnels, timelines, filters, and mobile layouts.

## Architecture

```text
Developer
   |
Git feature/devops branches -> GitHub
   |                            |
   |                     Poll SCM / webhook-ready
   v                            v
develop -> main         Jenkins Freestyle + Pipeline
                              |
                       Build -> 19 tests -> JUnit
                              |
                 Docker build/deploy (capable agent)
                              |
                         Ansible convergence
                              |
Browser :8090 -> Nginx :80 -> React frontend :80
                              -> Spring Boot :8080 -> PostgreSQL :5432
```

Host lab ports are 8090 (gateway), 8081 (backend), and 5433 (PostgreSQL). Jenkins remains on 8080.

## Technology stack

| Layer | Technology |
|---|---|
| Frontend | React, Vite, JavaScript, HTML5, CSS3, Lucide icons |
| Backend | Java 17 target, Spring Boot, Spring Web, JPA, Security, JWT, Maven |
| Database | PostgreSQL 16 in Docker; H2 for isolated integration tests; Flyway migrations |
| Testing | JUnit 5, MockMvc, Spring Boot Test, Vitest, Testing Library, JUnit XML |
| Delivery | Git, GitHub, Jenkins 2.573, Docker, Compose, Nginx, Ansible Core 2.20.1 |

## User roles

### Candidate

Register/login, complete profile, browse programs, check eligibility, create/save/submit applications, upload dummy documents, view notifications, assessments, interviews, and track the selection timeline.

### Recruiter / Officer

Review applications, filter action queues, verify documents, shortlist/reject with remarks, update stages, and schedule assessments/interviews.

### Administrator

View candidates, recruiters, programs, applications, system analytics, status distribution, candidate funnel, and audit records.

## Data architecture

Flyway migrations create relational tables for users/roles, candidate profiles, programs/rules, applications/history, documents, eligibility results, selection stages, assessments/questions/attempts, interviews, notifications, and audit logs. Foreign keys protect relationships; PostgreSQL and upload data live in named Docker volumes.

Synthetic startup data contains 20 candidates, 5 recruiters, 3 admins, 10 programs, 30 applications, documents, assessments, interviews, notifications, and audits. Demo seeding is controlled by `DEMO_DATA_ENABLED`.

## Main REST APIs

| Method and path | Purpose |
|---|---|
| `POST /api/auth/register` | Register candidate and profile |
| `POST /api/auth/login` | Authenticate and return JWT |
| `GET /api/auth/me` | Current authenticated identity/roles |
| `GET /api/programs` | Search/filter/paginate programs |
| `GET /api/programs/{id}` | Program details and requirements |
| `GET, PUT /api/candidates/profile` | Read/update candidate profile |
| `POST /api/programs/{id}/eligibility` | Calculate and persist eligibility |
| `POST, GET /api/applications` | Create/list applications |
| `PUT /api/applications/{id}/draft` | Save a seven-step draft |
| `POST /api/applications/{id}/submit` | Submit application |
| `GET, PUT /api/applications/{id}/status` | Track/update status |
| `POST, GET /api/applications/{id}/documents` | Upload/list document metadata |
| `PUT /api/documents/{id}/review` | Verify/reject/request re-upload |
| `POST, GET /api/interviews` | Schedule/list interviews |
| `GET /api/analytics/summary` | Admin analytics |
| `GET /api/notifications` | Candidate notifications |
| `GET /api/audit-logs` | Authorized audit view |
| `GET /actuator/health` | Container/deployment health |

Errors use consistent JSON and correct 400/401/403/404/409/500 status codes without exposing stack traces.

## Project structure

```text
Flight_Management/
├── backend/                  Spring Boot API, migrations, and JUnit tests
├── frontend/                 React/Vite UI and Vitest tests
├── nginx/                    Production reverse-proxy image/config
├── ansible/                  Inventory, playbook, variables, and three roles
├── scripts/                  Jenkins, validation, start/status/stop helpers
├── docs/                     Thirteen experiment reports and evidence logs
├── screenshots/              Thirteen experiment evidence directories
├── Jenkinsfile               Declarative source-controlled Pipeline
├── docker-compose.yml        Four-service deployment
├── .env.example              Safe placeholder configuration
└── README.md
```

## Environment configuration

Never commit `.env`. For a fresh clone:

```powershell
Copy-Item .env.example .env
notepad .env
```

Replace `DATABASE_PASSWORD`, `JWT_SECRET`, and `DEMO_ACCOUNT_PASSWORD` before starting. `JWT_SECRET` must be at least 32 characters. The demo password stays only in the ignored `.env`; keep demo data enabled only for the lab.

## Start AeroCadet after a laptop restart

Open **PowerShell** and run:

```powershell
Set-Location C:\Users\thira\Flight_Management
.\scripts\start-aerocadet.ps1
```

The helper starts Ubuntu WSL, starts its Docker service if necessary, starts/reuses all four containers, waits for health, and prints the URL. Then open:

- AeroCadet: `http://localhost:8090`
- Jenkins: `http://localhost:8080`

Useful commands:

```powershell
.\scripts\status-aerocadet.ps1       # containers and backend health
.\scripts\start-aerocadet.ps1 -Build # rebuild after source/Dockerfile changes
.\scripts\stop-aerocadet.ps1         # stop containers; preserve data volumes
```

All services use `restart: unless-stopped`, so they recover when the Docker daemon starts. The start helper is the reliable one-command option after shutdown.

## Development and tests

### Backend — PowerShell

```powershell
Set-Location C:\Users\thira\Flight_Management\backend
.\mvnw.cmd -B test
```

### Frontend — PowerShell

```powershell
Set-Location C:\Users\thira\Flight_Management\frontend
npm ci
npm run test:ci
npm run build
npm run dev
```

## Docker

The optimized backend image uses a Maven build stage and non-root Alpine JRE runtime. A deliberately heavy standard image is retained for Experiment 8. Verified sizes: 1.07 GB standard versus 487 MB optimized (about 54% smaller).

```powershell
.\scripts\start-aerocadet.ps1 -Build
.\scripts\status-aerocadet.ps1
```

Named volumes `aerocadet_postgres-data` and `aerocadet_uploads` persist the database and dummy uploads. The stop helper never removes them.

## Jenkins

- `AeroCadet-Freestyle` checks out `develop`, polls every five minutes, executes `scripts/jenkins-freestyle.ps1`, and publishes backend/frontend JUnit XML.
- `AeroCadet-Pipeline` loads `Jenkinsfile` from `develop`, builds both applications, runs tests in parallel, publishes tests/artifacts, and contains real Docker/Compose/Ansible/health stages.
- `DEPLOY=false` by default because the local Jenkins Windows service is `SYSTEM` and cannot use the interactive user's WSL-only Docker engine. Enable it on a Docker-capable agent.

Failure proof is preserved: build `#4` failed a deliberately incorrect frontend assertion and skipped every deployment stage; commit `cacf932` restored the test and build `#5` succeeded.

GitHub cannot call `localhost:8080`; webhook success is therefore not claimed. Poll SCM is working. See `docs/experiment-06-webhook.md` for the exact public-endpoint procedure.

## Ansible

```bash
cd /mnt/c/Users/thira/Flight_Management
export ANSIBLE_CONFIG="$PWD/ansible/ansible.cfg"
ansible-playbook -i ansible/inventory ansible/playbook.yml --syntax-check
ansible-playbook -i ansible/inventory ansible/playbook.yml
```

The Docker role checks/provisions the engine, the Nginx role validates gateway configuration, and the application role converges Compose, waits for health, reports containers, and verifies public/backend endpoints. The second verified run was idempotent (`changed=0`).

## Git workflow

Development follows `feature/*` and `devops/*` branches into `develop`, then `develop` into `main`. Required published branches include authentication, programs, application, documents, selection, analytics, Docker, Jenkins, and Ansible.

Experiment 3 contains a real controlled conflict between `lab/conflict-jenkins` and `lab/conflict-ansible`, resolved in merge commit `eafb21d` by retaining both contributions.

## Validation

```powershell
Set-Location C:\Users\thira\Flight_Management
.\scripts\validate-project.ps1
```

The validator checks required files/configuration, all frontend and backend tests, container state, frontend/backend/database reachability, and health.

## Experiment mapping

| Experiment | Report |
|---:|---|
| 1 | [Git and GitHub](docs/experiment-01-git.md) |
| 2 | [Fetch, pull, and synchronization](docs/experiment-02-sync.md) |
| 3 | [Branching and conflict resolution](docs/experiment-03-branching.md) |
| 4 | [Jenkins Freestyle](docs/experiment-04-jenkins-freestyle.md) |
| 5 | [Jenkins Pipeline](docs/experiment-05-jenkins-pipeline.md) |
| 6 | [GitHub webhook](docs/experiment-06-webhook.md) |
| 7 | [Docker lifecycle](docs/experiment-07-docker.md) |
| 8 | [Dockerfiles/image optimization](docs/experiment-08-dockerfiles.md) |
| 9 | [Docker deployment](docs/experiment-09-docker-deployment.md) |
| 10 | [Continuous testing](docs/experiment-10-continuous-testing.md) |
| 11 | [Ansible/Nginx](docs/experiment-11-ansible.md) |
| 12 | [Ansible Docker management](docs/experiment-12-ansible-docker.md) |
| 13 | [Integrated DevOps](docs/experiment-13-integrated-devops.md) |

Also see [command log](docs/command-log.md), [screenshot manifest](docs/screenshot-manifest.md), [Docker evidence](docs/docker-deployment.md), [Jenkins evidence](docs/jenkins-ci-cd.md), and [Ansible evidence](docs/ansible-deployment.md).

## Troubleshooting

- `WSL/Service/E_ACCESSDENIED` inside an automated sandbox: run the PowerShell helper normally as the signed-in Windows user.
- Port 8090 unavailable: change `APP_HOST_PORT` in the untracked `.env` and update `CORS_ALLOWED_ORIGINS`.
- Backend unhealthy: run `.\scripts\status-aerocadet.ps1`, then inspect `docker logs aerocadet-backend-1` in Ubuntu.
- Jenkins deployment stage cannot find Docker: keep `DEPLOY=false` on the built-in Windows service or attach a Docker-capable Jenkins agent.
- Empty/fresh database: ensure `DEMO_DATA_ENABLED=true` for this educational lab and that the PostgreSQL volume is healthy.
- Never run `docker compose down -v` unless permanent deletion of the lab database/uploads is intentional.

## Final result

AeroCadet is a working, restartable aviation recruitment demonstration with verified authentication, workflows, role dashboards, tests, Jenkins CI behavior, Docker deployment/persistence, Ansible convergence/idempotence, and explicit evidence boundaries for the localhost-only GitHub webhook.
