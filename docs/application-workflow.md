# Application, Document, and Selection Workflow

## Application lifecycle

Candidates create one application per program. Each receives a unique `AC-YYYY-XXXXXXXX` identifier and begins in `DRAFT`.

The seven form steps are:

1. personal details;
2. academic details;
3. medical-document details;
4. documents;
5. eligibility;
6. review;
7. submit.

Draft payloads are saved as structured JSON with a current-step marker. Only a draft can be edited or submitted. Submission records a timestamp, status-history event, notification, audit event, and seven selection stages.

Supported status values are `DRAFT`, `SUBMITTED`, `ELIGIBILITY_CHECK`, `DOCUMENT_VERIFICATION`, `SHORTLISTED`, `ASSESSMENT`, `INTERVIEW`, `MEDICAL`, `FINAL_SELECTION`, `OFFERED`, `REJECTED`, and `WITHDRAWN`.

Candidate ownership is checked on every candidate read/write operation. Recruiter/admin review operations are protected by method-level roles.

## Documents

The local lab implementation stores files in `UPLOAD_DIRECTORY`, designed to be mounted as a Docker volume. The database stores only metadata and workflow state.

Controls include:

- PDF, JPEG, and PNG allow-list;
- 10 MB application limit;
- normalized server-generated names;
- path traversal prevention;
- `PENDING_VERIFICATION`, `VERIFIED`, `REJECTED`, and `REUPLOAD_REQUESTED` review outcomes;
- reason, reviewer, and review timestamp metadata.

Only synthetic dummy documents belong in this project.

## Notifications and audit trail

Submission, document review, and status changes create internal candidate notifications. Users can list notifications, read an unread count, and mark individual records read.

Application creation, draft saves, submission, upload, document review, and status changes generate immutable audit rows with actor, entity, action, details, and timestamp.

## Verified behavior

The automated integration flow performs a real end-to-end lifecycle:

1. register candidate;
2. create and save a draft;
3. submit the application;
4. upload a synthetic PDF;
5. authenticate a recruiter;
6. verify the document;
7. shortlist the candidate;
8. retrieve three status-history events and seven stages;
9. confirm three unread notifications;
10. confirm audit records exist.

It also verifies that duplicate applications for the same candidate and program return HTTP 409.

