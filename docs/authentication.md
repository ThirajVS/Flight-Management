# Authentication and Role Foundation

## Scope

The authentication increment implements real candidate registration and stateless API authentication. It uses synthetic test accounts only.

## Security controls

- Passwords are hashed with BCrypt strength 12 and are never returned by the API.
- JWTs are signed with an environment-provided secret of at least 32 characters.
- Tokens contain an issued time, expiry time, and normalized email subject.
- Spring Security validates the bearer token before protected requests.
- Sessions are stateless and CSRF is disabled only because browser cookies are not used for authentication.
- CORS origins are configured through `CORS_ALLOWED_ORIGINS`.
- Authentication failures return HTTP 401; authorization failures return HTTP 403.
- Duplicate email registration returns HTTP 409.
- Validation errors return HTTP 400 without stack traces.

The browser demo stores the token in `sessionStorage`, so it is cleared when the browser session ends. A production system should consider an HttpOnly, Secure, SameSite cookie and a refresh-token strategy.

## Roles

| Database role | API-facing role | Purpose |
|---|---|---|
| `ROLE_CANDIDATE` | `CANDIDATE` | Candidate portal access |
| `ROLE_RECRUITER` | `RECRUITER` | Application review workflow |
| `ROLE_ADMIN` | `ADMIN` | Administration and audit access |

New public registrations always receive `ROLE_CANDIDATE`. Recruiter and administrator creation will remain an administrator-only workflow.

## API

### Register

`POST /api/auth/register`

Required fields: full name, email, phone, password, date of birth, nationality, city, state, and country. A successful request returns HTTP 201 with a short-lived bearer token and candidate summary.

### Login

`POST /api/auth/login`

Accepts email and password. A successful request returns HTTP 200 with an expiring bearer token. Incorrect credentials return the same generic response so account existence is not disclosed.

### Current identity

`GET /api/auth/me`

Requires `Authorization: Bearer <token>`. It returns only the user ID, full name, normalized email, and roles.

## Automated verification

The backend integration suite verifies:

- successful candidate registration;
- BCrypt storage rather than plaintext storage;
- JWT login and access to a protected endpoint;
- duplicate email conflict handling;
- invalid credential rejection;
- anonymous access rejection.

The frontend suite verifies both authentication modes and all required registration controls render from the landing page.

