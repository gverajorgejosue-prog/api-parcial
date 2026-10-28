# api-parcial

API REST de denuncias ciudadanas (Spring Boot 4 + Java 21 + PostgreSQL/MySQL).

## Endpoints

| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/denuncias` | Registra una denuncia |
| GET | `/denuncias?offset=0&limit=5` | Listado paginado |
| GET | `/denuncias/{id}` | Denuncia por ID |
| GET | `/denuncias/ciudadano/{dni}` | Denuncias por DNI |
| PUT | `/denuncias/{id}` | Marca la denuncia como atendida |

## Despliegue

Incluye `Dockerfile` multi-stage para Render.com. La aplicación lee la configuración de la base de datos desde variables de entorno:

| Variable | Descripción | Ejemplo |
|----------|-------------|---------|
| `DB_URL` | URL JDBC de PostgreSQL | `jdbc:postgresql://host:5432/denunciadb?sslmode=require` |
| `DB_USER` | Usuario de la BD | `postgres` |
| `DB_PASSWORD` | Contraseña de la BD | `****` |
| `PORT` | Puerto HTTP (lo inyecta Render) | `8080` |

Si alguna variable no está definida, se usan valores por defecto de `localhost:5432/denunciadb`.

### Variables por defecto (local)

```properties
spring.datasource.url=${DB_URL:jdbc:postgresql://localhost:5432/denunciadb}
spring.datasource.username=${DB_USER:postgres}
spring.datasource.password=${DB_PASSWORD:postgres}
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### Nota de seguridad

No incluir credenciales reales en el repositorio. En Render, configurar las variables en **Environment**; en local, usar variables de entorno del sistema.
