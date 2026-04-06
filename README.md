# vivelibre-test-java

Prueba técnica con dos proyectos Maven independientes.

Repositorio GitHub:

```text
https://github.com/jleza-knowmadmood/vivelibre-test-java
```

Imagen Docker Hub:

```text
https://hub.docker.com/r/jlkmm/exercise-2-token-service
```

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
- La configuración se separa por perfiles Spring para los entornos `local` y `docker`.
- El perfil `local` usa `http://localhost:8080` y el perfil `docker` usa `http://auth-service:8080`.
- El token externo se cachea en memoria con Spring Cache y Caffeine.
- La expiración del token se controla mediante TTL configurable con `token-cache.ttl-seconds`.
- Se incluye logging básico del flujo de petición y una métrica simple en memoria con el número de peticiones procesadas, expuesta además en `GET /metrics`.
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

Probar endpoint de métricas:

```bash
curl --location --request GET 'http://localhost:8081/metrics' \
--user token-user:token-password
```

Respuesta esperada:

```json
{
  "requestsProcessed": 5
}
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
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

En este caso se activa el perfil `local` y el servicio externo de autenticación debe estar disponible en `http://localhost:8080`.
En PowerShell entrecomillar el parámetro "-Dspring-boot.run.profiles=local"

Descargar y ejecutar imagen publicada en Docker Hub:

```bash
docker pull jlkmm/exercise-2-token-service:latest
docker run -p 8081:8081 --add-host=host.docker.internal:host-gateway -e SPRING_PROFILES_ACTIVE=local -e EXTERNAL_AUTH_BASE_URL=http://host.docker.internal:8080 jlkmm/exercise-2-token-service:latest
```

En este caso, el servicio externo de autenticación debe estar ejecutándose en tu máquina host en el puerto `8080`. El contenedor accede a él mediante `host.docker.internal`.

Tests:

```bash
cd exercise-2-token-service
mvn test
```

## Decisiones técnicas

- Se ha optado por dos proyectos Maven independientes en lugar de un proyecto multimódulo para mantener cada ejercicio aislado y facilitar la revisión de la prueba.
- `exercise-1-books` se ha resuelto sin Spring, priorizando una implementación simple y centrada en procesamiento de datos, con un método independiente por cada punto solicitado.
- `exercise-2-token-service` se ha desarrollado con Spring Boot y OpenAPI para exponer el endpoint de forma clara y documentada.
- La autenticación básica HTTP se ha añadido como mejora opcional del ejercicio 2 para reforzar el acceso al endpoint sin alterar el flujo principal pedido.
- La persistencia temporal y expiración del token se han resuelto con Spring Cache y Caffeine, evitando infraestructura adicional y manteniendo una solución ligera en memoria.
- La configuración de `exercise-2-token-service` se ha separado en perfiles `local` y `docker` para cubrir explícitamente distintos entornos de ejecución.
- El Dockerfile del ejercicio 2 se ha definido como multistage para que la construcción de la imagen no dependa de artefactos locales previos.
