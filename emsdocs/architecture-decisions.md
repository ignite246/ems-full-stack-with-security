# Architecture Decisions

## 1. CORS is handled by API Gateway

### Decision

CORS is configured at the API Gateway rather than individually
in backend controllers.

### Why?

The React frontend communicates with the API Gateway, which is the
browser-facing entry point of the application.

The request flow is:

React
↓
API Gateway
↓
EMS Backend

Therefore, the Gateway is responsible for handling browser CORS.

### What was changed?

Controller-level `@CrossOrigin("*")` annotations were removed from
backend controllers.

For example:

```java
@RestController
@RequestMapping("/api/departments")
public class DepartmentController {