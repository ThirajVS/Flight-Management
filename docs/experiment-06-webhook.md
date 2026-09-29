# Experiment 6 — GitHub Webhook

## Aim

Trigger Jenkins automatically from a GitHub push.

## Objective

Configure GitHub `push` delivery to Jenkins and verify an HTTP 2xx delivery plus an automatically scheduled build.

## Requirements

A stable HTTPS endpoint reachable from GitHub and forwarding to `http://localhost:8080/github-webhook/`, or a reachable lab Jenkins server.

## Current configuration

Jenkins currently listens only on `localhost:8080`; GitHub cannot reach a loopback address. Both AeroCadet jobs use verified Poll SCM (`H/5 * * * *`) so remote changes are still detected automatically for the local lab.

## Manual implementation

1. Start a controlled HTTPS tunnel or expose a lab server to Jenkins port 8080.
2. In GitHub open **Settings → Webhooks → Add webhook**.
3. Set Payload URL to `https://<reachable-host>/github-webhook/`, content type `application/json`, and select push events.
4. Enable **GitHub hook trigger for GITScm polling** in the Jenkins job.
5. Push a documentation-only commit.
6. Verify GitHub Recent Deliveries shows HTTP 2xx and Jenkins records `Started by GitHub push`.

## Execution and output

Not executed because no public endpoint was supplied. This limitation is intentionally not represented as success.

## Screenshots

**MANUAL SCREENSHOT REQUIRED** in `screenshots/06_github_webhook/`: webhook settings, successful delivery response, pushed commit, automatically triggered Jenkins console, and result.

## Result

Documented workaround — SCM polling is verified; webhook delivery requires a user-controlled public endpoint.
