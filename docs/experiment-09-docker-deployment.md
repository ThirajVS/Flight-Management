# Experiment 9 — Java Web Application Deployment in Docker

## Aim

Deploy the complete React, Spring Boot, PostgreSQL, and Nginx application with Compose.

## Objective

Verify Java backend execution, port mapping, networking, named volumes, migrations, health, persistence, and browser access.

## Requirements

Docker Compose, `.env`, ports 8090/8081/5433, and the four project Dockerfiles/images.

## Commands

```powershell
.\scripts\start-aerocadet.ps1 -Build
.\scripts\status-aerocadet.ps1
Invoke-RestMethod http://localhost:8090/actuator/health
```

## Configuration and implementation

Nginx on host port 8090 serves the frontend and proxies `/api` plus `/actuator` to Spring Boot. Backend port 8081 and PostgreSQL port 5433 are exposed for lab inspection. Services communicate on the `aerocadet` bridge; PostgreSQL and dummy uploads use named volumes.

## Execution and output

Compose built and started all four services. Flyway applied four migrations, synthetic seed data created 28 users and 30 applications, health returned `UP`, and candidate/recruiter/admin dashboards rendered through Nginx. Removing/recreating PostgreSQL retained 28 users.

## Screenshots

Use `screenshots/09_docker_deployment/` for Compose file, startup, `ps`, port mapping, volume, backend logs, database query, and browser deployment.

## Result

Pass — the Java application runs inside Docker with durable PostgreSQL storage.
