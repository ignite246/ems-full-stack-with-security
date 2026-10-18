# EMS Project --- Local Services Startup & Dependency Guide

> **Purpose:** Quick reference for starting the complete Employee
> Management System locally.
>
> This document explains **what each application does, which port it
> uses, what it depends on, and the recommended startup sequence**.

------------------------------------------------------------------------

## 1. EMS Ecosystem at a Glance

The current local ecosystem contains these applications:

  ---------------------------------------------------------------------------------
  \#            Application                 Port Type             Main
                                                                  Responsibility
  ------------- -------------- ----------------- ---------------- -----------------
  1             MySQL                     `3306` Infrastructure   Persistent
                                                                  databases

  2             EMS Service               `8761` Spring Boot      Eureka service
                Registry                                          discovery

  3             EMS                       `8081` Microservice     Notifications +
                Notification                                      notification
                Service                                           persistence

  4             EMS Auth                  `8083` Microservice     Registration,
                Service                                           login and JWT
                                                                  generation

  5             EMS Backend               `8080` Microservice     Employee,
                                                                  Department and
                                                                  Office APIs

  6             EMS API                   `8082` Gateway          Browser-facing
                Gateway                                           API entry point

  7             React Frontend            `3000` UI               User interface
  ---------------------------------------------------------------------------------

### High-level architecture

``` text
                         ┌─────────────────────┐
                         │   React Frontend    │
                         │      :3000          │
                         └──────────┬──────────┘
                                    │
                                    ▼
                         ┌─────────────────────┐
                         │    API Gateway      │
                         │      :8082          │
                         └──────────┬──────────┘
                                    │
                    ┌───────────────┴───────────────┐
                    │                               │
                    ▼                               ▼
          ┌──────────────────┐            ┌──────────────────┐
          │   EMS Backend    │            │   Auth Service   │
          │      :8080       │            │      :8083       │
          └────────┬─────────┘            └──────────────────┘
                   │
                   │ service discovery
                   ▼
          ┌──────────────────┐
          │    Eureka        │
          │      :8761       │
          │ Service Registry │
          └──────────────────┘
                   ▲
                   │
          ┌────────┴─────────┐
          │                  │
          │                  │ registration
          ▼                  ▼
 ┌──────────────────┐
 │ Notification     │
 │ Service :8081    │
 └──────────────────┘

             All application data
                    │
                    ▼
              ┌──────────┐
              │  MySQL   │
              │  :3306   │
              └──────────┘
```

> **Important:** The Gateway is currently still using **static
> `http://localhost:...` routes**. Eureka-based Gateway routing using
> `lb://...` is the next planned architecture step.

------------------------------------------------------------------------

# 2. Recommended Startup Sequence

For a normal local development session, start services in this order:

``` text
1. MySQL
      ↓
2. Eureka Service Registry
      ↓
3. Notification Service
      ↓
4. Auth Service
      ↓
5. EMS Backend
      ↓
6. API Gateway
      ↓
7. React Frontend
```

You can think of this as:

``` text
DATABASE
   ↓
SERVICE REGISTRY
   ↓
MICROSERVICES
   ↓
API GATEWAY
   ↓
UI
```

------------------------------------------------------------------------

# 3. Step 1 --- Start MySQL

## Responsibility

MySQL provides persistent storage for the EMS ecosystem.

The current project uses separate databases for the major services.

### EMS Backend database

``` text
javaguide_ems
```

Used for:

-   Employees
-   Departments
-   Offices
-   Addresses
-   Experiences
-   Other EMS business data

### Auth Service database

``` text
ems_auth_service
```

Used for:

-   Users
-   Roles
-   Authentication-related data

### Notification Service database

``` text
ems_notifications
```

Used for:

-   Notification records
-   Notification service data

## Default local configuration

``` properties
spring.datasource.url=jdbc:mysql://localhost:3306/javaguide_ems
spring.datasource.username=root
spring.datasource.password=root
```

The other services use their respective database names.

## Why start MySQL first?

The Spring Boot applications need their databases when they initialize
JPA/Hibernate and establish datasource connections.

If MySQL is not running, services that depend on it may fail during
startup.

### Verify

Make sure MySQL is listening on:

``` text
localhost:3306
```

------------------------------------------------------------------------

# 4. Step 2 --- Start Eureka Service Registry

## Application

``` text
ems-service-registry
```

## Port

``` text
8761
```

## Responsibility

Eureka is the **service registry**.

It keeps track of registered service instances.

Current registered applications include:

``` text
EMS-AUTH-SERVICE
EMS-BACKEND
EMS-NOTIFICATION-SERVICE
```

## Configuration

``` properties
spring.application.name=ems-service-registry
server.port=8761

eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
```

The registry does not register itself with another Eureka server.

## Why start it early?

The other Eureka-enabled services use:

``` text
http://localhost:8761/eureka/
```

as their registry.

Therefore, starting Eureka before the registered services gives them a
registry to register with immediately.

## Verify

Open:

``` text
http://localhost:8761
```

You should see the Eureka dashboard.

Initially, the dashboard may have no application instances.

That is normal.

As the other services start, they should appear as `UP`.

------------------------------------------------------------------------

# 5. Step 3 --- Start EMS Notification Service

## Application

``` text
ems-notification-service
```

## Port

``` text
8081
```

## Context path

``` text
/notification-service
```

Therefore its local base URL is:

``` text
http://localhost:8081/notification-service
```

## Responsibility

This service handles notification-related operations and persists
notification data in:

``` text
ems_notifications
```

## Eureka configuration

``` properties
spring.application.name=ems-notification-service

eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
eureka.client.register-with-eureka=true
eureka.client.fetch-registry=true
```

## Why start it before EMS Backend?

EMS Backend can discover Notification Service only when Notification
Service is registered with Eureka.

The backend does not need to know:

``` text
localhost:8081
```

for service discovery.

Instead it uses the logical service name:

``` text
EMS-NOTIFICATION-SERVICE
```

## Verify

Check the Eureka dashboard:

``` text
http://localhost:8761
```

You should see:

``` text
EMS-NOTIFICATION-SERVICE    UP
```

------------------------------------------------------------------------

# 6. Step 4 --- Start EMS Auth Service

## Application

``` text
ems-auth-service
```

## Port

``` text
8083
```

## Current context path

``` text
/ems-dev/v1
```

## Responsibility

This service owns authentication-related functionality:

-   User registration
-   User login
-   Password verification
-   Role assignment
-   JWT generation

## Important endpoint examples

Registration:

``` text
POST http://localhost:8083/ems-dev/v1/api/auth/register
```

Login:

``` text
POST http://localhost:8083/ems-dev/v1/api/auth/login
```

## Eureka configuration

``` properties
spring.application.name=ems-auth-service

eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
eureka.client.register-with-eureka=true
eureka.client.fetch-registry=true
```

## Database

``` text
ems_auth_service
```

## Verify

Eureka should show:

``` text
EMS-AUTH-SERVICE    UP
```

------------------------------------------------------------------------

# 7. Step 5 --- Start EMS Backend

## Application

``` text
ems-backend
```

## Port

``` text
8080
```

## Context path

``` text
/ems-dev/v1
```

Base URL:

``` text
http://localhost:8080/ems-dev/v1
```

## Responsibility

This is the main business microservice.

It currently owns APIs for:

-   Employees
-   Departments
-   Offices

It also contains the business-side notification integration.

## Database

``` text
javaguide_ems
```

## Eureka configuration

``` properties
spring.application.name=ems-backend

eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
eureka.client.register-with-eureka=true
eureka.client.fetch-registry=true
```

## Notification discovery

The backend no longer relies on a fixed notification-service URL.

It calls the logical service:

``` text
EMS-NOTIFICATION-SERVICE
```

Conceptually:

``` text
EMS Backend
     │
     │ logical service name
     ▼
EMS-NOTIFICATION-SERVICE
     │
     ▼
Eureka / LoadBalancer
     │
     ▼
Notification Service instance
```

## Spring Cloud LoadBalancer

The backend uses a load-balanced `RestClient.Builder`.

Its responsibility is to resolve:

``` text
EMS-NOTIFICATION-SERVICE
```

to an available service instance.

## Spring Retry

Notification calls also use Spring Retry.

The current configuration includes:

``` properties
notification.retry.max-attempts=3
notification.retry.backoff-delay=1000
```

So:

``` text
Discovery
    ↓
Select instance
    ↓
HTTP request
    ↓
Failure?
    ↓
Retry according to configuration
```

## Important transaction behavior

Employee creation uses:

``` text
@TransactionalEventListener(AFTER_COMMIT)
```

Therefore:

``` text
Employee transaction
       ↓
     COMMIT
       ↓
AFTER_COMMIT event
       ↓
Notification call
```

A notification failure does not roll back the already-committed employee
transaction.

------------------------------------------------------------------------

# 8. Step 6 --- Start API Gateway

## Application

``` text
ems-api-gateway
```

## Port

``` text
8082
```

The Gateway is the **browser-facing backend entry point**.

The React application should call:

``` text
http://localhost:8082
```

rather than directly calling the backend services.

## Current routes

### Auth

``` text
/ems-dev/v1/api/auth/**
        ↓
localhost:8083
```

### Employees

``` text
/ems-dev/v1/api/employees/**
        ↓
localhost:8080
```

### Departments

``` text
/ems-dev/v1/api/departments/**
        ↓
localhost:8080
```

### Offices

``` text
/ems-dev/v1/api/offices/**
        ↓
localhost:8080
```

### Backend Actuator

``` text
/ems-dev/v1/actuator/**
        ↓
localhost:8080
```

## Current architectural limitation

The Gateway is **not yet using Eureka for routing**.

It still has routes such as:

``` properties
spring.cloud.gateway.server.webflux.routes[1].uri=http://localhost:8080
```

The next planned improvement is:

``` text
Gateway
   ↓
Eureka
   ↓
EMS Backend instance
```

using discovery-based routes such as:

``` text
lb://EMS-BACKEND
```

This should be implemented as a separate learning milestone.

------------------------------------------------------------------------

# 9. Step 7 --- Start React Frontend

## Application

React + Vite + Bootstrap

## Port

``` text
3000
```

## Responsibility

The frontend provides the user interface for the EMS.

It communicates with the backend ecosystem through the API Gateway.

## Current API base URL

``` properties
VITE_API_BASE_URL=http://localhost:8082/ems-dev/v1/api
```

## Actuator base URL

``` properties
VITE_ACTUATOR_BASE_URL=http://localhost:8082/ems-dev/v1/actuator
```

Therefore:

``` text
React :3000
    │
    ▼
Gateway :8082
    │
    ├── Auth Service :8083
    │
    └── EMS Backend :8080
              │
              ▼
       Notification Service :8081
```

------------------------------------------------------------------------

# 10. Complete Startup Checklist

Use this every time you start the project.

## Infrastructure

-   [ ] MySQL is running
-   [ ] Port `3306` is available
-   [ ] Required databases exist

## Service Registry

-   [ ] Start `ems-service-registry`
-   [ ] Port `8761` is available
-   [ ] Open `http://localhost:8761`

## Notification Service

-   [ ] Start `ems-notification-service`
-   [ ] Port `8081` is available
-   [ ] Eureka shows `EMS-NOTIFICATION-SERVICE UP`

## Auth Service

-   [ ] Start `ems-auth-service`
-   [ ] Port `8083` is available
-   [ ] Eureka shows `EMS-AUTH-SERVICE UP`

## EMS Backend

-   [ ] Start `ems-backend`
-   [ ] Port `8080` is available
-   [ ] Eureka shows `EMS-BACKEND UP`

## API Gateway

-   [ ] Start `ems-api-gateway`
-   [ ] Port `8082` is available
-   [ ] Gateway starts without route/configuration errors

## Frontend

-   [ ] Start React/Vite
-   [ ] Port `3000` is available
-   [ ] Browser opens the EMS UI
-   [ ] Frontend API base URL points to `8082`

------------------------------------------------------------------------

# 11. Recommended Terminal Layout

Because there are several processes, a useful local setup is:

``` text
Terminal 1
└── MySQL
    └── Database server

Terminal 2
└── ems-service-registry
    └── :8761

Terminal 3
└── ems-notification-service
    └── :8081

Terminal 4
└── ems-auth-service
    └── :8083

Terminal 5
└── ems-backend
    └── :8080

Terminal 6
└── ems-api-gateway
    └── :8082

Terminal 7
└── React frontend
    └── :3000
```

This makes debugging easier because each application has its own
console.

------------------------------------------------------------------------

# 12. Dependency Mental Model

Do not think of the services only as a numbered startup list.

Think about their dependencies.

``` text
                     MySQL
                       │
             ┌─────────┼─────────┐
             │         │         │
             ▼         ▼         ▼
          Auth       Backend   Notification
             │         │         │
             └────┬────┴────┬────┘
                  │         │
                  ▼         ▼
                Eureka Service Registry
                       │
                       ▼
                 API Gateway
                       │
                       ▼
                  React UI
```

There is an important distinction:

### Runtime dependency

A service may technically be able to start even if another service is
temporarily unavailable.

For example, the EMS Backend can start while Notification Service is
unavailable.

### Functional dependency

The operation may still need the other service.

For example:

``` text
Employee creation
      ↓
Employee DB transaction
      ↓
AFTER_COMMIT event
      ↓
Notification Service
```

The employee transaction can commit even if notification delivery fails
because notification is triggered after commit.

------------------------------------------------------------------------

# 13. What Each Service Does --- Interview Version

## Eureka Server

> "Eureka acts as the service registry. Services register themselves,
> and clients can discover service instances using logical service IDs."

## Auth Service

> "Auth Service owns user registration and login and issues JWT tokens.
> The EMS Backend validates the JWT for protected business APIs."

## EMS Backend

> "EMS Backend owns the core employee-management business APIs such as
> employees, departments and offices. It also consumes Notification
> Service using service discovery."

## Notification Service

> "Notification Service owns notification processing and persistence. It
> is independently deployable and discoverable through Eureka."

## API Gateway

> "The API Gateway is the browser-facing entry point. It routes frontend
> requests to backend microservices and centralizes concerns such as
> CORS. Its routes are currently static; Eureka-based Gateway routing is
> the next step."

## React Frontend

> "The React frontend provides the UI and communicates with the backend
> ecosystem through the API Gateway."

## MySQL

> "MySQL provides persistent storage, with separate databases used by
> the backend, authentication service and notification service."

------------------------------------------------------------------------

# 14. Typical User Login Flow

``` text
React
  │
  │ POST /api/auth/login
  ▼
API Gateway :8082
  │
  │ static route
  ▼
Auth Service :8083
  │
  │ validate credentials
  │
  ▼
JWT response
  │
  ▼
React
  │
  │ stores JWT
  ▼
Subsequent protected API calls
```

The frontend then sends the JWT in:

``` http
Authorization: Bearer <JWT>
```

------------------------------------------------------------------------

# 15. Typical Employee Creation Flow

``` text
React
  │
  │ POST /employees
  │ + JWT
  ▼
API Gateway
  │
  ▼
EMS Backend
  │
  ├── validate JWT
  │
  ├── save Employee
  │
  ├── save related data
  │
  └── COMMIT
          │
          ▼
     AFTER_COMMIT
          │
          ▼
 NotificationClient
          │
          │ EMS-NOTIFICATION-SERVICE
          ▼
     Eureka / LoadBalancer
          │
          ▼
 Notification Service
          │
          ▼
       HTTP 201
```

------------------------------------------------------------------------

# 16. If Something Fails --- Troubleshooting Order

When the UI is not working, troubleshoot from the bottom of the
dependency chain upward.

## Check 1 --- MySQL

``` text
Is MySQL running on 3306?
```

## Check 2 --- Eureka

``` text
http://localhost:8761
```

Check whether required services are `UP`.

## Check 3 --- Auth Service

Test login directly if necessary:

``` text
POST http://localhost:8083/ems-dev/v1/api/auth/login
```

## Check 4 --- EMS Backend

Check whether:

``` text
8080
```

is listening and the application started successfully.

## Check 5 --- Notification Service

Check:

``` text
8081
```

and verify its Eureka registration.

## Check 6 --- Gateway

Check:

``` text
8082
```

and inspect route configuration.

## Check 7 --- React

Check:

``` text
3000
```

and verify:

``` text
VITE_API_BASE_URL=http://localhost:8082/ems-dev/v1/api
```

------------------------------------------------------------------------

# 17. Port Conflict Quick Reference

      Port Expected application
  -------- ----------------------
    `3000` React/Vite
    `3306` MySQL
    `8080` EMS Backend
    `8081` Notification Service
    `8082` API Gateway
    `8083` Auth Service
    `8761` Eureka Server

If an application fails with an address/port binding error, check
whether another process is already using its port.

------------------------------------------------------------------------

# 18. Current Architecture vs. Target Architecture

## Current

``` text
React
  ↓
API Gateway
  ↓
fixed HTTP routes
  ↓
EMS Backend

EMS Backend
  ↓
Eureka discovery
  ↓
Notification Service
```

## Planned

``` text
                         ┌──────────────┐
                         │    Eureka    │
                         │   Registry   │
                         └──────┬───────┘
                                │
                ┌───────────────┼────────────────┐
                │               │                │
                ▼               ▼                ▼
             Gateway          Backend        Notification
                ▲               ▲
                │               │
                └───────┬───────┘
                        │
                     React UI
```

Eventually:

``` text
React
  ↓
API Gateway
  ↓
lb://EMS-BACKEND
  ↓
Eureka
  ↓
EMS Backend instance
```

And:

``` text
EMS Backend
  ↓
lb://EMS-NOTIFICATION-SERVICE
  ↓
Eureka
  ↓
Notification Service instance
```

------------------------------------------------------------------------

# 19. The One Rule to Remember

When starting the complete project locally:

``` text
MySQL
  ↓
Eureka
  ↓
Microservices
  ↓
Gateway
  ↓
Frontend
```

When debugging:

``` text
Start from the dependency closest to the infrastructure
and move upward toward the UI.
```

When explaining the architecture in an interview:

``` text
Logical service identity
        ↓
Service discovery
        ↓
Instance selection
        ↓
HTTP communication
        ↓
Retry / resilience
```

------------------------------------------------------------------------

# 20. Current Project Milestone

### Completed

-   [x] Separate Auth Service
-   [x] API Gateway
-   [x] Gateway centralized CORS
-   [x] Notification Service
-   [x] Spring Application Events
-   [x] AFTER_COMMIT notification processing
-   [x] Spring Retry
-   [x] Eureka Server
-   [x] EMS Backend Eureka registration
-   [x] Auth Service Eureka registration
-   [x] Notification Service Eureka registration
-   [x] Backend → Notification Service discovery
-   [x] Spring Cloud LoadBalancer for notification calls
-   [x] Successful end-to-end discovery + notification test

### Next

-   [ ] Make API Gateway an Eureka client
-   [ ] Replace static Gateway URLs with `lb://...` routes
-   [ ] Test multiple service instances through Gateway
-   [ ] Continue with resilience patterns such as timeout/circuit
    breaker
-   [ ] Later evaluate asynchronous messaging, Outbox, Kafka/RabbitMQ,
    etc.

------------------------------------------------------------------------

## Quick Start

If you just want the shortest possible checklist:

``` text
1. Start MySQL
2. Start ems-service-registry       :8761
3. Start ems-notification-service   :8081
4. Start ems-auth-service           :8083
5. Start ems-backend                :8080
6. Start ems-api-gateway            :8082
7. Start React frontend             :3000

8. Open Eureka:
   http://localhost:8761

9. Open the UI:
   http://localhost:3000
```

**Expected Eureka applications:**

``` text
EMS-AUTH-SERVICE           UP
EMS-BACKEND                UP
EMS-NOTIFICATION-SERVICE   UP
```

> Keep this file in the EMS repository as a local-development runbook.
> Update the startup sequence whenever a new infrastructure component,
> microservice, or dependency is introduced.
