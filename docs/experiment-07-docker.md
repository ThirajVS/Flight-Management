# Experiment 7 — Docker Lifecycle

## Aim

Demonstrate the full image and container lifecycle using the AeroCadet stack.

## Objective

Pull, build, run, list, stop, start, restart, inspect, log, execute in, remove, and recreate containers without losing persistent data.

## Requirements

Ubuntu WSL Docker 29.6.2, Compose 5.3.1, and an untracked `.env`.

## Commands

```bash
docker pull postgres:16-alpine
docker compose --env-file .env build
docker compose --env-file .env up -d --wait
docker ps
docker ps -a
docker logs aerocadet-backend-1
docker inspect aerocadet-backend-1
docker exec aerocadet-postgres-1 psql -U aerocadet -d aerocadet -c 'select count(*) from users;'
docker stop aerocadet-backend-1
docker start aerocadet-backend-1
docker restart aerocadet-frontend-1
docker compose --env-file .env rm -f postgres
docker compose --env-file .env up -d postgres
```

## Implementation and execution

All four services are currently healthy. Lifecycle operations were executed against only AeroCadet resources. PostgreSQL had 28 user rows before and after its container was removed and recreated.

## Output

The recreated database attached to `aerocadet_postgres-data`; row count remained 28. Backend and frontend recovered healthy after stop/start/restart.

## Screenshots

Use `screenshots/07_docker/` for each lifecycle command and output.

## Result

Pass — complete lifecycle and volume-safe recreation were verified.
