# Environment Check

Checked on **2026-09-29** before implementation, as required by the project brief.

| Component | Environment | Actual result | Status |
|---|---|---|---|
| OS | Windows host | Windows 10 Home, version 25H2, build 26200, x64 | Ready |
| Shell | Windows host | PowerShell 7.6.5 Core | Ready |
| Git | Windows host | 2.55.0.windows.1 | Ready |
| Java | Windows host | Oracle Java 21.0.11 LTS | Available; project targets Java 17 |
| Maven | Windows host | Not installed / not on `PATH` | Use Maven container initially |
| Node.js | Windows host | 24.18.0 | Ready |
| npm | Windows host | 11.16.0 | Ready |
| Docker Desktop | Windows host | Running | Ready |
| Docker Engine | Ubuntu WSL2 | 29.6.2, Linux engine | Ready |
| Docker Compose | Ubuntu WSL2 | 5.3.1 | Ready |
| WSL | Windows host | WSL2, Ubuntu 26.04.1 LTS | Ready |
| Ansible | Ubuntu WSL2 | Core 2.20.1 | Ready |
| Jenkins | Windows service/browser | 2.573, `http://localhost:8080/`, signed in | Ready |
| GitHub | In-app browser | `ThirajVS/Flight-Management`, public and empty, signed in | Ready |

## Findings

- The project workspace was empty and was not a Git repository.
- The GitHub repository was also empty, so local initialization does not overwrite remote work.
- Docker works through Ubuntu WSL2. The Windows Docker CLI binary is installed with Docker Desktop but is not on the Windows `PATH`.
- Java 17 will be pinned in Maven/Docker build images even though the Windows host currently has Java 21.
- A Maven Wrapper or Jenkins-managed Maven installation is required before native Maven commands can run on Windows.
- Jenkins is a running automatic Windows service. Its dashboard and authenticated user were visually verified.

## Command platform policy

Commands in the documentation are explicitly labelled **POWERSHELL** or **UBUNTU TERMINAL**. Windows and Linux syntax must not be mixed within a command block.

