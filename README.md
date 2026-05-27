# 🏥 Patient Management System

A cloud-native **microservices application** built with Java and Spring Boot, 
designed to manage patient data and healthcare workflows across independently 
deployable services.

> 🚧 **Status: In Active Development** — core services built, deployment to AWS EKS in progress.

---

## 🧩 Architecture Overview

The system follows a microservices architecture where each service owns its 
domain and communicates through well-defined contracts.

```
┌─────────────────────────────────────────────────────┐
│                   API Gateway                        │
└────────────────────┬────────────────────────────────┘
                     │
        ┌────────────┼────────────┐
        ▼            ▼            ▼
┌──────────────┐ ┌──────────────┐ ┌──────────────┐
│   Patient    │ │ Notification │ │   Analytics  │
│   Service    │ │   Service    │ │   Service    │
└──────┬───────┘ └──────┬───────┘ └──────────────┘
       │                │
       │ gRPC           │ Kafka Events
       ▼                ▼
┌──────────────┐ ┌──────────────┐
│   MySQL DB   │ │  Kafka Bus   │
└──────────────┘ └──────────────┘
```

### Key architectural decisions

- **gRPC** for synchronous inter-service communication — strongly typed 
  contracts with Protocol Buffers, lower latency than REST for internal calls
- **Kafka** for asynchronous event streaming — services publish and consume 
  domain events without tight coupling
- **Docker + Kubernetes** for containerisation and orchestration
- **AWS EKS** as the target deployment platform

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.x |
| Inter-service Communication | gRPC (Protocol Buffers) |
| Event Streaming | Apache Kafka |
| Database | MySQL |
| Containerisation | Docker |
| Orchestration | Kubernetes (target: AWS EKS) |
| Cloud | AWS (EKS, RDS, ECR) |
| Build Tool | Maven |
| Version Control | Git |

---

## 📁 Project Structure

```
PatientManagement/
├── patient-service/          # Core patient CRUD and domain logic
│   ├── src/
│   │   ├── main/java/
│   │   │   ├── controller/
│   │   │   ├── service/
│   │   │   ├── repository/
│   │   │   └── grpc/         # gRPC server implementation
│   │   └── resources/
│   │       └── proto/        # Protocol Buffer definitions
├── notification-service/     # Handles patient event notifications
│   ├── src/
│   │   └── main/java/
│   │       ├── consumer/     # Kafka event consumers
│   │       └── service/
├── docker-compose.yml        # Local development environment
├── k8s/                      # Kubernetes manifests
│   ├── deployments/
│   ├── services/
│   └── configmaps/
└── README.md
```

---

## ⚙️ Getting Started

### Prerequisites

- Java 21
- Docker & Docker Compose
- Maven 3.8+

### Run locally

```bash
# Clone the repository
git clone https://github.com/roy6767/PatientManagement.git
cd PatientManagement

# Start infrastructure (Kafka, MySQL, Zookeeper)
docker-compose up -d

# Build all services
mvn clean install

# Start patient-service
cd patient-service
mvn spring-boot:run

# Start notification-service (new terminal)
cd notification-service
mvn spring-boot:run
```

---

## 🔌 gRPC Communication

Services communicate internally via gRPC using Protocol Buffer definitions.

```protobuf
// patient.proto
syntax = "proto3";

service PatientService {
  rpc GetPatient (PatientRequest) returns (PatientResponse);
  rpc CreatePatient (CreatePatientRequest) returns (PatientResponse);
}

message PatientRequest {
  string patient_id = 1;
}

message PatientResponse {
  string patient_id = 1;
  string name       = 2;
  string email      = 3;
  string status     = 4;
}
```

---

## 📨 Kafka Event Flow

When a patient record is created or updated, the patient-service publishes 
a domain event that downstream services consume independently.

```
Patient Service                    Kafka                Notification Service
     │                               │                          │
     │── PublishEvent(PatientCreated)─▶                         │
     │                               │── ConsumeEvent ─────────▶│
     │                               │                          │── Send notification
```

**Events published:**
- `patient.created`
- `patient.updated`
- `patient.discharged`

---

## 🚀 Deployment Plan (In Progress)

- [ ] Dockerise all services
- [ ] Write Kubernetes manifests (Deployments, Services, ConfigMaps)
- [ ] Set up AWS EKS cluster
- [ ] Configure AWS RDS for production MySQL
- [ ] Set up AWS ECR for container registry
- [ ] Configure Kubernetes health probes and resource limits
- [ ] CI/CD pipeline via GitHub Actions

---

## 🎯 Why This Project

This project was built to practice and demonstrate real-world backend 
engineering patterns used in production systems:

- Designing services with clear domain boundaries
- Choosing the right communication pattern (gRPC vs Kafka) based on 
  use case — synchronous vs asynchronous
- Infrastructure-as-code mindset with Kubernetes manifests
- Cloud-native deployment thinking with AWS

---

## 👤 Author

**Biplob Roy** — Backend Java Developer  
🔗 [LinkedIn](https://www.linkedin.com/in/biplob-roy-463b55143)  
🐙 [GitHub](https://github.com/roy6767)  
📧 biplob.roy.prodip@gmail.com
