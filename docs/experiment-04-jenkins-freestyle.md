# Experiment 4 — Jenkins Freestyle CI

## Aim

Configure a Jenkins Freestyle job connected to GitHub and run a real build/test workflow.

## Objective

Verify checkout, backend tests, frontend tests, production build, JUnit publication, and an automated source-change trigger.

## Requirements

Jenkins 2.573, Git, Java 21 capable of compiling Java 17, Node/npm, Maven wrapper, and repository network access.

## Commands and configuration

- Job: `AeroCadet-Freestyle`
- SCM: Git, `https://github.com/ThirajVS/Flight-Management.git`
- Branch: `*/develop`
- Trigger: Poll SCM, `H/5 * * * *`
- Build command: `powershell.exe -NoProfile -ExecutionPolicy Bypass -File scripts/jenkins-freestyle.ps1`
- JUnit files: `backend/target/surefire-reports/*.xml, frontend/reports/junit.xml`

## Implementation

The PowerShell build script runs `mvnw.cmd -B test`, `npm ci`, `npm run test:ci`, and `npm run build`. Any nonzero command stops the job.

## Execution and output

Build `#1` checked out develop commit `54b020b`, ran 13 backend tests and 6 frontend tests, built the Vite bundle, recorded JUnit reports, and ended `Finished: SUCCESS` in 49 seconds.

## Screenshots

Use `screenshots/04_jenkins_freestyle/` for dashboard, job configuration, SCM, trigger, command, build history, console, test result, and success.

## Result

Pass — a real GitHub-backed Freestyle job builds and tests AeroCadet successfully.
