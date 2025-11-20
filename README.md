# 🐾 The Shire Of Paws - Backend

Backend API for The Shire Of Paws dog adoption management system built with Spring Boot, following best practices and clean architecture principles.

## 📋 Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Getting Started](#getting-started)
- [API Endpoints](#api-endpoints)
- [Testing](#testing)
- [Database](#database)
- [Security](#security)

## ✨ Features

- ✅ Complete CRUD operations for dogs and adoption requests
- ✅ JWT-based authentication for admin users
- ✅ Role-based access control (Admin/Public)
- ✅ Global exception handling
- ✅ Input validation with Bean Validation
- ✅ Pagination and filtering
- ✅ Clean architecture with 3-layer separation
- ✅ MapStruct for DTO mapping
- ✅ Unit tests with JUnit and Mockito
- ✅ H2 database for development
- ✅ PostgreSQL support for production

## 🛠 Tech Stack

- **Java 21**
- **Spring Boot 3.2.0**
- **Spring Security** - JWT authentication
- **Spring Data JPA** - Data persistence
- **MapStruct** - Object mapping
- **Lombok** - Boilerplate reduction
- **H2 Database** - Development
- **PostgreSQL** - Production
- **JUnit 5 & Mockito** - Testing
- **Maven** - Build tool

## 🏗 Architecture

```
theshireofpaws-backend/
│
├── src/main/java/com/theshireofpaws/
│   │
│   ├── TheshireofpawsApplication.java    # Main application class
│   │
│   ├── config/                           # ⚙️ Configuration
│   │   └── DataSeeder.java               # Initial data loader
│   │
│   ├── controller/                       # 🎮 REST Controllers
│   │   ├── DogController.java
│   │   └── AdoptionRequestController.java
│   │
│   ├── dto/                              # 📦 Data Transfer Objects
│   │   ├── request/                      # Input DTOs
│   │   │   ├── DogRequest.java
│   │   │   ├── AdoptionRequestRequest.java
│   │   │   ├── AdminLoginRequest.java
│   │   │   └── UpdateAdoptionStatusRequest.java
│   │   └── response/                     # Output DTOs
│   │       ├── DogResponse.java
│   │       ├── AdoptionRequestResponse.java
│   │       └── JwtResponse.java
│   │
│   ├── entity/                           # 🗄️ JPA Entities
│   │   ├── AdminUser.java
│   │   ├── Dog.java
│   │   ├── AdoptionRequest.java
│   │   └── enums/
│   │       ├── DogStatus.java
│   │       └── AdoptionStatus.java
│   │
│   ├── exception/                        # ⚠️ Exception Handling
│   │   ├── GlobalExceptionHandler.java
│   │   ├── ResourceNotFoundException.java
│   │   ├── BadRequestException.java
│   │   └── ErrorResponse.java
│   │
│   ├── mapper/                           # 🔄 MapStruct Mappers
│   │   ├── DogMapper.java
│   │   └── AdoptionRequestMapper.java
│   │
│   ├── repository/                       # 💾 JPA Repositories
│   │   ├── AdminUserRepository.java
│   │   ├── DogRepository.java
│   │   └── AdoptionRequestRepository.java
│   │
│   ├── security/                         # 🔒 Security Configuration
│   │   ├── SecurityConfig.java
│   │   ├── SecurityConstants.java
│   │   ├── CustomAuthenticationManager.java
│   │   ├── AdminUserDetails.java
│   │   └── filter/
│   │       ├── JWTAuthenticationFilter.java
│   │       └── JWTAuthorizationFilter.java
│   │
│   └── service/                          # 🔧 Business Logic
│       ├── interfaces/
│       │   ├── AdminUserService.java
│       │   ├── DogService.java
│       │   └── AdoptionRequestService.java
│       └── impl/
│           ├── AdminUserServiceImpl.java
│           ├── DogServiceImpl.java
│           └── AdoptionRequestServiceImpl.java
│
└── src/main/resources/
    ├── application.properties            # H2 config (development)
    └── application-postgres.properties   # PostgreSQL config (production)
```

## 🚀 Getting Started

### Prerequisites

- Java 21 or higher
- Maven 3.8+
- (Optional) PostgreSQL 15+ for production

### Installation

1. **Clone the repository**
```bash
git clone 
cd theshireofpaws-backend
```

2. **Build the project**
```bash
mvn clean install
```

3. **Run the application**
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Default Admin Credentials

```
Email: admin@theshireofpaws.com
Password: admin123
```

### Access H2 Console

- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:theshireofpaws`
- Username: `sa`
- Password: (leave empty)

## 📡 API Endpoints

### Authentication

```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "admin@theshireofpaws.com",
  "password": "admin123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "email": "admin@theshireofpaws.com",
  "type": "Bearer"
}
```

### Dogs

#### Get All Dogs (Public)
```http
GET /api/dogs?page=0&size=12&sortBy=createdAt&sortDir=DESC
```

#### Get Dog by ID (Public)
```http
GET /api/dogs/{id}
```

#### Filter Dogs (Public)
```http
GET /api/dogs/filter?status=AVAILABLE&gender=Male&size=Medium
```

#### Create Dog (Admin Only)
```http
POST /api/dogs
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "Buddy",
  "story": "Buddy is a friendly dog looking for a loving home. He enjoys long walks and playing fetch!",
  "gender": "Male",
  "age": 3,
  "size": "Medium",
  "photoUrl": "https://example.com/buddy.jpg",
  "status": "AVAILABLE"
}
```

#### Update Dog (Admin Only)
```http
PUT /api/dogs/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "Buddy Updated",
  "age": 4,
  "status": "IN_PROCESS"
}
```

#### Delete Dog (Admin Only)
```http
DELETE /api/dogs/{id}
Authorization: Bearer {token}
```

### Adoption Requests

#### Submit Adoption Request (Public)
```http
POST /api/adoption-requests
Content-Type: application/json

{
  "requesterName": "John Doe",
  "requesterEmail": "john@example.com",
  "housingType": "House",
  "householdSize": 4,
  "motivation": "I have always loved dogs and have been looking to adopt for a while now. I have a spacious backyard and plenty of time to dedicate to training and caring for a new companion.",
  "daytimeLocation": "I work from home most days, so the dog will have company throughout the day.",
  "dogId": "uuid-here"
}
```

#### Get All Requests (Admin Only)
```http
GET /api/adoption-requests?page=0&size=15
Authorization: Bearer {token}
```

#### Get Request by ID (Admin Only)
```http
GET /api/adoption-requests/{id}
Authorization: Bearer {token}
```

#### Filter Requests (Admin Only)
```http
GET /api/adoption-requests/filter?status=IN_PROCESS&dogId={uuid}
Authorization: Bearer {token}
```

#### Get Requests by Dog (Admin Only)
```http
GET /api/adoption-requests/dog/{dogId}
Authorization: Bearer {token}
```

#### Update Request Status (Admin Only)
```http
PUT /api/adoption-requests/{id}/status
Authorization: Bearer {token}
Content-Type: application/json

{
  "status": "APPROVED"
}
```

### Statistics (Admin Only)

```http
GET /api/dogs/stats/count-by-status?status=AVAILABLE
Authorization: Bearer {token}

GET /api/adoption-requests/stats/count-by-status?status=IN_PROCESS
Authorization: Bearer {token}
```

## 🧪 Testing

### Run all tests
```bash
mvn test
```

### Run specific test class
```bash
mvn test -Dtest=DogServiceTest
```

### Generate test coverage report
```bash
mvn clean test jacoco:report
```

## 💾 Database

### Development (H2)

The application uses H2 in-memory database by default. Data is reset on each restart.

**Configuration:**
```properties
spring.datasource.url=jdbc:h2:mem:theshireofpaws
spring.datasource.username=sa
spring.datasource.password=
```

### Production (PostgreSQL)

1. Create a PostgreSQL database:
```sql
CREATE DATABASE theshireofpaws;
```

2. Update `application-postgres.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/theshireofpaws
spring.datasource.username=your_username
spring.datasource.password=your_password
```

3. Run with PostgreSQL profile:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=postgres
```

### Entity Relationships

```
AdminUser (1) ----manages----> (n) Dogs
Dogs (1) ----receives----> (n) AdoptionRequests
```

### Database Schema

**admin_user**
- id (UUID, PK)
- email (VARCHAR, UNIQUE)
- password (VARCHAR)
- created_at (TIMESTAMP)
- updated_at (TIMESTAMP)

**dogs**
- id (UUID, PK)
- name (VARCHAR)
- story (TEXT)
- gender (VARCHAR)
- age (INTEGER)
- size (VARCHAR)
- photo_url (VARCHAR)
- status (VARCHAR) - AVAILABLE, IN_PROCESS, ADOPTED
- created_at (TIMESTAMP)
- updated_at (TIMESTAMP)

**adoption_requests**
- id (UUID, PK)
- requester_name (VARCHAR)
- requester_email (VARCHAR)
- housing_type (VARCHAR)
- household_size (INTEGER)
- motivation (TEXT)
- daytime_location (TEXT)
- status (VARCHAR) - IN_PROCESS, APPROVED, DENIED
- dog_id (UUID, FK)
- created_at (TIMESTAMP)
- updated_at (TIMESTAMP)

## 🔒 Security

### JWT Token

- Tokens expire after 24 hours
- Include in Authorization header: `Bearer {token}`
- Token contains admin email as subject

### Roles

- **ADMIN**: Full access to all endpoints
- **PUBLIC**: Can view dogs and submit adoption requests

### Endpoints Access

| Endpoint | Method | Access |
|----------|--------|--------|
| `/api/auth/**` | ANY | Public |
| `/api/dogs` | GET | Public |
| `/api/dogs` | POST/PUT/DELETE | Admin |
| `/api/adoption-requests` | POST | Public |
| `/api/adoption-requests` | GET/PUT | Admin |

## 📝 Validation Rules

### Dog
- **name**: 2-100 characters, required
- **gender**: Male/Female/Unknown, required
- **age**: 0-30 years, required
- **size**: Small/Medium/Large/Extra Large, required

### Adoption Request
- **requesterName**: 2-100 characters, required
- **requesterEmail**: Valid email format, required
- **housingType**: House/Apartment/Other, required
- **householdSize**: 1-20, required
- **motivation**: 50-2000 characters, required

## 🐛 Error Handling

All exceptions return a consistent error response:

```json
{
  "timestamp": "2024-11-13T19:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Dog not found with id: 'uuid-here'",
  "path": "/api/dogs/uuid-here",
  "details": []
}
```

## 🔄 Business Logic

### Automatic Status Updates

**When a user submits an adoption request:**
1. Dog status changes from `AVAILABLE` → `IN_PROCESS`
2. Request status is set to `IN_PROCESS`

**When admin approves a request:**
1. Dog status changes to `ADOPTED`
2. Request status changes to `APPROVED`
3. All other pending requests for the same dog are automatically `DENIED`

**When admin denies a request:**
1. Request status changes to `DENIED`
2. If no other pending requests exist, dog status changes back to `AVAILABLE`

## 📦 Building for Production

```bash
# Build JAR file
mvn clean package -DskipTests

# Run JAR
java -jar target/theshireofpaws-0.0.1-SNAPSHOT.jar --spring.profiles.active=postgres
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is for educational purposes.

## 👥 Author

Created as an individual final project for full-stack development course.

---

## 🎯 Project Requirements Checklist

### Functional Requirements
- ✅ Complete CRUD for dogs
- ✅ Complete CRUD for adoption requests
- ✅ Admin authentication and authorization (JWT)

### Non-Functional Requirements - Backend
- ✅ **Stack:** Spring Boot ✓
- ✅ **Paradigm:** Object-Oriented Programming ✓
- ✅ **Architecture:** MVC + 3 layers + REST API ✓
- ✅ **Tests:** JUnit + Mockito ✓

### Database
- ✅ **PostgreSQL** support ✓
- ✅ **H2** for development ✓

### Version Control
- ✅ **Git + GitHub** ready ✓
- ✅ **Gitflow methodology** compatible ✓
- ✅ **Descriptive commits** structure ✓

### Best Practices
- ✅ **Naming conventions** ✓
- ✅ **Single Responsibility** ✓
- ✅ **DRY** (Don't Repeat Yourself) ✓
- ✅ **KISS** (Keep It Simple, Stupid) ✓
- ✅ **SOLID principles** ✓
- ✅ **Endpoints tested** (Postman collection included) ✓

---

**🐾 Welcome to The Shire Of Paws - Where every dog finds their forever home! 🏡**
