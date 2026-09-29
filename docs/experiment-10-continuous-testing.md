# Experiment 10 — Continuous Testing and Failure Gate

## Aim

Run automated backend/frontend tests in Jenkins, publish JUnit results, fail on defects, and prevent deployment.

## Objective

Prove real failure and recovery without weakening production code.

## Requirements

JUnit/MockMvc tests, Vitest/Testing Library tests, JUnit XML reporters, and Jenkins Pipeline.

## Commands

```powershell
backend\mvnw.cmd -B test
Set-Location frontend
npm ci
npm run test:ci
```

## Implementation

The backend has 13 tests covering startup, authentication/authorization, programs, profile/eligibility, application workflow, analytics, and health. The frontend has 6 tests for landing content, demo labels, auth dialogs, and all three role dashboards.

## Execution and output

On branch `ci/failure-demo`, commit `68f7aa0` changed one expected program-card count from 3 to 99. Jenkins Pipeline `#4` reported 1 failed and 5 passed frontend tests; backend tests still passed 13/13. Test Report, Docker Build, Compose Validation, Deployment, Ansible, and Health Check were all skipped due to the failure. Commit `cacf932` restored the assertion. Pipeline `#5` recorded all 19 tests with no failures and ended `SUCCESS`.

Final develop build `#6` checked out completion commit `17d30db`, repeated all 19 tests with no failures, published the JUnit result and artifacts, and ended `SUCCESS`.

## Screenshots

Use `screenshots/10_testing/` for passing suites, JUnit trend, Pipeline `#4` assertion failure, skipped deployment stages, fix commit, and Pipeline `#5` success.

## Result

Pass — Jenkins detects a real regression, blocks downstream work, publishes results, and recovers after the fix.
