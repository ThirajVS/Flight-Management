# Docker deployment

## Architecture

The Compose project contains four isolated services on the `aerocadet` bridge network:

```text
Browser :8090 -> Nginx gateway :80 -> Frontend Nginx :80
                                  -> Spring Boot :8080 -> PostgreSQL :5432
```

Host ports 8090, 8081, and 5433 avoid the workstation's Jenkins service on 8080. Only the gateway is required for normal browser use. Named volumes persist PostgreSQL data and uploaded dummy documents.

## Images

- `backend/Dockerfile` uses a Maven dependency/build stage and a non-root Alpine JRE runtime.
- `backend/Dockerfile.standard` deliberately keeps Maven, a full JDK, source, and build output in the final image for a meaningful baseline.
- `frontend/Dockerfile` builds Vite assets in Node and copies only `dist` into Nginx.
- `nginx/Dockerfile` packages the reverse-proxy configuration and health probe.

The optimized backend is expected to be substantially smaller because build tools, downloaded Maven dependencies, and source files do not enter its runtime layers.

Verified local image sizes were **1.07 GB** for `aerocadet-backend:standard` and **487 MB** for `aerocadet-backend:latest`, a reduction of roughly 54%.

## PowerShell entry point

```powershell
Set-Location C:\Users\thira\Flight_Management
wsl.exe -d Ubuntu -e docker compose --env-file /mnt/c/Users/thira/Flight_Management/.env `
  -f /mnt/c/Users/thira/Flight_Management/docker-compose.yml up -d --build
Invoke-RestMethod http://localhost:8090/actuator/health
```

## Ubuntu terminal lifecycle commands

```bash
cd /mnt/c/Users/thira/Flight_Management
docker compose --env-file .env pull
docker compose --env-file .env build
docker compose --env-file .env up -d
docker compose --env-file .env ps
docker compose --env-file .env logs --tail=100 backend
docker inspect aerocadet-backend-1
docker exec aerocadet-postgres-1 psql -U aerocadet -d aerocadet -c 'select count(*) from users;'
docker compose --env-file .env stop backend
docker compose --env-file .env start backend
docker compose --env-file .env restart frontend
docker compose --env-file .env rm -f postgres
docker compose --env-file .env up -d postgres
docker compose --env-file .env down
```

`down` intentionally retains named volumes. Use `down -v` only when the lab database should be permanently erased.

## Verified deployment

The application was built and launched through Docker Compose. PostgreSQL migrations reached version 4, the demo seeder created 20 candidates, five recruiters, three administrators and 30 applications, all four services became healthy, `/actuator/health` returned `UP`, and candidate/recruiter/admin dashboards were verified in the browser through port 8090.

Volume persistence was verified by counting 28 user rows, stopping the backend and PostgreSQL, removing only `aerocadet-postgres-1`, recreating it from Compose, and querying again. The count remained 28 because `aerocadet_postgres-data` survived the container lifecycle.
