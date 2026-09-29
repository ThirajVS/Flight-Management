# Ansible deployment

## Layout

```text
ansible/
├── ansible.cfg
├── inventory
├── playbook.yml
├── requirements.yml
├── group_vars/all.yml
└── roles/
    ├── docker/
    ├── nginx/
    └── application/
```

The Docker role can provision packages and the daemon on a Linux server (`configure_host=true`) and always verifies engine connectivity. The Nginx role validates the versioned gateway configuration inside the pinned Nginx image. The application role validates Compose, converges all four services, waits for container health, reports container state, and verifies both public and Spring Boot health endpoints.

## Ubuntu terminal

```bash
cd /mnt/c/Users/thira/Flight_Management
export ANSIBLE_CONFIG="$PWD/ansible/ansible.cfg"
ansible-playbook -i ansible/inventory ansible/playbook.yml --syntax-check
ansible-playbook -i ansible/inventory ansible/playbook.yml
```

For a fresh Linux host, update `inventory`, set `project_dir` and `application_url`, securely create the untracked `.env`, then run with `-e configure_host=true`. The invoking account requires sudo rights and Docker access.

## Verified result

The syntax check passed. The first successful convergence built/recreated the application and ended with `ok=14 changed=1 failed=0`; all four services were healthy and both health endpoints returned HTTP 200. A second immediate run ended with `ok=14 changed=0 failed=0`, verifying idempotence.
