# AeroCadet

**Flight/Cadet Application Management System**

AeroCadet is an educational, full-stack aviation cadet recruitment portal built to demonstrate a complete Git → GitHub → Jenkins → Testing → Docker → Ansible deployment lifecycle. All programs and candidate records in this repository are fictional demonstration data.

## Implemented increments

The tested foundation and authentication increments provide:

- a Java 17-targeted Spring Boot backend with public status and Actuator health endpoints;
- candidate registration with complete contact and location fields;
- BCrypt password hashing, JWT expiry/validation, and stateless Spring Security;
- candidate/recruiter/admin role foundations and protected identity lookup;
- consistent validation, authentication, authorization, and conflict errors;
- real JUnit integration tests and Maven test reports;
- a responsive React/Vite aviation landing page;
- polished sign-in and candidate-registration interfaces wired to the API;
- secure environment-variable templates and secret-safe Git exclusions;
- initial environment, command, and screenshot documentation.

Later increments add role-based dashboards, cadet programs, eligibility, applications, documents, selection workflows, analytics, Docker Compose, Jenkins, and Ansible.

## Technology stack

| Layer | Technology |
|---|---|
| Frontend | React, Vite, JavaScript, CSS |
| Backend | Java 17 target, Spring Boot, Maven |
| Testing | JUnit 5, MockMvc, Vitest, Testing Library |
| Database | PostgreSQL (Docker runtime), H2 (isolated tests) |
| DevOps | Git, GitHub, Jenkins, Docker Compose, Ansible, Nginx |

## Project structure

```text
Flight_Management/
├── backend/               Spring Boot API
├── frontend/              React/Vite interface
├── docs/                  Experiment evidence and reports
├── scripts/               Validation helpers
├── screenshots/           Evidence grouped by experiment
├── .env.example           Safe configuration template
└── README.md
```

## Available endpoints

- `GET /api/public/status` — public application identity and service status
- `GET /actuator/health` — deployment health probe
- `POST /api/auth/register` — create a candidate account and profile
- `POST /api/auth/login` — authenticate and receive an expiring bearer token
- `GET /api/auth/me` — retrieve the authenticated user and roles

## Local verification

The checked machine has no Maven installation on the Windows path. The backend is therefore built in the pinned Maven/Java 17 container until Jenkins tooling is configured.

**UBUNTU TERMINAL (WSL)**

```bash
cd /mnt/c/Users/thira/Flight_Management
docker run --rm -v "$PWD/backend:/workspace" -w /workspace maven:3.9.9-eclipse-temurin-17 mvn test
```

**POWERSHELL**

```powershell
Set-Location C:\Users\thira\Flight_Management\frontend
npm install
npm run test:ci
npm run build
```

Do not copy `.env.example` values into a shared environment unchanged. Create a local `.env`, replace all placeholder secrets, and keep that file untracked.

## Documentation

- [Environment check](docs/environment-check.md)
- [Command log](docs/command-log.md)
- [Screenshot manifest](docs/screenshot-manifest.md)
- [Authentication design](docs/authentication.md)

## Safety and scope

This is an educational recruitment workflow demonstration. It is not production-certified aviation software and does not make medical or employment decisions. Only synthetic data and dummy documents belong in the project.

