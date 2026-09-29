# Experiment 3 — Branching, Merging, and Conflict Resolution

## Aim

Use a develop/feature workflow, merge completed increments, create one controlled conflict, and resolve it correctly.

## Objective

Demonstrate branch creation/switching, feature commits, merges, conflict markers, resolution, and final integration.

## Requirements

Git repository with `main`, `develop`, feature, DevOps, and lab branches.

## Commands

```powershell
git branch --all
git switch -c feature/authentication
git switch develop
git merge --no-ff feature/authentication
git log --oneline --graph --decorate --all
```

## Configuration and implementation

Published branches include `feature/authentication`, `feature/programs`, `feature/application`, `feature/documents`, `feature/selection`, `feature/analytics`, `devops/docker`, `devops/ansible`, and `devops/jenkins`.

The controlled conflict used `docs/merge-conflict-demo.md`. Baseline commit `35f2e43` was changed independently by `lab/conflict-jenkins` (`5bd22f2`) and `lab/conflict-ansible` (`0675322`). Merging both produced real `<<<<<<<`, `=======`, and `>>>>>>>` markers. The resolution retained both valid contributions as `JENKINS_CI + ANSIBLE_DEPLOYMENT`; merge commit `eafb21d` was pushed to `develop`.

## Execution and output

Git reported `CONFLICT (content)` and `UU docs/merge-conflict-demo.md`. After editing, `git add` and `git commit` completed the merge and the graph showed both parents.

## Screenshots

Use `screenshots/03_git_branching/` for branch list, graph, conflict output, conflict markers, resolved file, and merge commit.

## Result

Pass — the project contains real feature history plus a safe, reproducible conflict and resolution.
