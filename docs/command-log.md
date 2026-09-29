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

