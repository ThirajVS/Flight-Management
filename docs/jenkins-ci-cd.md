# Jenkins CI/CD

## Verified jobs

### AeroCadet-Freestyle

The job uses the public GitHub repository, branch `*/develop`, and Poll SCM schedule `H/5 * * * *`. Its Windows build step is:

```powershell
powershell.exe -NoProfile -ExecutionPolicy Bypass -File scripts/jenkins-freestyle.ps1
```

The script runs the Maven-wrapper backend tests, installs locked frontend dependencies, runs Vitest with JUnit output, and produces the Vite build. The JUnit publisher uses:

```text
backend/target/surefire-reports/*.xml, frontend/reports/junit.xml
```

Build `#1` checked out commit `54b020b`, passed 13 backend and 6 frontend tests, produced the frontend bundle, published results, and finished `SUCCESS`.

### AeroCadet-Pipeline

The job loads `Jenkinsfile` from SCM using the same repository, branch `*/develop`, and five-minute polling schedule. Stages are Checkout, Backend Build, Frontend Build, parallel Automated Testing, Test Report, Docker Build, Compose Validation, Deployment, optional Ansible Convergence, and Health Check. All deployment stages depend on successful tests.

Verified build history:

| Build | Result | Evidence |
|---:|---|---|
| `#1` | Failed | Exposed and led to correction of a Groovy Windows-path quoting defect. |
| `#2` | Failed | Builds/tests passed; Docker stage proved the Windows `SYSTEM` service cannot access the interactive user's WSL-only engine. |
| `#3` | Success | `DEPLOY=false`; all 19 tests passed, JUnit results and artifacts were published. |
| `#4` | Failed by design | Branch `ci/failure-demo`, commit `68f7aa0`; 1 frontend test failed, 5 passed, backend 13/13 passed, and every downstream deployment stage was skipped. |
| `#5` | Success | Commit `cacf932` restored the assertion; all 19 tests passed and the pipeline recovered. |
| `#6` | Success | Final develop commit `17d30db`; all 19 tests passed with no failures and artifacts were published. |

The job was restored to `*/develop` after the controlled failure/recovery exercise.

## Agent-aware deployment

`DEPLOY` defaults to false because this workstation's Jenkins service runs as Windows `SYSTEM`, while Docker is available only through the signed-in user's Ubuntu WSL engine. This keeps the local CI path reliable without pretending that the service account can deploy. On a Docker-capable agent, enable `DEPLOY` to run Docker Build, Compose Validation, Deployment, and Health Check on isolated ports 18090, 18081, and 15433. Enable `RUN_ANSIBLE` only when Ansible is also available to that agent.

The pipeline creates ephemeral `.env.jenkins` credentials at run time, including the database, JWT, and demo-account values, and `deleteDir()` removes the workspace afterward. No operational credential is committed.

## GitHub trigger boundary

Jenkins listens on `localhost:8080`; GitHub cannot deliver to a loopback endpoint. Webhook success is not claimed. Poll SCM is the verified automatic-change detector for this workstation. To complete a webhook demonstration, expose Jenkins through a controlled HTTPS endpoint, configure `<public-url>/github-webhook/`, enable the GitHub hook trigger, push a harmless commit, and capture a GitHub HTTP 2xx delivery plus a Jenkins build marked `Started by GitHub push`.

## Result

Freestyle and Pipeline CI are verified. The test-failure gate is proven by real builds `#4` and `#5`, and final develop build `#6` is green; container deployment, persistence, Ansible convergence, idempotence, and health were independently verified through the Docker-capable Ubuntu WSL environment.
