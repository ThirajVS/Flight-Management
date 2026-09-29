# Experiment 5 — Jenkins Declarative Pipeline

## Aim

Implement a source-controlled CI/CD Pipeline with real build, test, report, container, deployment, and health stages.

## Objective

Load `Jenkinsfile` from Git and prove that required stages execute or are conditionally gated based on agent capabilities.

## Requirements

Jenkins Pipeline plugins, Git, Java/Maven wrapper, Node/npm, and a Docker-capable agent for `DEPLOY=true`.

## Configuration

The `AeroCadet-Pipeline` job uses Pipeline script from SCM, GitHub repository, `*/develop`, and `Jenkinsfile`. Poll SCM runs every five minutes.

## Implementation

Stages are Checkout, Backend Build, Frontend Build, parallel Automated Testing, Test Report, Docker Build, Compose Validation, Deployment, optional Ansible Convergence, and Health Check. JUnit and build artifacts are published in `post` actions.

## Execution and output

- `#1`: failed at Groovy compilation; the Windows path quoting defect was fixed and pushed.
- `#2`: compilation/build/tests passed; Docker stage exposed that Jenkins runs as `SYSTEM` while Docker exists only in the interactive user's WSL session.
- `#3`: `DEPLOY=false`; 13 backend + 6 frontend tests passed, JUnit was recorded, artifacts archived, and the Pipeline ended `SUCCESS`.
- `#4`: controlled failing frontend test; deployment stages were skipped and the Pipeline failed.
- `#5`: corrected test; all tests passed and the Pipeline recovered to `SUCCESS`.
- `#6`: final develop commit `17d30db`; all 19 tests passed, artifacts were published, and the Pipeline ended `SUCCESS`.

`DEPLOY` remains available but defaults false on this agent. Docker, Compose deployment, Ansible, and health were independently executed and verified on the Docker-capable Ubuntu WSL environment.

## Screenshots

Use `screenshots/05_jenkins_pipeline/` for Jenkinsfile, job configuration, stage view, build `#3`/`#5`, reports, artifacts, and console success.

## Result

Pass — CI is green and agent-aware; deployment stages remain real and opt-in where Docker is available.
