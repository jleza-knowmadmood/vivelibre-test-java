# vivelibre-test-java

Technical test with two independent Maven projects.

## Projects

### exercise-1-books

Plain Java 21 Maven project.

Run:

```bash
cd exercise-1-books
mvn test
```

### exercise-2-token-service

Spring Boot 3.5.13 service running on Java 21.

Implemented endpoint:

```text
GET http://localhost:8081/token
```

Expected response:

```json
{
  "token": "TOKEN_RECIBIDO",
  "timestamp": "2025-02-27T14:00:00Z"
}
```

Technical notes:

- The service calls an external authentication service and returns the received token plus the current timestamp in ISO 8601 format.
- The timestamp is truncated to seconds.
- The external service base URL is configurable through `EXTERNAL_AUTH_BASE_URL`.
- If the external service fails or returns an empty token, the API responds with `502 Bad Gateway`.

Run locally:

```bash
docker run -p 8080:8080 skeet15/auth-vivelibre:latest
```

```bash
cd exercise-2-token-service
mvn spring-boot:run
```

Test endpoint:

```bash
curl --location --request GET 'http://localhost:8081/token'
```

Build image:

```bash
cd exercise-2-token-service
mvn clean package
docker build -t exercise-2-token-service:latest .
```

Run with Docker Compose:

```bash
docker compose up --build
```

The external authentication service used by the compose setup is provided through the Docker image `skeet15/auth-vivelibre:latest`.

Tests:

```bash
cd exercise-2-token-service
mvn test
```
