# PatiPets Backend

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-blue)
![Docker](https://img.shields.io/badge/Docker-ready-2496ED)
![License](https://img.shields.io/badge/license-Noncommercial-lightgrey)

Backend para PatiPets, una plataforma web comunitaria que conecta ciudadanos con refugios de animales para adoptar, ser voluntario o apadrinar. Construido con **arquitectura hexagonal (Ports & Adapters)**, autenticación **JWT propia** (sin Auth0/Firebase/servicios cloud de auth), migraciones con **Flyway** y **Supabase** local (Postgres + Storage) como backend de datos e imágenes.

Repo hermano: [`patipets-frontend`](https://github.com/Luis-Perez24/patipets-frontend) (Vue 3 + Pinia + Tailwind).

Documentación de arquitectura detallada (C4, decisiones de diseño, tests, limitaciones): [`docs/ARQUITECTURA.md`](docs/ARQUITECTURA.md).

---

## Requisitos

- JDK 21
- Apache Maven
- Docker
- Node.js + npm
- Supabase CLI (`npx supabase`)

---

## Primeros Pasos

### 1. Clonar el repositorio

```bash
git clone git@github.com:Luis-Perez24/patipets-backend.git
cd patipets-backend
```

### 2. Iniciar Supabase local

```bash
npx supabase init
npx supabase start
```

Al finalizar aparecerá un mensaje con la **service_role key**.
Cópiala porque la necesitarás en el siguiente paso.

### 3. Configurar variables de entorno

```bash
cp .env.example .env
```

Edita el archivo `.env` con los valores correspondientes:

```env
SUPABASE_DB_PASSWORD=postgres
SUPABASE_SERVICE_KEY=<la_service_role_key_del_paso_anterior>
JWT_SECRET=<genera_una_clave_con_el_siguiente_comando>
```

Para generar el `JWT_SECRET` ejecuta:

```bash
openssl rand -base64 64 | tr -d '\n'
```

### 4. Compilar y ejecutar

```bash
mvn clean package -DskipTests
java -jar target/patipets-backend-1.0.0.jar
```

La aplicación arrancará en `http://localhost:8080`.
Flyway creará automáticamente las tablas y poblará los datos
de prueba (usuarios, refugios, animales) al iniciar.

---

## Usuarios de Prueba

Los siguientes usuarios vienen precargados (`V4__seed_data.sql`):

| Email | Password | Rol |
|---|---|---|
| admin@patipets.cl | admin123 | ADMIN |
| ciudadano@patipets.cl | ciudadano123 | CIUDADANO |
| refugio@patipets.cl | refugio123 | REFUGIO |

---

## Endpoints Principales

### Autenticación
| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/v1/auth/register` | Registrar nuevo usuario |
| POST | `/api/v1/auth/login` | Iniciar sesión (devuelve JWT) |

### Públicos (no requieren token)
| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/v1/public/animales` | Catálogo de animales disponibles |
| GET | `/api/v1/public/dashboard/estadisticas` | Estadísticas generales |
| GET | `/api/v1/public/refugios/ubicaciones` | Mapa de refugios |

### Solicitudes de Adopción (requieren JWT)
| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/v1/adopcion/solicitar` | Enviar solicitud (formulario 3 pasos) |
| GET | `/api/v1/adopcion/mis-solicitudes` | Mis solicitudes |
| GET | `/api/v1/adopcion/refugio/{id}/solicitudes` | Solicitudes del refugio |
| PUT | `/api/v1/adopcion/solicitudes/{id}/aprobar` | Aprobar solicitud |
| PUT | `/api/v1/adopcion/solicitudes/{id}/rechazar` | Rechazar solicitud |

### Refugios (requieren JWT)
| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/v1/admin/refugios` | Registrar refugio |
| GET | `/api/v1/admin/refugios/pendientes` | Refugios pendientes |
| PUT | `/api/v1/admin/refugios/{id}/aprobar` | Aprobar refugio |
| PUT | `/api/v1/admin/refugios/{id}/rechazar` | Rechazar refugio |

### Animales por Refugio (requieren JWT)
| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/v1/refugios/{id}/animales` | Agregar animal |
| GET | `/api/v1/refugios/{id}/animales` | Listar animales del refugio |
| PUT | `/api/v1/refugios/animales/{id}` | Actualizar animal |
| DELETE | `/api/v1/refugios/animales/{id}` | Eliminar animal |

### Alertas (requieren JWT)
| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/v1/alertas` | Crear alerta |
| GET | `/api/v1/alertas/refugio/{id}` | Alertas de un refugio |
| GET | `/api/v1/alertas/activas` | Alertas activas |
| PUT | `/api/v1/alertas/{id}/resolver` | Resolver alerta |
| DELETE | `/api/v1/alertas/{id}` | Eliminar alerta |

### Admin (requiere JWT con rol ADMIN)
| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/v1/admin/dashboard/estadisticas` | Dashboard con métricas |
| GET | `/api/v1/admin/usuarios` | Listar usuarios (con filtros) |
| GET | `/api/v1/admin/usuarios/{id}` | Ver detalle de usuario |
| PUT | `/api/v1/admin/usuarios/{id}` | Actualizar usuario |
| PUT | `/api/v1/admin/usuarios/{id}/bloquear` | Bloquear usuario |
| PUT | `/api/v1/admin/usuarios/{id}/desbloquear` | Desbloquear usuario |

---

## Ejemplos con curl

### 1. Login como administrador

```bash
curl -s -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@patipets.cl","password":"admin123"}' | python3 -m json.tool
```

### 2. Guardar el token en una variable

```bash
TOKEN=$(curl -s -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@patipets.cl","password":"admin123"}' \
  | python3 -c "import sys,json; print(json.load(sys.stdin)['datos']['token'])")
```

### 3. Consultar dashboard admin

```bash
curl -s http://localhost:8080/api/v1/admin/dashboard/estadisticas \
  -H "Authorization: Bearer $TOKEN" | python3 -m json.tool
```

---

## Detener la Aplicación

- Spring Boot: `Ctrl + C` en la terminal donde corre
- Supabase: `npx supabase stop`

---

## Stack Tecnológico

| Tecnología | Versión |
|---|---|
| Java | 21 |
| Spring Boot | 3.2.5 |
| Spring Security + JWT | jjwt 0.12.5 |
| Spring Data JPA + Hibernate | 6 |
| Flyway | 9 |
| PostgreSQL | 17 (via Supabase) |
| Supabase Storage | — |
| Maven | — |
| Docker | — |

---

## Arquitectura

Ver [`docs/ARQUITECTURA.md`](docs/ARQUITECTURA.md) para el modelo C4, justificación del estilo hexagonal, cobertura de tests y limitaciones conocidas.

## Licencia

Distribuido bajo [PolyForm Noncommercial License 1.0.0](LICENSE) — de uso libre para fines personales, educativos o de evaluación; **no autorizado para uso comercial** sin permiso del autor.
