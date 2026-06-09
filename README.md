# Patient Management System

A cloud-native **microservices application** built with Java 21 and Spring Boot 3.x, designed to manage patient data, appointments, billing, and healthcare workflows across independently deployable modules.

> **Status: Active Development** — Phase 1 (service discovery, auth, gateway, notifications) and Phase 2 (circuit breakers, config server, distributed tracing) complete.

---

## Architecture Overview

```
                        ┌─────────────────────┐
  All external          │    gateway-module    │  JWT validation
  requests    ─────────▶│    (port 8888)       │  + routing
                        └──────────┬──────────┘
                                   │  lb:// (Eureka)
               ┌───────────────────┼────────────────────┐
               ▼                   ▼                    ▼
    ┌──────────────────┐  ┌─────────────────┐  ┌─────────────────┐
    │  patient-module  │  │ booking-module  │  │department-module│
    │   (port 8080)    │  │  (port 8081)    │  │  (port 8082)    │
    └──────────────────┘  └────────┬────────┘  └─────────────────┘
                                   │ Feign (with Resilience4j CB)
                          ┌────────┘
                          │  Kafka: booking.created
                 ┌────────▼────────┐
                 │                 │
       ┌─────────▼───────┐  ┌──────▼──────────────┐
       │ billing-module  │  │ notification-module  │
       │  (port 8086)    │  │   (port 8087)        │
       └─────────────────┘  └─────────────────────┘

  ┌──────────────────┐   ┌──────────────┐   ┌──────────────────┐
  │  eureka-module   │   │ config-module│   │   auth-module    │
  │   (port 8761)    │   │  (port 8889) │   │   (port 8085)    │
  └──────────────────┘   └──────────────┘   └──────────────────┘
```

### Communication patterns

- **REST (OpenFeign)** — booking-module calls patient-module and department-module synchronously to validate bookings
- **Kafka** — booking-module publishes `booking.created` events; billing-module and notification-module consume them independently
- **Eureka** — all modules register and discover each other by name; no hardcoded URLs
- **Spring Cloud Gateway** — single entry point; validates JWT on every request before routing

---

## Modules

| Module | Port | Role |
|---|---|---|
| `patient-module` | 8080 | Patient CRUD, email lookup, pagination |
| `department-module` | 8082 | Departments, doctors, treatments |
| `booking-module` | 8081 | Appointment scheduling, slot validation, Kafka producer |
| `billing-module` | 8086 | Kafka consumer, auto-creates invoices on booking |
| `auth-module` | 8085 | JWT register/login, BCrypt, roles |
| `gateway-module` | 8888 | Spring Cloud Gateway, JWT filter, Eureka load-balanced routing |
| `notification-module` | 8087 | Kafka consumer, sends booking email via SMTP |
| `eureka-module` | 8761 | Netflix Eureka service registry |
| `config-module` | 8889 | Spring Cloud Config Server (shared + per-module config) |

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.5.x |
| API Gateway | Spring Cloud Gateway (reactive/WebFlux) |
| Service Discovery | Netflix Eureka (Spring Cloud) |
| Synchronous communication | Spring Cloud OpenFeign |
| Circuit breakers | Resilience4j (via spring-cloud-starter-circuitbreaker-resilience4j) |
| Async messaging | Apache Kafka |
| Authentication | JWT (JJWT 0.12.6, HS256) + Spring Security |
| Configuration | Spring Cloud Config Server (native backend) |
| Distributed tracing | Micrometer Tracing + Brave + Zipkin |
| Persistence | Spring Data JPA + Hibernate + MySQL 8.0 |
| Object mapping | MapStruct 1.6.3 |
| Boilerplate reduction | Lombok 1.18.30 |
| API docs | SpringDoc OpenAPI 2.x (patient-module only) |
| Containerisation | Docker (multi-stage builds) |
| Local stack | Docker Compose |
| Build tool | Maven 3.9.9 |

---

## API Endpoints

### Auth (public — no JWT required)
```
POST  /api/v1/auth/register   Register a new user (email, password, role)
POST  /api/v1/auth/login      Login, returns JWT token
```

### Patients
```
GET   /api/v1/patients              List patients (paginated, searchable)
GET   /api/v1/patients/{id}         Get by UUID
GET   /api/v1/patients/{email}      Get by email
POST  /api/v1/patients              Create patient
PUT   /api/v1/patients/{id}         Update patient
```

### Bookings
```
POST  /api/v1/bookings              Create booking
GET   /api/v1/bookings/{id}         Get booking by UUID
GET   /api/v1/bookings/patient/{id} All bookings for a patient
PUT   /api/v1/bookings/{id}/cancel  Cancel booking
PUT   /api/v1/bookings/{id}/complete Complete booking
POST  /api/v1/bookings/{id}/rebook  Rebook appointment
```

### Departments, Doctors, Treatments
```
POST  /api/departments              Create department
PUT   /api/departments/{id}         Update department
GET   /api/departments/{id}         Department details (with doctors/treatments)
GET   /api/departments              List active departments

POST  /api/doctors                  Create doctor
PUT   /api/doctors/{id}             Update doctor
GET   /api/doctors/{id}             Get doctor by ID
GET   /api/doctors/department/{id}  Doctors by department
GET   /api/doctors/{id}/treatments  Doctor with their treatments

POST  /api/treatments               Create treatment
PUT   /api/treatments/{id}          Update treatment
GET   /api/treatments/{id}          Get treatment by ID
GET   /api/treatments/department/{id} Treatments by department
```

All requests except `/api/v1/auth/**` must include `Authorization: Bearer <token>`.

---

## Kafka Event Flow

```
booking-module
  │
  │── publishes ──▶  booking.created  ──▶  billing-module   (creates invoice)
                                      └──▶  notification-module  (sends email)
```

**Event payload** (`BookingCreatedEvent`):
```json
{
  "bookingId": "uuid",
  "patientId": "uuid",
  "doctorId": 1,
  "treatmentId": 1,
  "appointmentDate": "2026-06-10",
  "startTime": "09:00",
  "endTime": "09:30",
  "amount": 500.0
}
```

Topic: `booking.created` — 3 partitions, 1 replica.
Both consumers check for existing records before processing (idempotent).

---

## Booking Validation Flow

A booking creation goes through these steps before being saved:

1. Patient exists (Feign → patient-module, circuit breaker protected)
2. Doctor is active (Feign → department-module, circuit breaker protected)
3. Treatment exists (Feign → department-module)
4. Appointment time is valid
5. Time slot is free (no overlap)
6. Doctor weekly booking limit not exceeded (10–15 per week)
7. Booking saved → Kafka event published

If patient-module or department-module is unavailable, the circuit breaker opens and returns HTTP 503 immediately without cascading failures.

---

## Project Structure

```
PatientManagement/
├── patient-module/          # Patient CRUD (port 8080)
├── department-module/       # Departments, doctors, treatments (port 8082)
├── booking-module/          # Appointment scheduling (port 8081)
├── billing-module/          # Invoice creation via Kafka (port 8086)
├── auth-module/             # JWT authentication (port 8085)
├── gateway-module/          # API Gateway with JWT filter (port 8888)
├── notification-module/     # Email notifications via Kafka (port 8087)
├── eureka-module/           # Service registry (port 8761)
├── config-module/           # Centralised config server (port 8889)
├── docker-compose.yml       # Full local stack
├── api-requests/            # .http test files
└── grpc-requests/           # gRPC test stubs
```

Each module follows the same internal package layout:
```
se.biplob.<module>/
  ├── controller/      REST endpoints
  ├── model/           JPA entities and enums
  ├── dto/             Request/response DTOs
  ├── service/         Business logic
  ├── repository/      Spring Data JPA
  ├── mapper/          MapStruct mappers
  ├── exceptions/      Custom exceptions + GlobalExceptionHandler
  ├── kafka/           Producer/consumer + events
  ├── feignclient/     Feign interfaces (booking-module only)
  └── config/          Spring configuration beans
```

---

## Getting Started

### Prerequisites

- Java 21
- Docker and Docker Compose
- Maven 3.9+

### Run the full stack with Docker Compose

```bash
git clone https://github.com/roy6767/PatientManagement.git
cd PatientManagement

# Set SMTP credentials for email notifications (optional)
export MAIL_USERNAME=your-email@gmail.com
export MAIL_PASSWORD=your-app-password

# Start everything (MySQL, Kafka, Zipkin, all 9 modules)
docker-compose up -d
```

Startup order is managed automatically via health checks:
`MySQL + Kafka` → `eureka-module` → `config-module` → all business modules

### Run individual modules locally

Start infrastructure first (MySQL, Kafka, Zookeeper, Zipkin), then eureka-module and config-module, then any business module:

```bash
# Eureka (start first)
cd eureka-module && mvn spring-boot:run

# Config Server (start second)
cd config-module && mvn spring-boot:run

# Then any business module, e.g.:
cd patient-module && mvn spring-boot:run
```

### First request — get a token

```bash
# Register
curl -X POST http://localhost:8888/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@example.com","password":"secret","role":"ADMIN"}'

# Login
curl -X POST http://localhost:8888/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@example.com","password":"secret"}'
# Returns: { "token": "eyJ...", "email": "...", "role": "ADMIN" }

# Use token
curl http://localhost:8888/api/v1/patients \
  -H "Authorization: Bearer eyJ..."
```

All requests go through port **8888** (gateway-module).

---

## Configuration

### Centralised config (config-module)

Shared settings served to all modules at startup:

| Property | Value |
|---|---|
| Eureka URL | `http://localhost:8761/eureka/` |
| Zipkin endpoint | `http://localhost:9411/api/v2/spans` |
| JWT secret | via `jwt.secret` |
| Tracing sample rate | 100% |

Per-module overrides live in `config-module/src/main/resources/config-repo/`.

### Environment variables (Docker)

| Variable | Used by | Purpose |
|---|---|---|
| `MAIL_USERNAME` | notification-module | SMTP sender account |
| `MAIL_PASSWORD` | notification-module | SMTP app password |
| `ADMIN_EMAIL` | notification-module | Notification recipient |
| `SPRING_DATASOURCE_URL` | all DB modules | Override MySQL URL |
| `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE` | all | Override Eureka URL |

---

## Distributed Tracing

All modules report traces to Zipkin (100% sampling in development).

Open the Zipkin UI at `http://localhost:9411` after starting the stack to visualise request flows across modules.

---

## Roadmap

### Phase 3 — Data Integrity (next)
- [ ] Separate database per module (currently all share `patients_management`)
- [ ] Flyway migrations (replace `ddl-auto=update`)
- [ ] Billing REST endpoints — invoice queries, payment processing
- [ ] Secrets management — externalise DB credentials and JWT secret

### Phase 4 — Observability and Quality
- [ ] Unit and integration tests (zero currently)
- [ ] Swagger/OpenAPI on all modules (currently only patient-module)
- [ ] Prometheus + Grafana metrics dashboards
- [ ] ELK stack for centralised logging
- [ ] Fix API prefix inconsistency (`/api/v1/` vs `/api/`)

### Phase 5 — Production Hardening
- [ ] Kubernetes manifests (Deployment, Service, Ingress, HPA, ConfigMap, Secrets)
- [ ] CI/CD pipeline (GitHub Actions)
- [ ] Container registry (Docker Hub / ECR)
- [ ] Load testing (k6 or Gatling)

---

## Author

**Biplob Roy** — Backend Java Developer
[LinkedIn](https://www.linkedin.com/in/biplob-roy-463b55143) | [GitHub](https://github.com/roy6767) | biplob.roy.prodip@gmail.com
