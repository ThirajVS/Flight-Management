# Experiment 13 — Integrated AeroCadet DevOps Project

## Aim

Integrate Git, GitHub, Jenkins, testing, Docker, Ansible, Nginx, PostgreSQL, and the working AeroCadet application.

## Objective

Demonstrate an evidence-backed lifecycle from source change to tested deployment and browser verification.

## Requirements

All project code/configuration, Jenkins jobs, Ubuntu WSL Docker/Ansible, GitHub repository, and local `.env`.

## Workflow

```text
Developer -> Git branches -> GitHub -> Jenkins checkout/build/test/report
          -> Docker images/Compose -> Ansible convergence -> health checks
          -> Nginx -> React + Spring Boot + PostgreSQL -> AeroCadet browser UI
```

## Implementation

The application provides JWT/BCrypt authentication, role dashboards, candidate profiles, ten fictional programs, eligibility checks, seven-step applications, document review, status/selection stages, assessments, interviews, notifications, analytics, and audits. Four Docker services, named volumes, Jenkinsfile, Freestyle script, and three Ansible roles implement the lab lifecycle.

## Execution and output

- GitHub `develop` contains incremental feature/DevOps merges and a resolved controlled conflict.
- Freestyle `#1`: success, 19 tests.
- Pipeline `#4`: intentional test failure and deployment gate.
- Pipeline `#5`: corrected success with JUnit/artifacts.
- Pipeline `#6`: final develop completion commit `17d30db`, 19 tests with no failures, and published artifacts.
- Docker: four healthy services and persistent 28-user database.
- Ansible: `ok=14 changed=1 failed=0`, then idempotent `changed=0`.
- Browser: landing page and candidate/recruiter/admin dashboards verified at `http://localhost:8090`.
- Webhook: not claimed; five-minute Poll SCM is active until a public endpoint is supplied.

## Screenshots

Use `screenshots/13_integrated_project/` for Git commit/push, Jenkins results, Docker/Ansible health, landing page, and each role dashboard. Webhook frames remain manual-only.

## Result

Pass with documented webhook constraint — the complete local DevOps/application workflow is real, tested, and restartable.
