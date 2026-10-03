# Architecture Decisions

This document records important architectural decisions made during
the development of the Employee Management System (EMS).

The purpose is to document not only what was implemented, but also
why a particular architectural decision was made, including important
trade-offs and consequences.

---

# ADR-001: API Gateway as the Browser-Facing Entry Point

## Status

Accepted

## Decision

The API Gateway is the browser-facing entry point for the EMS
application.

The React frontend communicates with the API Gateway rather than
directly communicating with individual backend services.

## Request Flow

```text
React Frontend
      |
      v
API Gateway :8082
      |
      v
EMS Backend :8080


# ADR-002: CORS is Managed by the API Gateway

## Status
Accepted

## Decision

CORS is configured and managed at the API Gateway level.

The API Gateway is the browser-facing entry point for the application, so
browser CORS requests are handled by the Gateway instead of individual
backend controllers.

## Why?

React communicates with the API Gateway rather than directly with the
EMS Backend.

Therefore, the Gateway is the appropriate place to handle browser-facing
CORS configuration.

This also gives us a single place to manage CORS as the system grows.

## What Changed?

Initially, CORS was configured in multiple places:

- API Gateway
- Backend controllers using `@CrossOrigin`

For example, some backend controllers used:

`@CrossOrigin("*")`

This resulted in both the Gateway and backend adding CORS response
headers.

We removed the controller-level `@CrossOrigin` configuration and kept
CORS management at the Gateway.

A project-wide search was performed to verify that no `@CrossOrigin`
annotations remain.

## Problem Encountered

When both Gateway and backend configured CORS, the browser received
duplicate `Access-Control-Allow-Origin` values.

For example:

`Access-Control-Allow-Origin: http://localhost:3000, *`

Although the HTTP request itself could return `200 OK`, the browser
rejected the response because the CORS header contained conflicting
origins.

## Resolution

The API Gateway is now the single CORS authority for browser requests.

Current Gateway CORS configuration:

- Allowed Origin: `http://localhost:3000`
- Allowed Methods: `GET, POST, PUT, DELETE, OPTIONS`
- Allowed Headers: `Authorization, Content-Type, Accept`

## Current Request Flow

React Frontend
      |
      | Browser request
      v
API Gateway :8082
      |
      | Internal request
      v
EMS Backend :8080

The browser interacts with the Gateway, so the Gateway provides the
CORS response headers.

## Security Consideration

Removing `@CrossOrigin` from backend controllers does not remove
application security.

CORS and authentication/authorization have different responsibilities.

Gateway:
- Browser-facing CORS
- API routing

EMS Backend:
- Spring Security
- JWT authentication
- Role-based authorization
- Business logic
- Data access

## Consequences

### Positive

- One central CORS configuration
- Avoids duplicate CORS headers
- Easier to maintain as more services are added
- Controllers remain focused on application logic
- Gateway becomes the single browser-facing entry point

### Trade-off

Backend services are no longer independently responsible for
browser-facing CORS configuration.

This is acceptable because browsers communicate with the Gateway rather
than directly with individual backend services.

## Current Status

Implemented and verified.

React now communicates through the API Gateway on port `8082`, and
controller-level `@CrossOrigin` configuration has been removed.


# ADR-003: Authentication as a Separate Microservice

## Status
Accepted

## Decision

Authentication will be extracted from the EMS Backend into a dedicated
`ems-auth-service`.

The Auth Service will be independently deployable and will own its
authentication-related data.

The EMS Backend will continue to own employee and other business-domain
data.

## Why?

Authentication and Employee Management are different responsibilities.

The EMS Backend is responsible for business domains such as:

- Employee
- Department
- Office
- Experience

The Auth Service will be responsible for:

- User registration
- User credentials
- Password hashing and verification
- User and role persistence
- Login
- JWT access-token generation

Separating these responsibilities provides a clear service boundary
and allows authentication to evolve independently from the Employee
Management application.

## Target Architecture

React Frontend
      |
      v
API Gateway :8082
      |
      +------------------------------+
      |                              |
      v                              v
Auth Service :8083              EMS Backend :8080
      |                              |
      v                              v
Auth DB                         EMS DB


## Authentication Flow

### Registration

React
  |
  v
API Gateway
  |
  v
Auth Service
  |
  v
Auth DB

The Auth Service validates the registration request, hashes the
password, creates the user and stores the authentication data in the
Auth DB.

### Login

React
  |
  v
API Gateway
  |
  v
Auth Service
  |
  v
Auth DB

The Auth Service verifies the user's credentials and generates a JWT
access token.

The token is returned to the frontend and subsequently sent with
protected API requests.

## Initial APIs

The Auth Service will initially expose:

`POST /api/auth/register`

`POST /api/auth/login`

The existing frontend API contract should be preserved where possible
so that extracting authentication does not require an unnecessary
frontend redesign.

## Database Ownership

The Auth Service will have its own database.

The EMS Backend must not directly access the Auth Service database.

Auth Service:
- Owns users
- Owns credentials
- Owns roles related to authentication
- Owns authentication persistence

EMS Backend:
- Owns employees
- Owns departments
- Owns offices
- Owns experience data

Each service owns its own persistence boundary.

## JWT Responsibility

The Auth Service will generate JWT access tokens.

The EMS Backend will validate the JWT locally when processing protected
requests.

The EMS Backend should not query the Auth Service or Auth DB for every
incoming request just to validate a JWT.

For the initial implementation, an HMAC-based JWT signing secret will
be shared between the Auth Service and the services that need to
validate the token.

As the architecture evolves, asymmetric signing and standards-based
identity solutions such as OAuth2/OIDC can be evaluated.

## JWT Authorities

The JWT will contain the user's identity and authorization information
required by the EMS Backend.

For example:

JWT subject:
`username`

Roles:
`ROLE_USER`

The exact JWT claims will be finalized during implementation.

## Authentication vs Authorization

Authentication and authorization will have separate responsibilities.

### Authentication

The Auth Service answers:

"Who is this user?"

It is responsible for:

- Registration
- Login
- Credential verification
- JWT generation

### Authorization

The EMS Backend answers:

"Is this authenticated user allowed to perform this operation?"

It is responsible for:

- Validating the JWT
- Reading user authorities/roles from the token
- Applying role-based access rules to EMS resources

## Current Implementation

The existing EMS Backend currently contains authentication-related
components such as:

- `AuthController`
- `AuthService`
- `User`
- `Role`
- `UserRepository`
- `RoleRepository`
- `CustomUserDetailsService`
- `JwtAuthenticationFilter`
- `JwtTokenProvider`

The current implementation uses the EMS Backend's database for user
lookup during authentication and JWT processing.

These responsibilities will be migrated gradually into the Auth Service.

## Migration Plan

The extraction will be performed incrementally.

1. Create `ems-auth-service`
2. Create the Auth Service database
3. Move user and role persistence to the Auth Service
4. Implement user registration
5. Implement login
6. Move JWT generation to the Auth Service
7. Update EMS Backend JWT validation so it no longer depends on
   `UserRepository` for every request
8. Add the API Gateway route for the Auth Service
9. Update and verify the React authentication flow
10. Remove the old authentication implementation from the EMS Backend

Each step will be tested before moving to the next step.

## Why Not Use OAuth2 / Keycloak Yet?

OAuth2/OIDC and an external identity provider such as Keycloak may be
appropriate for a larger production architecture.

However, they are intentionally not introduced at this stage.

The immediate objective is to understand the underlying microservice
boundaries:

- Authentication service
- Authentication database
- JWT generation
- JWT validation
- Authorization
- API Gateway routing

An external identity provider can be evaluated later without
introducing multiple architectural concepts at the same time.

## Consequences

### Positive

- Authentication has a clear service boundary
- Auth data has independent database ownership
- Authentication can be deployed independently
- JWT generation is centralized
- EMS Backend does not need direct access to the Auth DB
- The authentication system can potentially be reused by other services

### Trade-offs

- An additional service must be deployed and maintained
- An additional database is required
- JWT signing configuration must be managed between services
- Token expiration and role changes require careful consideration
- Distributed-system concerns increase as more services are introduced

## Current Scope Excludes

The initial implementation does not introduce:

- Service discovery
- Kafka / RabbitMQ
- OAuth2 / Keycloak
- Distributed sessions
- Refresh-token infrastructure
- Circuit breakers
- Centralized configuration
- Distributed tracing

These can be introduced later when they solve an actual architectural
requirement.

## Current Status

Architecture decision accepted.

Implementation will begin with the creation of the
`ems-auth-service`.