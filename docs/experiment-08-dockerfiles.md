# Experiment 8 — Custom Dockerfiles and Image Optimization

## Aim

Build custom images and compare a meaningful standard Java image with an optimized runtime image.

## Objective

Show how multi-stage builds and a JRE-only runtime reduce production image size.

## Requirements

`backend/Dockerfile`, `backend/Dockerfile.standard`, `frontend/Dockerfile`, `nginx/Dockerfile`, and Docker BuildKit.

## Commands

```bash
docker build -f backend/Dockerfile.standard -t aerocadet-backend:standard backend
docker build -f backend/Dockerfile -t aerocadet-backend:latest backend
docker images aerocadet-backend
docker history aerocadet-backend:standard
docker history aerocadet-backend:latest
```

## Configuration and implementation

The standard backend deliberately retains Maven, the full JDK, source, dependencies, and output. The optimized Dockerfile builds with Maven and copies only the packaged JAR into a non-root Alpine JRE runtime.

## Execution and output

`aerocadet-backend:standard` measured 1.07 GB; `aerocadet-backend:latest` measured 487 MB. The optimized image is approximately 54% smaller while running the same Spring Boot JAR.

## Screenshots

Use `screenshots/08_dockerfiles/` for Dockerfiles, builds, image list, histories, and running container.

## Result

Pass — custom images built and a meaningful size reduction was measured.
