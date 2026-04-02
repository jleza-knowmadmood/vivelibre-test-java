# vivelibre-test-java

Prueba técnica con dos proyectos Maven independientes.

## Proyectos

### exercise-1-books

Proyecto Maven con Java 21 sin Spring.

Incluye:

- Carga del JSON `books.json`
- Métodos de filtrado y procesamiento para los requisitos solicitados
- Tests unitarios que cubren los puntos principales
- Exportación opcional a JSON y CSV

Notas técnicas:

- La solución está implementada como un proyecto Maven simple, sin Spring.
- Cada requisito está representado por un método dedicado dentro de la capa de servicio.
- También se incluyen comprobaciones opcionales para autores duplicados, libros sin fecha de publicación y libros más recientes.
- El punto 9 opcional genera ficheros de salida JSON y CSV.

Ejecutar:

```bash
cd exercise-1-books
mvn test
```

Prueba manual:

```bash
cd exercise-1-books
mvn compile exec:java -Dexec.mainClass=com.vivelibre.books.BooksApplication
```

La salida por consola incluye ejemplos de filtrado, ordenación, conteo por autor, fechas formateadas y estadísticas de páginas.
En PowerShell entrecomillar el parámetro "-Dexec.mainClass=com.vivelibre.books.BooksApplication"
Los ficheros opcionales del punto 9 se generan en `exercise-1-books/target/generated/`.

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
- Esta implementación añade como extra opcional autenticación básica HTTP sobre el endpoint `GET /token`.
- La URL base del servicio externo se configura mediante `EXTERNAL_AUTH_BASE_URL`.
- El token externo se cachea en memoria con Spring Cache y Caffeine.
- La expiración del token se controla mediante TTL configurable con `token-cache.ttl-seconds`.
- Se incluye logging básico del flujo de petición y una métrica simple en memoria con el número de peticiones procesadas, visible actualmente en logs.
- Si el servicio externo falla o devuelve un token vacío, la API responde con `502 Bad Gateway`.
- La documentación OpenAPI está disponible mediante Swagger UI.

Ejecución recomendada con Docker Compose:

```bash
docker compose up --build
```

El fichero `docker-compose.yml` orquesta el servicio externo de autenticación y este microservicio.

Probar endpoint protegido:

```bash
curl --location --request GET 'http://localhost:8081/token' \
--user token-user:token-password
```

Swagger UI:

```text
http://localhost:8081/swagger-ui/index.html
```

Credenciales de prueba:

```text
user: token-user
password: token-password
```

Se incluyen en el proyecto para facilitar la validación manual del endpoint protegido y su prueba desde Swagger UI.

Ejecución alternativa del microservicio en local:

```bash
cd exercise-2-token-service
mvn spring-boot:run
```

En este caso, el servicio externo de autenticación debe estar disponible en `http://localhost:8080`.

Construir imagen:

```bash
cd exercise-2-token-service
docker build -t exercise-2-token-service:latest .
```

Tests:

```bash
cd exercise-2-token-service
mvn test
```
