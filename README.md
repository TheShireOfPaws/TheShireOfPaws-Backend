# 🐾 The Shire of Paws 🐾

This project is a full-stack web application designed to **streamline the management and adoption process** for an animal shelter. It aims to bridge the gap between animals in need and potential adopters by providing a modern, accessible platform.

## 🌟 Objective

To develop a comprehensive digital tool for an animal shelter, enabling efficient management of animal records, handling adoption requests, and fostering communication with the community.

---

## 💻 Backend: API Service (Spring Boot)

The Backend serves as a robust **RESTful API** built to handle all data management, business logic, and security.

### 🚀 Getting Started

Clone the repository and navigate to the backend directory.

```bash
# Clone the repository
git clone (https://github.com/TheShireOfPaws/TheShireOfPaws-Backend)
cd the-shire-of-paws/backend
```
### 🛠️ Tech Stack

* **Framework:** Spring Boot
* **Language:** Java
* **Paradigm:** Object-Oriented Programming (OOP)
* **Architecture:** MVC + 3-Layer Architecture (Controller, Service, Repository) + Client-Server (API Rest)
* **Database:** PostgreSQL
* **Security:** Spring Security + JWT (JSON Web Tokens) for Authentication and Authorization
* **Testing:** JUnit 5 and Mockito

---

### 🗄️ Database Configuration (PostgreSQL)

Ensure you have a PostgreSQL database running. Update your `src/main/resources/application.properties` or `application.yml` with your database credentials.

```properties
# application.properties
spring.datasource.url=jdbc:postgresql://localhost:5432/the_shire_of_paws_db
spring.datasource.username=your_username
spring.datasource.password=your_password

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```
### 🔑 Security & Authorization

The application uses **Spring Security** with **JWT (JSON Web Tokens)** for securing endpoints and implementing a role-based access control system.

#### Key Aspects:

* **Authentication:** Users (both `ADOPTER` and `ADMIN`) must successfully log in by providing credentials to an authentication endpoint. Upon success, the API returns a **JWT**.
* **Authorization:** The received JWT must be included in subsequent requests to access protected resources. Spring Security is configured to:
    * Protect sensitive endpoints (`/api/admin/**`) by requiring the `ADMIN` role.
    * Allow public endpoints (`/api/dogs`, `/api/auth/**`, `/api/adoptions/request`) to be accessed by unauthenticated users (ANONYMOUS/ALL).
* **Roles:** Two primary roles are used:
    * `ADMIN`: Full access; can perform CRUD on dogs, manage adoption requests, and view the dashboard.
    * `ADOPTER`: Future role for authenticated adopters (can be expanded to view their request status).

 ### ✨ Core Features & API Endpoints

The API follows a **RESTful** design and enables the required functionalities:

| Feature | HTTP Method | Endpoint | Description | Roles |
| :--- | :--- | :--- | :--- | :--- |
| **Auth/Login** | `POST` | `/api/auth/login` | Authenticate user and return JWT. | ANONYMOUS |
| **Auth/Register** | `POST` | `/api/auth/register` | Register a new user (Adopter role). | ANONYMOUS |
| **Dogs (Public)** | `GET` | `/api/dogs` | Retrieve a list of all dogs available for adoption. | ALL |
| **Dogs (CRUD)** | `POST` | `/api/admin/dogs` | Create a new dog profile. | ADMIN |
| **Dogs (CRUD)** | `PUT` | `/api/admin/dogs/{id}` | Update an existing dog profile. | ADMIN |
| **Dogs (CRUD)** | `DELETE` | `/api/admin/dogs/{id}` | Delete a dog profile. | ADMIN |
| **Adoption Request** | `POST` | `/api/adoptions/request` | Non-registered user submits a pre-adoption request. Updates dog status. | ANONYMOUS |
| **Adoption Management** | `GET` | `/api/admin/adoptions/pending` | View all pending pre-adoption requests. | ADMIN |
| **Adoption Approval**| `PUT` | `/api/admin/adoptions/{requestId}/approve`| Approve request, register adopter (if needed), and link Dog. | ADMIN |
| **Admin Dashboard** | `GET` | `/api/admin/dashboard` | Access to admin-specific summary data. | ADMIN |

### 🏃 Running the Application

After configuring the database in `application.properties`, you can use **Maven** to build and run the Spring Boot application.

```bash
# 1. Clean, compile, and package the application
mvn clean install

# 2. Run the Spring Boot application
mvn spring-boot:run
