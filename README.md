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

Run locally:

```bash
cd exercise-2-token-service
mvn spring-boot:run
```

Build image:

```bash
cd exercise-2-token-service
mvn clean package
docker build -t exercise-2-token-service:latest .
```

Optional Docker Hub push:

```bash
docker tag exercise-2-token-service:latest <dockerhub-user>/exercise-2-token-service:latest
docker push <dockerhub-user>/exercise-2-token-service:latest
```

Run with Docker Compose:

```bash
docker compose up --build
```
