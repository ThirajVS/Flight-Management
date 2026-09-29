# Assessments, interviews, dashboards, and analytics

## Scope

This increment completes the main selection workflow with synthetic assessment questions, scored attempts, recruiter interview scheduling, selection-stage updates, role-specific dashboards, aggregate analytics, and administrator audit-log access.

## Backend behavior

- `GET /api/assessments/questions` returns ten demo multiple-choice questions without exposing correct answers.
- `POST /api/assessments/submit` stores answers, elapsed time, score, and `QUALIFIED` or `NOT_QUALIFIED` result.
- `POST|PUT /api/interviews` allows recruiters and administrators to schedule and update interviews.
- `GET /api/interviews` gives candidates only their own interview schedule.
- `PUT /api/applications/{applicationId}/stages/{stageId}` persists stage date, status, score, and remarks.
- `GET /api/recruiter/dashboard` reports actionable operational counts.
- `GET /api/analytics/summary` returns administrator metrics, status/program/month groupings, and a candidate funnel.
- `GET /api/audit-logs` is administrator-only and returns the most recent 100 actions.

## Demo data

Demo seeding is opt-in through `DEMO_DATA_ENABLED=true`. It creates 20 candidates, five recruiters, three administrators, 30 applications, 30 document records, 12 assessment attempts, eight interviews, notifications, selection stages, and audit events. All people and organizations are fictional.

Demo account patterns are `candidate01@demo.aerocadet.local`, `recruiter01@demo.aerocadet.local`, and `admin01@demo.aerocadet.local`. The local-only demo password is documented in deployment notes and must not be reused outside this lab.

## Verification

`SelectionAndAnalyticsIntegrationTest` exercises application submission, perfect-score assessment evaluation, recruiter interview scheduling, recruiter counters, administrator analytics, administrator audit retrieval, and a candidate authorization denial. The complete backend suite contains 13 passing tests; the frontend suite contains six passing tests covering the landing/auth flow and all role dashboards.
