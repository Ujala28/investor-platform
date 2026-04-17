# Investor Management System

## 📌 Overview

This is a Spring Boot based backend application to manage investors and their contacts.
It provides REST APIs for CRUD operations, filtering, pagination, caching, and validation.

---

## 🚀 Features

* Create, update, delete, and fetch investors
* Manage investor contacts
* Pagination and filtering (by investor type and KYC status)
* Validation (email uniqueness, PAN format, age ≥ 18)
* Exception handling with global handler
* Logging using SLF4J
* Redis caching for performance optimization
* Unit and integration testing

---

## 🛠️ Tech Stack

* Java 17+
* Spring Boot
* Spring Data JPA
* PostgreSQL
* Redis (Caching)
* Lombok
* Maven
* JUnit & Mockito

---

## 📁 Project Structure

```
com.wealth.investor
├── controller
├── service
├── repository
├── entity
│   └── enums
├── dto
│   ├── request
│   └── response
├── exception
├── config
```

---

## ⚙️ Setup Instructions

### 1️⃣ Clone project

```bash
git clone <https://github.com/Ujala28/investor-platform>
```

---

### 2️⃣ Configure Database

Update `application.properties`:

```
spring.datasource.url=jdbc:postgresql://localhost:5432/your_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

---

### 3️⃣ Run Redis (Docker)

```
docker run -d -p 6379:6379 redis
```

---

### 4️⃣ Run Application

```
mvn spring-boot:run
```

---

## 📡 API Endpoints

### Investor APIs

| Method | Endpoint              | Description                   |
| ------ | --------------------- | ----------------------------- |
| POST   | `/api/investors`      | Create investor               |
| GET    | `/api/investors/{id}` | Get investor                  |
| PUT    | `/api/investors/{id}` | Update investor               |
| DELETE | `/api/investors/{id}` | Delete investor               |
| GET    | `/api/investors`      | Get all (pagination + filter) |

---

### Contact APIs

| Method | Endpoint                     |
| ------ | ---------------------------- |
| POST   | `/api/contacts/{investorId}` |
| GET    | `/api/contacts/{investorId}` |
| DELETE | `/api/contacts/{contactId}`  |

---

## 🧠 Business Rules

* Email must be unique
* Investor must be at least 18 years old
* PAN format validation enforced
* Corporate investors cannot have spouse contacts
* KYC transition: REJECTED → VERIFIED is not allowed

---

## ⚡ Caching (Redis)

* Implemented using Spring Cache
* `@Cacheable` → fetch investor
* `@CachePut` → update investor
* `@CacheEvict` → delete investor

---

## 🧪 Testing

### Run tests:

```
mvn test
```

* Unit tests for service layer
* Integration test using MockMvc

---

## 📊 Logging

* Implemented using SLF4J
* Logs include:

    * Request start
    * Success messages
    * Errors and exceptions

---

## 💡 Improvements (Future Scope)

* Kafka integration for event-driven architecture
* JWT authentication & authorization
* Dockerization of full application
* Swagger/OpenAPI enhancements

---

## 👨‍💻 Author

Ujala Choudhary
