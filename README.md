# Microservices Architecture with Eureka, Spring Cloud Gateway, JWT Security and Prometheus Monitoring

## Overview

This project implements a microservices architecture with seven core services: `ProductService`,`CartService`,`NotificationService`,`OrderService`, `PaymentService`,`ApiGateway`, and `UserService`. The system uses **Spring Cloud Netflix Eureka** for service registration and discovery, **Spring Cloud Gateway** as an API Gateway, and **Prometheus** and visualizing metrics.

## 🛠️ Technologies Used:

- Java 21
- Spring Boot 3.5 +
- Spring Cloud Gateway
- Spring Data JPA
- Spring Web
- Spring Security JWT
- OpenFeign
- Maven
- PostGreSQL database

## Architecture

- **OrderService**: Provides order management functionality.
- **PaymentService**: Manages payment processing and integrates with `OrderService`.
- **UserService**: Handles user authentication and issues secure tokens for client requests.
- **Eureka Server**: Acts as the service registry, where all services register themselves.
- **Spring Cloud Gateway**: The API Gateway that routes incoming client requests to the appropriate microservices.
- **Prometheus**: Used for monitoring service health, request statistics, JVM metrics, and more.

```

                                +---------------------+
                                |   Eureka Server     |
                                | (Service Registry)  |
                                +---------------------+
                                            ^
                                            |
                                            |  Service Discovery
                    +-----------------------+-----------------------+
                    |                                               |
                    |                                               |
            +---------------+                               +----------------+
            | OrderService  |<----------------------------->| PaymentService |
            +---------------+                               +----------------+
                    ^                                               ^
                    |                                               |
                    +---------------+                   +-----------+
                                    |                   |
                            +----------------+      +---------------+
                            | UserService |<----|       Clients     |
                            +----------------+      +---------------+
                                    ^
                                    |
                            +--------------------------------+
                            |        Spring Cloud Gateway    |
                            |     (API Gateway - Proxy)      |
                            +--------------------------------+
                                    ^
                                    |
                                    | Routes client requests
                                    |
                                +----------------+       
                                |    Prometheus  | 
                                | (Scrapes metrics from  |      
                                | Gateway and Services)  |           
                                +------------------------+      
```
## Services

### 1. **Eureka Server**
The **Eureka Server** is responsible for managing service registration and discovery. It allows the microservices to register themselves and discover other services dynamically.

**Running the Eureka Server:**

- mvn spring-boot:run
- Port: 8761
- Browser: http://localhost:8761

### 2. **API Gateway**
The Spring Cloud Gateway acts as a reverse proxy and routes external API requests to the appropriate microservices (`OrderService`, `PaymentService`, and `UserService`).

Gateway Routes:
- **/api/v1/order/**: Routes to OrderService
- **/api/v1/payment/**: Routes to PaymentService
- **/api/v1/user/**: Routes to UserService
- Port: 8080

### 3. **UserService**
The UserService is responsible for authenticating users and securing communication between services. It provides the following features:
- **Authentication**: Validates user credentials and issues JWTs (JSON Web Tokens) for secure communication.
- **Token Validation**: Verifies the integrity and validity of incoming JWTs.
- **Integration**: Works with the API Gateway to authenticate requests and authorize access to microservices.

Endpoints:
- POST /api/user/login: Authenticates the user and issues a JWT.
- GET /api/user/validate: Validates a given JWT.
- Port: 8083

**How It Works:**
- Clients authenticate with UserService to obtain a JWT.
- JWTs are included in the `Authorization` header of subsequent requests.
- The API Gateway verifies JWTs with UserService before routing requests to `OrderService` or `PaymentService`.
- This ensures only authenticated and authorized requests can access the system.

### 4. **OrderService**
The OrderService handles order-related business logic, such as placing, updating, and viewing orders.

Endpoints:
- POST /api/v1/order: Create a new order
- GET /api/v1/order: Get all orders
- Port: 8084

### 5. **PaymentService**
The PaymentService manages payment processing and tracks the payment status of orders.

Endpoints:
- POST /api/v1/payment: Process a payment
- GET /api/v1/payment/{orderId}: Get payment details by order ID
- Port: 8085

### Monitoring
**This project uses Prometheus to scrape metrics from each service to visualize these metrics.**

#### Prometheus Setup
Prometheus is used to scrape metrics from the ***/actuator/prometheus*** endpoint exposed by each service.

### Available Metrics:
- HTTP Requests: http_server_requests_seconds_count
- Gateway Requests: gateway.requests
- JVM Memory Usage: jvm_memory_used_bytes
- Custom Metrics (e.g., number of orders placed, payments processed)

### License
This project is licensed under the MIT License.

### Installation

1. **Clone the repository:**

   ```bash
   git clone https://github.com/awokou/ecommerce-microservices.git
