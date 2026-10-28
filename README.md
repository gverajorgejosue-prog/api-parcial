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

Incluye `Dockerfile` multi-stage para Render.com. Variables de entorno:

- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
