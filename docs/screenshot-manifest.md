# Screenshot Manifest

The directories are committed, but screenshots are never fabricated. Browser states for AeroCadet and Jenkins were visually verified through the actual applications during implementation; this environment could display those captures but could not export them as PNG files into the repository. Every row therefore remains an explicit capture/export task. Capture only the named application or terminal, crop clearly, enlarge terminal text, and exclude credentials, tokens, `.env` contents, and personal information.

Status key: **MANUAL SCREENSHOT REQUIRED** means the underlying operation is documented/verified but the named PNG is not present. **PUBLIC ENDPOINT REQUIRED** means the evidence cannot exist until Jenkins has a user-controlled public HTTPS endpoint.

## Experiment 1 — Git and environment

| Screenshot | Tool | Action / what must be visible | Purpose | Status |
|---|---|---|---|---|
| `01_git_version.png` | PowerShell | Run `git --version`; include command and version | Verify Git | MANUAL SCREENSHOT REQUIRED |
| `02_java_version.png` | PowerShell | Run `java --version`; include Java 21 host output | Verify Java (project targets 17) | MANUAL SCREENSHOT REQUIRED |
| `03_maven_version.png` | PowerShell | Run `backend\mvnw.cmd --version` | Verify Maven Wrapper and Java | MANUAL SCREENSHOT REQUIRED |
| `04_node_version.png` | PowerShell | Run `node --version` | Verify Node | MANUAL SCREENSHOT REQUIRED |
| `05_npm_version.png` | PowerShell | Run `npm --version` | Verify npm | MANUAL SCREENSHOT REQUIRED |
| `06_docker_version.png` | Ubuntu Terminal | Run `docker --version` | Verify Docker | MANUAL SCREENSHOT REQUIRED |
| `07_compose_version.png` | Ubuntu Terminal | Run `docker compose version` | Verify Compose | MANUAL SCREENSHOT REQUIRED |
| `08_ansible_version.png` | Ubuntu Terminal | Run `ansible --version` | Verify Ansible | MANUAL SCREENSHOT REQUIRED |
| `09_jenkins_version.png` | Jenkins browser | Open `http://localhost:8080/manage/about` | Verify Jenkins 2.573 | MANUAL SCREENSHOT REQUIRED |
| `10_git_config.png` | PowerShell | Run `git config --list --show-origin`; crop out personal values if needed | Demonstrate configuration | MANUAL SCREENSHOT REQUIRED |
| `11_git_init_status.png` | PowerShell | Use documented initialization evidence or a disposable clone; show `git status` | Demonstrate repository state | MANUAL SCREENSHOT REQUIRED |
| `12_git_add_commit.png` | PowerShell | Show safe `git add`, `git commit`, and resulting commit | Demonstrate local history | MANUAL SCREENSHOT REQUIRED |
| `13_git_remote_push.png` | PowerShell | Run `git remote -v` and show successful push without credentials | Verify GitHub connection | MANUAL SCREENSHOT REQUIRED |
| `14_github_repository.png` | GitHub browser | Open repository Code page with branches/files visible | Verify remote repository | MANUAL SCREENSHOT REQUIRED |
| `15_git_clone.png` | PowerShell | Clone into a disposable lab folder and show completion | Demonstrate clone | MANUAL SCREENSHOT REQUIRED |

## Experiment 2 — Synchronization

| Screenshot | Tool | Action / what must be visible | Purpose | Status |
|---|---|---|---|---|
| `01_remote_log_status.png` | PowerShell | Run `git remote -v`, `git log --oneline -5`, and `git status` | Inspect local/remote state | MANUAL SCREENSHOT REQUIRED |
| `02_git_fetch.png` | PowerShell | Show HEAD before/after `git fetch origin` | Prove fetch does not move the branch | MANUAL SCREENSHOT REQUIRED |
| `03_git_pull.png` | PowerShell | Run `git pull --ff-only origin develop` | Prove pull fetches/integrates | MANUAL SCREENSHOT REQUIRED |
| `04_git_push.png` | PowerShell | Show an up-to-date or successful push | Prove synchronization | MANUAL SCREENSHOT REQUIRED |
| `05_fetch_vs_pull.png` | PowerShell | Show the before/fetch/after/pull sequence from Experiment 2 report | Explain the difference | MANUAL SCREENSHOT REQUIRED |

## Experiment 3 — Branching and conflict

| Screenshot | Tool | Action / what must be visible | Purpose | Status |
|---|---|---|---|---|
| `01_branch_list.png` | PowerShell | Run `git branch -a` | Show published branch model | MANUAL SCREENSHOT REQUIRED |
| `02_branch_create_switch.png` | PowerShell | Show safe branch creation/switching in a disposable branch | Demonstrate branch commands | MANUAL SCREENSHOT REQUIRED |
| `03_feature_commit.png` | PowerShell | Show a feature commit and branch log | Demonstrate feature work | MANUAL SCREENSHOT REQUIRED |
| `04_merge.png` | PowerShell | Show successful feature merge | Demonstrate integration | MANUAL SCREENSHOT REQUIRED |
| `05_merge_conflict.png` | PowerShell | Reproduce using documented lab branches; show `CONFLICT (content)` and `UU` | Prove a real conflict | MANUAL SCREENSHOT REQUIRED |
| `06_conflict_markers.png` | Editor/PowerShell | Show `<<<<<<<`, `=======`, `>>>>>>>` in the controlled demo file | Show conflict anatomy | MANUAL SCREENSHOT REQUIRED |
| `07_resolved_file.png` | Editor | Show `JENKINS_CI + ANSIBLE_DEPLOYMENT` | Prove intentional resolution | MANUAL SCREENSHOT REQUIRED |
| `08_merge_graph.png` | PowerShell | Run `git log --graph --oneline --decorate --all` | Show both merge parents | MANUAL SCREENSHOT REQUIRED |

## Experiment 4 — Jenkins Freestyle

| Screenshot | Tool | Action / what must be visible | Purpose | Status |
|---|---|---|---|---|
| `01_dashboard.png` | Jenkins browser | Dashboard with `AeroCadet-Freestyle` | Verify job exists | MANUAL SCREENSHOT REQUIRED |
| `02_scm_configuration.png` | Jenkins browser | Job Configure → Git URL and `*/develop` | Verify SCM | MANUAL SCREENSHOT REQUIRED |
| `03_build_trigger.png` | Jenkins browser | Poll SCM with `H/5 * * * *` | Verify automatic polling | MANUAL SCREENSHOT REQUIRED |
| `04_build_command.png` | Jenkins browser | PowerShell build step and JUnit paths | Verify real CI commands | MANUAL SCREENSHOT REQUIRED |
| `05_console_success.png` | Jenkins browser | Build `#1` console ending `Finished: SUCCESS` | Prove successful execution | MANUAL SCREENSHOT REQUIRED |
| `06_test_result.png` | Jenkins browser | Build `#1` test result with zero failures | Prove report publication | MANUAL SCREENSHOT REQUIRED |

## Experiment 5 — Jenkins Pipeline

| Screenshot | Tool | Action / what must be visible | Purpose | Status |
|---|---|---|---|---|
| `01_pipeline_configuration.png` | Jenkins browser | Pipeline from SCM, `*/develop`, `Jenkinsfile` | Verify source-controlled pipeline | MANUAL SCREENSHOT REQUIRED |
| `02_jenkinsfile.png` | Editor/GitHub | Show stage declarations without secret values | Verify pipeline definition | MANUAL SCREENSHOT REQUIRED |
| `03_stage_view_success.png` | Jenkins browser | Build `#5`, green build/test/report stages | Prove recovered pipeline | MANUAL SCREENSHOT REQUIRED |
| `04_test_report.png` | Jenkins browser | Latest Test Result with 19 tests and no failures | Prove continuous testing | MANUAL SCREENSHOT REQUIRED |
| `05_artifacts.png` | Jenkins browser | Build artifacts list | Prove publication | MANUAL SCREENSHOT REQUIRED |
| `06_console_success.png` | Jenkins browser | Build `#5` ending `SUCCESS` | Prove pipeline success | MANUAL SCREENSHOT REQUIRED |

## Experiment 6 — GitHub webhook

| Screenshot | Tool | Action / what must be visible | Purpose | Status |
|---|---|---|---|---|
| `01_webhook_configuration.png` | GitHub browser | Webhook URL ending `/github-webhook/`; hide secrets | Verify reachable hook setup | PUBLIC ENDPOINT REQUIRED |
| `02_successful_delivery.png` | GitHub browser | Recent Delivery HTTP 2xx | Prove delivery | PUBLIC ENDPOINT REQUIRED |
| `03_trigger_push.png` | GitHub browser | Harmless trigger commit | Identify source event | PUBLIC ENDPOINT REQUIRED |
| `04_automatic_build.png` | Jenkins browser | Cause `Started by GitHub push` | Prove automatic trigger | PUBLIC ENDPOINT REQUIRED |
| `05_webhook_build_success.png` | Jenkins browser | Triggered build result/console | Complete webhook chain | PUBLIC ENDPOINT REQUIRED |

## Experiment 7 — Docker lifecycle

| Screenshot | Tool | Action / what must be visible | Purpose | Status |
|---|---|---|---|---|
| `01_pull_images.png` | Ubuntu Terminal | `docker compose --env-file .env pull` completion | Pull images | MANUAL SCREENSHOT REQUIRED |
| `02_images.png` | Ubuntu Terminal | `docker images` filtered to AeroCadet dependencies | List images | MANUAL SCREENSHOT REQUIRED |
| `03_run_ps.png` | Ubuntu Terminal | Safe container run and `docker ps` | Create/list running container | MANUAL SCREENSHOT REQUIRED |
| `04_ps_all.png` | Ubuntu Terminal | `docker ps -a` | List all containers | MANUAL SCREENSHOT REQUIRED |
| `05_stop_start_restart.png` | Ubuntu Terminal | Lifecycle commands on an AeroCadet service | Prove state transitions | MANUAL SCREENSHOT REQUIRED |
| `06_logs.png` | Ubuntu Terminal | Sanitized backend logs | Inspect runtime | MANUAL SCREENSHOT REQUIRED |
| `07_exec.png` | Ubuntu Terminal | Safe PostgreSQL row-count query | Prove exec/database access | MANUAL SCREENSHOT REQUIRED |
| `08_inspect.png` | Ubuntu Terminal | Relevant inspect health/network/volume fields | Inspect metadata | MANUAL SCREENSHOT REQUIRED |
| `09_remove_recreate.png` | Ubuntu Terminal | Remove/recreate only PostgreSQL container; 28 rows remain | Prove lifecycle and persistence | MANUAL SCREENSHOT REQUIRED |

## Experiment 8 — Dockerfiles and image optimization

| Screenshot | Tool | Action / what must be visible | Purpose | Status |
|---|---|---|---|---|
| `01_dockerfiles.png` | Editor | Standard and multi-stage optimized backend Dockerfiles | Compare designs | MANUAL SCREENSHOT REQUIRED |
| `02_build_images.png` | Ubuntu Terminal | Both backend build commands succeeding | Prove custom builds | MANUAL SCREENSHOT REQUIRED |
| `03_size_comparison.png` | Ubuntu Terminal | Standard 1.07 GB and optimized 487 MB images | Prove ~54% reduction | MANUAL SCREENSHOT REQUIRED |
| `04_docker_history.png` | Ubuntu Terminal | `docker history` for both images | Explain layer difference | MANUAL SCREENSHOT REQUIRED |
| `05_running_optimized.png` | Ubuntu Terminal | Healthy optimized backend container | Verify usable result | MANUAL SCREENSHOT REQUIRED |

## Experiment 9 — Docker deployment

| Screenshot | Tool | Action / what must be visible | Purpose | Status |
|---|---|---|---|---|
| `01_compose_file.png` | Editor | Four services, health checks, network, volumes | Verify orchestration | MANUAL SCREENSHOT REQUIRED |
| `02_compose_up.png` | Ubuntu Terminal | `docker compose ... up -d --build --wait` success | Deploy stack | MANUAL SCREENSHOT REQUIRED |
| `03_container_status_ports.png` | Ubuntu Terminal | Four healthy services and port mappings | Verify deployment | MANUAL SCREENSHOT REQUIRED |
| `04_network_volume.png` | Ubuntu Terminal | AeroCadet network and named volumes | Verify isolation/persistence | MANUAL SCREENSHOT REQUIRED |
| `05_java_postgres.png` | Ubuntu Terminal | Backend health and PostgreSQL query | Verify Java/database services | MANUAL SCREENSHOT REQUIRED |
| `06_browser_application.png` | Application browser | `http://localhost:8090` landing page | Verify browser deployment | MANUAL SCREENSHOT REQUIRED |

## Experiment 10 — Continuous testing

| Screenshot | Tool | Action / what must be visible | Purpose | Status |
|---|---|---|---|---|
| `01_backend_tests.png` | PowerShell | Maven summary: 13 run, 0 failures/errors | Prove backend tests | MANUAL SCREENSHOT REQUIRED |
| `02_frontend_tests.png` | PowerShell | Vitest summary: 6 passed | Prove frontend tests | MANUAL SCREENSHOT REQUIRED |
| `03_jenkins_test_stage.png` | Jenkins browser | Automated Testing stage in build `#5` | Prove Jenkins execution | MANUAL SCREENSHOT REQUIRED |
| `04_junit_report.png` | Jenkins browser | Published result with no failures | Prove reporting | MANUAL SCREENSHOT REQUIRED |
| `05_controlled_failure.png` | Jenkins browser | Build `#4`, 1 failed/5 passed frontend tests and skipped downstream stages | Prove quality gate | MANUAL SCREENSHOT REQUIRED |
| `06_fixed_pipeline.png` | Jenkins browser | Build `#5` success after commit `cacf932` | Prove recovery | MANUAL SCREENSHOT REQUIRED |

## Experiment 11 — Ansible and Nginx

| Screenshot | Tool | Action / what must be visible | Purpose | Status |
|---|---|---|---|---|
| `01_ansible_version.png` | Ubuntu Terminal | `ansible --version` | Verify installation | MANUAL SCREENSHOT REQUIRED |
| `02_inventory_playbook.png` | Editor | Inventory and playbook/role imports | Verify configuration | MANUAL SCREENSHOT REQUIRED |
| `03_syntax_check.png` | Ubuntu Terminal | Successful `--syntax-check` | Validate playbook | MANUAL SCREENSHOT REQUIRED |
| `04_playbook_run.png` | Ubuntu Terminal | `ok=14 changed=1 failed=0` | Prove convergence | MANUAL SCREENSHOT REQUIRED |
| `05_nginx_validation.png` | Ubuntu Terminal | Nginx configuration test and health response | Verify gateway | MANUAL SCREENSHOT REQUIRED |
| `06_idempotence.png` | Ubuntu Terminal | Second run `changed=0 failed=0` | Prove idempotence | MANUAL SCREENSHOT REQUIRED |

## Experiment 12 — Ansible Docker management

| Screenshot | Tool | Action / what must be visible | Purpose | Status |
|---|---|---|---|---|
| `01_containers_before.png` | Ubuntu Terminal | Compose state before convergence | Establish baseline | MANUAL SCREENSHOT REQUIRED |
| `02_application_role.png` | Editor | Network/build/stop/start/verify tasks | Show automation | MANUAL SCREENSHOT REQUIRED |
| `03_ansible_execution.png` | Ubuntu Terminal | Successful role execution | Prove management | MANUAL SCREENSHOT REQUIRED |
| `04_containers_after.png` | Ubuntu Terminal | Four healthy containers after playbook | Verify resulting state | MANUAL SCREENSHOT REQUIRED |
| `05_application_health.png` | Ubuntu Terminal/browser | `/healthz` 200 and `/actuator/health` `UP` | Verify deployment | MANUAL SCREENSHOT REQUIRED |

## Experiment 13 — Integrated project

| Screenshot | Tool | Action / what must be visible | Purpose | Status |
|---|---|---|---|---|
| `01_git_github.png` | PowerShell/GitHub | Final commit pushed and visible remotely | Source-control proof | MANUAL SCREENSHOT REQUIRED |
| `02_jenkins_pipeline.png` | Jenkins browser | Latest green develop build | CI proof | MANUAL SCREENSHOT REQUIRED |
| `03_docker_ansible_health.png` | Ubuntu Terminal | Healthy containers, Ansible result, endpoint health | Deployment proof | MANUAL SCREENSHOT REQUIRED |
| `04_landing.png` | Application browser | Desktop AeroCadet landing page | Product proof | MANUAL SCREENSHOT REQUIRED |
| `05_mobile.png` | Application browser | 390×844 responsive landing page | Responsive proof | MANUAL SCREENSHOT REQUIRED |
| `06_candidate_dashboard.png` | Application browser | Candidate metrics, profile progress, applications/timeline | Candidate workflow | MANUAL SCREENSHOT REQUIRED |
| `07_recruiter_dashboard.png` | Application browser | Review queue, filters, selection actions | Recruiter workflow | MANUAL SCREENSHOT REQUIRED |
| `08_admin_dashboard.png` | Application browser | Analytics, funnel, status distribution, audits | Admin workflow | MANUAL SCREENSHOT REQUIRED |
| `09_program_eligibility.png` | Application browser | Fictional program card and rule-by-rule eligibility | Core application feature | MANUAL SCREENSHOT REQUIRED |
| `10_documents_selection.png` | Application browser | Document status and seven selection stages | Workflow proof | MANUAL SCREENSHOT REQUIRED |

## Safe manual capture sequence

1. Start the stack with `scripts\start-aerocadet.ps1` and Jenkins through its existing Windows service.
2. Open only the relevant terminal, GitHub page, Jenkins page, editor file, or AeroCadet page listed above.
3. Hide `.env`, credentials, tokens, webhook secrets, browser profile details, and unrelated windows.
4. Capture with Windows Snipping Tool (`Win+Shift+S`), crop tightly, and save to the row's experiment directory with the exact filename.
5. Confirm every saved PNG is readable at normal report size before submission.
