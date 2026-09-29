# Experiment 11 — Ansible Server and Nginx Configuration

## Aim

Use Ansible roles to validate/provision the host and configure the AeroCadet Nginx gateway.

## Objective

Demonstrate inventory, variables, role structure, syntax validation, Nginx configuration validation, execution, and idempotence.

## Requirements

Ubuntu WSL, Ansible Core 2.20.1, Docker access, and the `ansible/` project tree.

## Commands

```bash
export ANSIBLE_CONFIG="$PWD/ansible/ansible.cfg"
ansible-playbook -i ansible/inventory ansible/playbook.yml --syntax-check
ansible-playbook -i ansible/inventory ansible/playbook.yml
```

## Configuration and implementation

The Docker role optionally installs host packages/engine, the Nginx role validates the versioned reverse-proxy configuration in the pinned image, and the application role converges Compose plus health checks. `configure_host=true` is available for a fresh Linux server.

## Execution and output

Syntax validation passed. The successful convergence recap was `ok=14 changed=1 failed=0`. An immediate second run produced `ok=14 changed=0 failed=0`, verifying idempotence. Nginx and both public/backend health endpoints returned HTTP 200.

## Screenshots

Use `screenshots/11_ansible/` for version, inventory, playbook, role tree, syntax check, Nginx validation, first run, and idempotent run.

## Result

Pass — Ansible configuration and Nginx verification are repeatable and idempotent.
