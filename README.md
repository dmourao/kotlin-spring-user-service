# Kotlin Spring User Service

A backend demo project implementing a user-management REST API with Spring Boot, Kotlin and PostgreSQL.

It demonstrates API design, relational persistence and integration testing with JUnit and Testcontainers, which provisions a real PostgreSQL instance for the test suite.

Developed as a personal project to explore current backend development practices and stay up to date with the Kotlin and Spring Boot ecosystem.

## Technologies

- Kotlin 2.3.21
- Spring Boot 4.1.1 and Spring Web
- Spring Data JPA / Hibernate
- PostgreSQL 18.0 (local Docker Compose; integration tests use PostgreSQL 18)
- Flyway for database migrations
- Maven
- JUnit 5, Spring RestTestClient, and Testcontainers

## Requirements

- JDK 25
- Docker with Docker Compose — required to start the local database and run the integration tests

The project includes the Maven Wrapper, so a separate Maven installation is not required.

## Run locally

1. Start PostgreSQL and, optionally, pgAdmin:

   ```bash
   docker compose up -d
   ```

   `PostgreSQL` is available at `localhost:5432`, with the `userservice` database and development credentials defined in `docker-compose.yaml`. 
   
   `pgAdmin4` is available at <http://localhost:8888>.

2. Start the application:

   ```bash
   # macOS / Linux
   ./mvnw spring-boot:run

   # Windows
   .\mvnw.cmd spring-boot:run
   ```

   The API is available at `http://localhost:8080`. 
   
   On startup, Flyway applies the SQL migrations included in the project.

## API

All endpoints use the `/api/v1/users` prefix.

| Method | Endpoint | Description | Successful response |
|---|---|---|---|
| `GET` | `/api/v1/users` | Lists all users | `200 OK` |
| `GET` | `/api/v1/users/{id}` | Retrieves a user by ID | `200 OK` |
| `POST` | `/api/v1/users` | Creates a user | `201 Created`, with the user and a `Location` header |
| `PUT` | `/api/v1/users/{id}` | Updates a user's name and age | `200 OK` |
| `DELETE` | `/api/v1/users/{id}` | Deletes a user | `204 No Content` |

### Testing API using httpie

The project includes a HTTPie colletion with some sample requests: [httpie-collection-kotlin-spring-user-service](./httpie-collection-kotlin-spring-user-service.json)

## Tests

Run the test suite:

```bash
# macOS / Linux
./mvnw test

# Windows
.\mvnw.cmd test
```

The integration tests start a PostgreSQL container with Testcontainers and configure the application to use it. 

Docker must be running.