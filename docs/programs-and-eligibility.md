# Programs, Candidate Profiles, and Eligibility

## Program marketplace

The public program API contains ten synthetic programs across multiple training locations. Every response contains `fictionalDemo: true`; organisation names also include “Demo.” The data must not be presented as a real airline offer.

`GET /api/programs` supports:

- text search across code, title, and organisation;
- location filtering;
- `OPEN`, `UPCOMING`, and `CLOSED` status filtering;
- zero-based pagination with an explicit, stable response contract;
- page sizes from 1 to 50.

The landing-page marketplace loads the API when available and retains curated fictional fallback cards when the API is offline.

## Candidate profile

Authenticated candidates can maintain:

- registration/contact and location details;
- 10th, 12th, physics, mathematics, and English marks;
- graduation details;
- administrative medical-document status;
- flight experience and total hours;
- previous aviation training and English proficiency;
- passport availability;
- preferred program, location, airline, and availability.

Profile completion is recalculated on every update. Medical status is administrative only and never represents an automated fitness decision.

## Eligibility engine

Each evaluation records a timestamped result linked to the candidate and program. It checks:

1. age range;
2. 12th-grade threshold;
3. physics threshold;
4. mathematics threshold;
5. English threshold;
6. medical-document verification status;
7. passport readiness.

Each criterion is reported as `PASSED`, `FAILED`, or `PENDING`. Overall status is:

- `NOT_ELIGIBLE` when at least one criterion fails;
- `PENDING_VERIFICATION` when none fail and at least one remains pending;
- `ELIGIBLE` when every criterion passes.

The API explains actual and required marks and explicitly states that the medical check is administrative, not a fitness decision.

## Verified behavior

The integration suite verifies:

- all ten programs are returned with a stable page shape;
- public search finds the intended program;
- a candidate can complete a profile;
- a pending medical document produces `PENDING_VERIFICATION`;
- a failed physics threshold produces `NOT_ELIGIBLE` with a failed Physics check;
- the latest stored result can be retrieved.

