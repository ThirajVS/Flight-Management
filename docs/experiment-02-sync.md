# Experiment 2 — Git Synchronization

## Aim

Demonstrate repository inspection and synchronization with fetch, pull, push, log, remote, and status.

## Objective

Show the behavioral difference between `git fetch` and `git pull` using the actual AeroCadet remote.

## Requirements

The local `develop` branch and the configured `origin` GitHub remote.

## Commands

```powershell
git remote -v
git log --oneline --graph --decorate -15
git status --short --branch
git rev-parse HEAD
git fetch origin
git rev-parse HEAD
git pull --ff-only origin develop
git push origin develop
```

## Configuration and implementation

`fetch` updates remote-tracking references and `FETCH_HEAD`; it does not merge into the checked-out branch or change working files. `pull` performs a fetch and then integrates the selected remote branch, using fast-forward-only here to prevent accidental merge commits.

## Execution and output

On 2026-09-30, `HEAD` was `eafb21d7ec0bdd5022558cde8817e7cc872bccda` before and after `git fetch origin`, proving fetch did not modify the checked-out revision. `git pull --ff-only origin develop` returned `Already up to date.` and the working tree remained clean.

## Screenshots

Use `screenshots/02_git_sync/` for `remote -v`, graph log, pre/post fetch hashes, pull output, push output, and clean status.

## Result

Pass — fetch and pull were executed and their different integration behavior was documented.
