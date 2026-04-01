# vivelibre-test-java

Prueba técnica con dos proyectos Maven independientes.

## Proyectos

### exercise-1-books

Proyecto Maven con Java 21 sin Spring.

Incluye:

- Carga del JSON `books.json`
- Métodos de filtrado y procesamiento para los requisitos solicitados
- Tests unitarios que cubren los puntos principales

Notas técnicas:

- La solución está implementada como un proyecto Maven simple, sin Spring.
- Cada requisito está representado por un método dedicado dentro de la capa de servicio.
- También se incluyen comprobaciones opcionales para autores duplicados, libros sin fecha de publicación y libros más recientes.

Ejecutar:

```bash
cd exercise-1-books
mvn test
```

### exercise-2-token-service

Microservicio con Spring Boot 3.5.13 sobre Java 21.

Endpoint implementado:

```text
GET http://localhost:8081/token
```

Respuesta esperada:

```json
{
  "token": "TOKEN_RECIBIDO",
  "timestamp": "2025-02-27T14:00:00Z"
}
```

Notas técnicas:

- El servicio llama a un servicio externo de autenticación y devuelve el token recibido junto con la fecha y hora actual en formato ISO 8601.
- El `timestamp` se devuelve truncado a segundos.
- La URL base del servicio externo se configura mediante `EXTERNAL_AUTH_BASE_URL`.
- Si el servicio externo falla o devuelve un token vacío, la API responde con `502 Bad Gateway`.

Ejecución local:

```bash
docker run -p 8080:8080 skeet15/auth-vivelibre:latest
```

```bash
cd exercise-2-token-service
mvn spring-boot:run
```

Probar endpoint:

```bash
curl --location --request GET 'http://localhost:8081/token'
```

Construir imagen:

```bash
cd exercise-2-token-service
mvn clean package
docker build -t exercise-2-token-service:latest .
```

Ejecutar con Docker Compose:

```bash
docker compose up --build
```

El servicio externo de autenticación usado en `docker-compose` se levanta con la imagen `skeet15/auth-vivelibre:latest`.

Tests:

```bash
cd exercise-2-token-service
mvn test
```
