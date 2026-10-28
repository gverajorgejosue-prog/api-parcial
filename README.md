# api-parcial

API REST de denuncias ciudadanas (Spring Boot 4 + Java 21 + MySQL/PostgreSQL).

## Endpoints

| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/denuncias` | Registra una denuncia (`estado = 1`) |
| GET | `/denuncias?offset=0&limit=5` | Listado paginado |
| GET | `/denuncias/{id}` | Denuncia por ID |
| GET | `/denuncias/ciudadano/{dni}` | Denuncias por DNI |
| PUT | `/denuncias/{id}` | Marca la denuncia como atendida (`estado = 0`) |

### Paginación

`offset` es el **desplazamiento en registros** (0, 5, 10...), no el número de página.
Internamente se calcula `página = offset / limit`, por lo que debe ser múltiplo de `limit`.

| Petición | Resultado |
|----------|-----------|
| `GET /denuncias?offset=0&limit=5` | Página 0: registros 1-5 |
| `GET /denuncias?offset=5&limit=5` | Página 1: registros 6-10 |

Con la base vacía, o con un offset fuera de rango, responde **200 OK** con `"content": []`
(nunca 500 ni NullPointerException). También acepta la barra final (`/denuncias/`).

## Validación con Postman / Newman

| Archivo | Contenido |
|---------|-----------|
| `postman/Denuncias-API.postman_collection.json` | 14 peticiones con 58 validaciones |
| `postman/Denuncias-Render.postman_environment.json` | Entorno (`baseUrl` de Render, DNI de prueba) |

**En Postman:** *Import* → selecciona ambos archivos → elige el entorno **Denuncias - Render** →
*Run* (Collection Runner).

**Por terminal:**

```bash
npx newman run postman/Denuncias-API.postman_collection.json \
  -e postman/Denuncias-Render.postman_environment.json \
  --timeout-request 120000
```

Qué valida: listado con y sin barra final, segunda página, página fuera de rango,
parámetro inválido (400), registro (201 + `Location`), detalle por ID, atender (PUT),
persistencia del estado, búsqueda por DNI, búsqueda sin resultados, 404 y 400 por ID no numérico.

> La colección **crea una denuncia nueva en cada ejecución** (la API no tiene endpoint DELETE).

## Despliegue en Render

Incluye `Dockerfile` multi-stage para Render.com. La configuración se lee de variables de entorno:

| Variable | Descripción | Ejemplo |
|----------|-------------|---------|
| `DB_URL` | URL JDBC (PostgreSQL en Render) | `jdbc:postgresql://host:5432/denunciadb?sslmode=require` |
| `DB_USER` | Usuario de la BD | `admin` |
| `DB_PASSWORD` | Contraseña de la BD | `****` |
| `PORT` | Puerto HTTP (lo inyecta Render) | `8080` |

No se fija `spring.datasource.driver-class-name` ni el dialecto: Spring los detecta
desde la URL, así el mismo artefacto sirve para MySQL local y PostgreSQL en Render.

## Ejecución local (MySQL)

Sin variables de entorno usa MySQL en `localhost:3306/denunciadb` (usuario `root`, sin contraseña):

```properties
spring.datasource.url=${DB_URL:jdbc:mysql://localhost:3306/denunciadb?useSSL=false&allowPublicKeyRetrieval=true&connectionTimeZone=LOCAL&preserveInstants=false}
spring.datasource.username=${DB_USER:root}
spring.datasource.password=${DB_PASSWORD:}

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

```bash
# Crear la base (si no existe)
mysql -u root -e "CREATE DATABASE IF NOT EXISTS denunciadb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

# Arrancar
./mvnw spring-boot:run
```

## Nota de seguridad

No incluir credenciales reales en el repositorio. En Render se configuran en **Environment**;
en local, con variables de entorno del sistema (`DB_URL`, `DB_USER`, `DB_PASSWORD`).
