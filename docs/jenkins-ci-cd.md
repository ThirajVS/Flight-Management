# Jenkins CI/CD

## Jobs

### Freestyle

The Freestyle job uses the public GitHub repository and runs:

```powershell
powershell.exe -NoProfile -ExecutionPolicy Bypass -File scripts\jenkins-freestyle.ps1
```

The script runs the Maven-wrapper backend tests, installs locked frontend dependencies, runs Vitest with JUnit output, and produces the Vite build. Configure **Publish JUnit test result report** with:

```text
backend/target/surefire-reports/*.xml, frontend/reports/junit.xml
```

### Pipeline

The Pipeline job loads `Jenkinsfile` from source control. Its real stages are Checkout, Backend Build, Frontend Build, parallel backend/frontend tests, Test Report, Docker Build, Compose Validation, Deployment, optional Ansible Convergence, and Health Check. All deployment stages are downstream of tests, so a failed test prevents image build and deployment.

The default isolated CI ports are 18090, 18081, and 15433. Ephemeral CI-only database/JWT values are generated in the workspace and removed by `deleteDir()`; no credential is stored in Git.

## Windows agent note

Docker Desktop is called using its absolute CLI path because it is not on this workstation's system `PATH`. `RUN_ANSIBLE` is disabled by default because a Windows service account may not own the interactive user's WSL distribution. Ansible was independently verified from Ubuntu. Enable the parameter only on a Jenkins agent where Ubuntu/Ansible is available to the service account.

## Webhook honesty

The repository is public, but Jenkins currently listens on `localhost:8080`. GitHub cannot deliver to a loopback-only endpoint. Do not claim automatic webhook delivery until Jenkins is exposed using a controlled HTTPS tunnel or reachable lab server and a GitHub delivery shows HTTP 2xx. The manual polling alternative is **Poll SCM** (`H/5 * * * *`) for the lab.

## Real failure/recovery demonstration

1. Create a short-lived `ci/failure-demo` branch.
2. Change one existing test assertion so it must fail; never weaken production code.
3. Push the branch and build it in Jenkins. Verify `Automated Testing` fails and all deployment stages are skipped.
4. Revert the test-only commit, push again, and rebuild.
5. Verify tests, image build, deployment, and health check all succeed.
6. Delete the demonstration branch after capturing evidence.

This is intentionally documented as a procedure until both builds are captured; results must not be fabricated.
