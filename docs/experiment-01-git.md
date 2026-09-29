# Experiment 1 — Git and GitHub Foundation

## Aim

Install/verify the development toolchain, initialize a Git repository, connect it to GitHub, and publish the first revision.

## Objective

Demonstrate `git init`, `status`, `add`, `commit`, `remote`, `push`, and `clone` with a real public repository.

## Requirements

Windows PowerShell, Git 2.55.0, Java 21 host runtime (project target Java 17), Node 24.18.0, npm 11.16.0, Ubuntu WSL, Docker 29.6.2, Compose 5.3.1, Ansible Core 2.20.1, Jenkins 2.573, and GitHub.

## Commands

```powershell
git --version
git init -b main
git status
git add .
git commit -m "initial AeroCadet project foundation"
git remote add origin https://github.com/ThirajVS/Flight-Management.git
git remote -v
git push -u origin main
git clone https://github.com/ThirajVS/Flight-Management.git
```

## Configuration and implementation

`.gitignore` excludes `.env`, build output, uploaded files, IDE state, and dependency folders. `.env.example` contains placeholders only. The remote is `https://github.com/ThirajVS/Flight-Management.git`.

## Execution and output

The repository was initialized on `main`, meaningful incremental commits were created, and all project branches were pushed. GitHub and Jenkins both cloned the public repository successfully.

## Screenshots

Store real terminal and GitHub captures in `screenshots/01_git/`. See `screenshot-manifest.md`; uncaptured GUI/terminal frames are marked **MANUAL SCREENSHOT REQUIRED**.

## Result

Pass — local Git, GitHub origin, push, and remote clone/checkout were verified.
