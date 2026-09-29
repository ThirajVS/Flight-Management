# Experiment 12 — Docker Management with Ansible

## Aim

Automate AeroCadet container/network/volume convergence and post-deployment verification with Ansible.

## Objective

Build images, converge services, wait for health, inspect state, and prove the application endpoint.

## Requirements

The `docker`, `nginx`, and `application` roles, Docker Compose, inventory localhost connection, and untracked `.env`.

## Commands

```bash
docker compose --env-file .env ps
ANSIBLE_CONFIG=ansible/ansible.cfg ansible-playbook -i ansible/inventory ansible/playbook.yml
docker compose --env-file .env ps
curl --fail http://localhost:8090/healthz
curl --fail http://localhost:8090/actuator/health
```

## Configuration and implementation

The application role validates Compose, ensures the network/volumes through Compose, builds or pulls images according to policy, converges services with `--wait`, reports state, and calls both health endpoints. Restart policies are `unless-stopped`.

## Execution and output

The first attempted validation identified that the one-off Nginx check needed the AeroCadet network. The role was corrected, then convergence succeeded with all four containers healthy. A second execution changed nothing.

## Screenshots

Use `screenshots/12_ansible_docker/` for containers before/after, playbook run, recap, health, and idempotence.

## Result

Pass — Ansible manages and verifies the complete Docker Compose deployment.
