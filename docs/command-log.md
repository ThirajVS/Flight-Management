# Command Log

Actual execution evidence is recorded incrementally. Secret values and credential material are never included.

| Experiment | Date | Environment | Command | Purpose | Expected output | Actual output | Status | Screenshot |
|---|---|---|---|---|---|---|---|---|
| 1 | 2026-09-29 | PowerShell | `git --version` | Verify Git | Installed version | `git version 2.55.0.windows.1` | Pass | Manual capture required |
| 1 | 2026-09-29 | PowerShell | `java --version` | Verify Java | Installed version | Java 21.0.11 LTS | Pass with version note | Manual capture required |
| 1 | 2026-09-29 | PowerShell | `node --version` | Verify Node.js | Installed version | `v24.18.0` | Pass | Manual capture required |
| 1 | 2026-09-29 | PowerShell | `npm --version` | Verify npm | Installed version | `11.16.0` | Pass | Manual capture required |
| 1 | 2026-09-29 | Ubuntu WSL2 | `ansible --version` | Verify Ansible | Installed version | `ansible [core 2.20.1]` | Pass | Manual capture required |
| 1 | 2026-09-29 | Ubuntu WSL2 | `docker --version` | Verify Docker CLI | Installed version | Docker 29.6.2 | Pass | Manual capture required |
| 1 | 2026-09-29 | Ubuntu WSL2 | `docker compose version` | Verify Compose | Installed version | Docker Compose 5.3.1 | Pass | Manual capture required |
| 1 | 2026-09-29 | Browser | Open `http://localhost:8080/` | Verify Jenkins | Authenticated dashboard | Jenkins 2.573 dashboard, signed in | Pass | `screenshots/04_jenkins_freestyle/` pending |
| 1 | 2026-09-29 | Browser | Open GitHub repository | Verify remote readiness | Repository page | Signed-in, public empty repository | Pass | `screenshots/01_git/` pending |
| 1 | 2026-09-29 | PowerShell | `git init -b main` | Initialize repository | Empty Git repository on main | Repository created | Pass | Manual capture required |
| 1 | 2026-09-29 | PowerShell | `git remote add origin ...` | Connect GitHub remote | Origin fetch/push URL | Correct repository configured | Pass | Manual capture required |
| Foundation | 2026-09-29 | PowerShell | `npm run test:ci` | Run frontend unit tests and emit JUnit XML | Tests execute | 2 passed, 0 failed | Pass | Pending experiment 10 capture |
| Foundation | 2026-09-29 | PowerShell | `npm run build` | Create production frontend bundle | Vite build succeeds | 1,577 modules transformed; bundle produced | Pass | Pending experiment 5 capture |
| Foundation | 2026-09-29 | Ubuntu WSL2 / Docker | `docker run ... maven:3.9.9-eclipse-temurin-17 mvn -B -q test` | Compile and test backend on required Java 17 | JUnit tests execute | 3 passed, 0 failed, 0 errors | Pass | Pending experiment 10 capture |
| Foundation | 2026-09-29 | Browser | Open `http://localhost:4174/` | Visual desktop and mobile validation | AeroCadet landing page renders responsively | Desktop and 390×844 mobile layouts rendered; navigation collapses on mobile | Pass | Pending application screenshot |
| Authentication | 2026-09-29 | Ubuntu WSL2 / Docker | `mvn -B -q test` | Verify registration, BCrypt, JWT, conflicts and protected access | Integration suite passes | 7 backend tests passed, including 4 authentication tests | Pass | Pending experiment 10 capture |
| Authentication | 2026-09-29 | PowerShell | `npm run test:ci` | Verify authentication interface behavior | Frontend suite passes | 3 tests passed | Pass | Pending experiment 10 capture |
| Authentication | 2026-09-29 | PowerShell | `npm run build` | Verify production bundle | Vite build succeeds | 1,579 modules transformed; bundle produced | Pass | Pending experiment 5 capture |
| Authentication | 2026-09-29 | Application browser | Open sign-in and registration dialogs | Visual UI validation | Responsive fields and states visible | Both polished forms rendered with no console-visible error | Pass | Pending integrated project capture |
| Programs / Eligibility | 2026-09-29 | Ubuntu WSL2 / Docker | `mvn -B -q test` | Validate migrations, program search, profile updates and eligibility | All suites pass | 10 backend tests passed, 0 failed | Pass | Pending experiment 10 capture |
| Programs / Eligibility | 2026-09-29 | PowerShell | `npm run test:ci` | Regression-test frontend after API marketplace wiring | Tests pass | 3 passed | Pass | Pending experiment 10 capture |
| Programs / Eligibility | 2026-09-29 | PowerShell | `npm run build` | Verify production bundle | Build succeeds | 1,579 modules transformed | Pass | Pending experiment 5 capture |
| Application Workflow | 2026-09-29 | Ubuntu WSL2 / Docker | `mvn -B -q test` | Execute draft, submit, upload, review, shortlist, notification and audit flow | Integration suite passes | 12 backend tests passed, 0 failed | Pass | Pending experiment 10 capture |
| Final Test Suite | 2026-09-30 | PowerShell | `backend\mvnw.cmd -B test` | Run complete backend regression suite | All tests pass | 13 passed, 0 failed/errors | Pass | Manual capture required |
| Final Test Suite | 2026-09-30 | PowerShell | `npm run test:ci` | Run complete frontend regression suite | All tests pass and JUnit XML emitted | 6 passed, 0 failed | Pass | Manual capture required |
| Docker Build | 2026-09-30 | Ubuntu WSL2 | `docker compose --env-file .env build` | Build production images | Three custom images build | Backend, frontend, and gateway built | Pass | Manual capture required |
| Docker Deployment | 2026-09-30 | Ubuntu WSL2 | `docker compose --env-file .env up -d --wait` | Deploy four-service stack | All services healthy | PostgreSQL, backend, frontend, and Nginx healthy | Pass | Manual capture required |
| Docker Health | 2026-09-30 | PowerShell | `Invoke-RestMethod http://localhost:8090/actuator/health` | Verify routed backend | Status `UP` | `UP` | Pass | Manual capture required |
| Docker Persistence | 2026-09-30 | Ubuntu WSL2 | Remove/recreate only PostgreSQL container, then query `users` | Verify named volume | Same row count after recreation | 28 before, 28 after | Pass | Manual capture required |
| Image Comparison | 2026-09-30 | Ubuntu WSL2 | Build/list standard and optimized backend images | Measure meaningful reduction | Optimized runtime smaller | 1.07 GB vs 487 MB, about 54% smaller | Pass | Manual capture required |
| Ansible Syntax | 2026-09-30 | Ubuntu WSL2 | `ansible-playbook ... --syntax-check` | Validate playbook | No syntax errors | Playbook accepted | Pass | Manual capture required |
| Ansible Convergence | 2026-09-30 | Ubuntu WSL2 | `ansible-playbook -i ansible/inventory ansible/playbook.yml` | Manage Docker deployment and checks | No failed tasks | `ok=14 changed=1 failed=0` | Pass | Manual capture required |
| Ansible Idempotence | 2026-09-30 | Ubuntu WSL2 | Repeat playbook immediately | Verify stable convergence | No changes/failures | `ok=14 changed=0 failed=0` | Pass | Manual capture required |
| Jenkins Freestyle | 2026-09-30 | Jenkins browser | Run `AeroCadet-Freestyle #1` | Verify Git checkout/build/tests/report | Successful job | 13 backend + 6 frontend tests; `SUCCESS` | Pass | Manual file export required |
| Jenkins Pipeline | 2026-09-30 | Jenkins browser | Run `AeroCadet-Pipeline #3` | Verify source-controlled CI | Successful job | 19 tests, artifacts, `SUCCESS` | Pass | Manual file export required |
| Failure Gate | 2026-09-30 | Jenkins browser | Build `ci/failure-demo` commit `68f7aa0` as Pipeline `#4` | Prove critical failure blocks downstream stages | Pipeline fails and skips deployment | 1 frontend failure; all downstream stages skipped | Pass | Manual file export required |
| Recovery | 2026-09-30 | Jenkins browser | Build fix commit `cacf932` as Pipeline `#5` | Prove recovery | All tests pass | 19 passed; `SUCCESS` | Pass | Manual file export required |
| Git Conflict | 2026-09-30 | PowerShell | Merge `lab/conflict-ansible` after `lab/conflict-jenkins` | Produce controlled conflict | Conflict markers and `UU` state | Real content conflict in `docs/merge-conflict-demo.md` | Pass | Manual capture required |
| Conflict Resolution | 2026-09-30 | PowerShell | Resolve, add, and commit | Preserve both contributions | Merge commit with both parents | Commit `eafb21d` | Pass | Manual capture required |
| Fetch vs Pull | 2026-09-30 | PowerShell | Compare HEAD around `git fetch`; then `git pull --ff-only origin develop` | Demonstrate semantics | Fetch leaves HEAD; pull integrates | HEAD unchanged; pull already up to date | Pass | Manual capture required |
| Restart Helper | 2026-09-30 | PowerShell | `.\scripts\start-aerocadet.ps1` | Recover after reboot/shutdown | Docker starts and stack becomes healthy | Four healthy services; application URL printed | Pass | Manual capture required |
| Status Helper | 2026-09-30 | PowerShell | `.\scripts\status-aerocadet.ps1` | Inspect deployment | Containers and backend health shown | Four healthy; backend `UP` | Pass | Manual capture required |
| Final Jenkins CI | 2026-09-30 | Jenkins browser | Run `AeroCadet-Pipeline #6` from `develop` | Verify completion commit `17d30db` | Tests/reports/artifacts succeed | 19 tests, no failures, artifacts published, `SUCCESS` | Pass | Manual file export required |

