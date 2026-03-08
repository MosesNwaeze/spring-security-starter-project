# Security Starter Project

A multi-module Spring Boot security starter with JWT authentication, role-based authorization, and reusable security components.

## Features

- JWT-based authentication
- Role-based authorization (ADMIN, USER)
- Multiple roles per user
- Auto-configuration for easy integration
- Request logging
- Custom security exceptions
- Comprehensive test coverage

## Prerequisites

- Java 17 or higher
- Gradle 7.x or higher


## Quick Start

### 1. run git clone https://github.com/MosesNwaeze/spring-security-starter-project

### 2. cd spring-security-starter-project

### 3. run ./gradlew clean build

### 4. cd sample-application/build/libs 

### 5. run java -jar sample-application-0.0.1-SNAPSHOT.jar


The application will start on http://localhost:8080

### API Documentation

1. Authentication Login

curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin@example.com",
    "password": "admin123"
  }'

Response:
json

{
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJVU0VSIl0sImV4cCI6MTc3Mjk3NDIzNywidXNlcklkIjoxLCJzdWIiOiJtb2xsYXNtYXJ0MUBnbWFpbC5jb20iLCJpc3MiOiJNT04iLCJpYXQiOjE3NzI5NzA2Mzh9.BNA9-xm2M_gAmUhw0QlbalARV998e56ZH2FUtm0Oyik",
    "refreshToken": "a885a532-2597-4015-bd33-2763b51684ef"
}

2. Admin Endpoints (Requires ADMIN role)

curl -X GET http://localhost:8080/api/admin/users \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
  
Response (json):
{
    "data": [
        {
            "userId": 2,
            "username": "mollasmart@gmail.com",
            "roles": [
                "ADMIN"
            ],
            "createdAt": "2026-03-08T22:39:14.114334",
            "updatedAt": "2026-03-08T22:39:14.114334"
        },
        {
            "userId": 1,
            "username": "mollasmart1@gmail.com",
            "roles": [
                "USER"
            ],
            "createdAt": "2026-03-08T22:37:39.981353",
            "updatedAt": "2026-03-08T22:37:39.981353"
        }
    ],
    "totalPages": 1,
    "totalElements": 2,
    "page": 0,
    "size": 10,
    "first": true,
    "last": true
} 
  

3. Create New User

curl -X POST http://localhost:8080/api/admin/users \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -d '{
    "username": "newuser@example.com",
    "password": "password123",
    "roles": ["USER"]
  }'
  
Response (json): 
{
    "userId": 1,
    "username": "mollasmart1@gmail.com",
    "roles": [
        "USER"
    ],
    "createdAt": "2026-03-08T22:37:39.9813528",
    "updatedAt": "2026-03-08T22:37:39.9813528"
} 

### Design Decisions and Trade-offs
1. Multi-module Architecture

    Decision: Separated core security logic from sample application

    Rationale: Creates reusable security starter that can be included in other projects

    Trade-off: More complex build configuration vs better separation of concerns

2. JWT with Embedded Roles

    Decision: Store user roles directly in JWT claims

    Rationale: Stateless authentication, no database lookup for each request

    Trade-off: Cannot revoke tokens without additional blacklist mechanism

3. Role-based Authorization

    Decision: Use Spring Security's role hierarchy with ROLE_ prefix

    Rationale: Leverages Spring's built-in authorization capabilities

    Trade-off: More complex token payload vs fine-grained control

4. Auto-configuration Approach

    Decision: Use Spring Boot's @AutoConfiguration

    Rationale: Zero-configuration integration for consumer applications

    Trade-off: Less explicit control vs easier adoption

5. Entity Audit Fields

    Decision: BaseEntity with @PrePersist and @PreUpdate

    Rationale: Automatic timestamp management without service layer code

    Trade-off: Tied to JPA vs database triggers

6. In-memory Database

    Decision: H2 database for development/testing

    Rationale: Quick setup, perfect for demonstration

    Trade-off: Data loss on restart vs easy testing

7. Multiple Roles Support

    Decision: Store roles as List<String> in entity and token

    Rationale: Flexible role assignment, future-proof

    Trade-off: More complex queries vs single role simplicity

8. Password Encryption

    Decision: BCryptPasswordEncoder with configurable strength

    Rationale: Industry standard, secure against rainbow tables

    Trade-off: Computational overhead vs security

