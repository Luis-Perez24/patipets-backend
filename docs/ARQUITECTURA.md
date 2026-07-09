# PatiPets — Documentación de Arquitectura

Plataforma web comunitaria que digitaliza la difusión y gestión de refugios de animales, conectando ciudadanos con refugios para adoptar, ser voluntario o apadrinar animales.

Repos: `patipets-backend` (este repo, Java/Spring Boot) y `patipets-frontend` (Vue 3), independientes con Git Flow propio.

---

## 1. Funcionalidades implementadas

| Módulo | Estado |
|---|---|
| Auth & Usuarios | ✅ Implementado — `AuthService`, JWT propio, activación de roles |
| Gestión de Refugios | ✅ Implementado — `GestionRefugioService`, flujo PENDIENTE→APROBADO/RECHAZADO |
| Catálogo de Animales | ✅ Implementado — `GestionAnimalService`, formulario de adopción multi-paso |
| Voluntarios & Padrinos | ✅ Implementado — `GestionApadrinamientoService` (padrinos) + convocatorias/inscripciones como extensión del módulo de Alertas (`GestionAlertaService`, `RespuestaAlerta`) |
| Notificaciones | ✅ Implementado — `GestionNotificacionService` orquestado por eventos de dominio |
| Mapa & Dashboard | ✅ Implementado — mapa SVG propio con geocoding local, dashboards público/admin, historial de refugio (`RefugioHistorial`) |
| Administración | ✅ Implementado — `GestionUsuarioService` (bloquear/editar/listar) |

---

## 2. Modelo C4 — Nivel 2 (Contenedores)

```mermaid
graph TB
    usuario["Persona<br/>Ciudadano / Refugio / Admin<br/>(navegador)"]

    subgraph patipets["Sistema PatiPets"]
        nginx["Contenedor: Nginx<br/>nginx:1.25-alpine<br/>Sirve el SPA + reverse proxy"]
        spa["Contenedor: SPA Vue 3<br/>Vue 3 + Pinia + Tailwind"]
        api["Contenedor: API Backend<br/>Java 21 + Spring Boot 3.2.5<br/>Arquitectura hexagonal, JWT propio"]
        db[("Contenedor: PostgreSQL<br/>Supabase local (CLI)<br/>Flyway, 27 migraciones")]
        storage[("Contenedor: Supabase Storage<br/>Supabase local (CLI)<br/>Imágenes")]
        mail["Contenedor: Mailpit/SMTP<br/>axllent/mailpit (dev)"]
    end

    usuario -->|"HTTPS/HTTP :80/:443"| nginx
    nginx -->|sirve estáticos| spa
    nginx -->|"proxy_pass /api/v1/*, /actuator/health"| api
    api -->|"JDBC :54322"| db
    api -->|"REST :54321"| storage
    api -->|"SMTP :1025"| mail
```

Este conjunto de contenedores se orquesta con Docker Compose y puede desplegarse en cualquier VM/VPS con Docker (backend en `develop`, producción en `main`).

---

## 3. Estilo arquitectónico

**Hexagonal (Ports & Adapters / Clean Architecture)**, con tres capas: `domain` (modelos y enums de negocio, sin dependencias externas) → `application` (puertos de salida + casos de uso + servicios) → `infrastructure` (adaptadores JPA, seguridad, controladores web, storage). Regla de dependencia: infrastructure depende de core, nunca al revés.

**Justificación:** el proyecto se diseñó bajo la restricción de *Zero External Services* (nada de Auth0/Firebase/Supabase Auth/servicios cloud, todo dockerizado localmente). Este estilo permite:
- Testear el núcleo de negocio sin levantar Spring, Postgres ni Docker — los tests unitarios mockean únicamente los *ports*.
- Implementar JWT propio y Supabase local como *adapters* intercambiables sin tocar una sola regla de negocio.
- Aislar decisiones de infraestructura (Docker, entorno de despliegue, Supabase CLI) del core, que es idéntico en todos los entornos.

---

## 4. Aspectos técnicos

| Capa | Tecnología |
|---|---|
| Backend | Java 21 + Spring Boot 3.2.5 + Spring Security 6 |
| Autenticación | JWT propio (jjwt 0.12.x, HS512) + BCrypt |
| Persistencia | Spring Data JPA + Hibernate 6 + Flyway (27 migraciones, `ddl-auto: validate`) |
| Base de datos | PostgreSQL 17 vía Supabase CLI local |
| Storage | Supabase Storage local + Thumbnailator (compresión 800×800 JPG) |
| Frontend | Vue 3 + Pinia + Vue Router + Tailwind CSS |
| Infraestructura | Docker + Docker Compose + Nginx (reverse proxy) |
| CI | GitHub Actions — build y tests automáticos en cada push |

**Estructura del sistema:** 8 controladores REST (`Auth`, `Admin`, `Adopcion`, `Alerta`, `Apadrinamiento`, `Notificacion`, `Público`, `Refugio` — este último incluye el CRUD de animales, anidado por diseño bajo cada refugio), 12 servicios de aplicación implementando 12 casos de uso, 14 puertos de salida.

**Integración entre componentes:** el SPA se comunica con la API vía `/api/v1/*` proxied por Nginx dentro del mismo contenedor de red de Docker Compose (resolución DNS interna por nombre de servicio `backend`).

**Seguridad:** `SecurityConfig` con sesión *stateless*, filtro `JwtAuthenticationFilter` que valida el token en cada request, `@PreAuthorize`/`@EnableMethodSecurity` para control de roles a nivel de método, CORS configurado, endpoints públicos explícitos (`/api/v1/public/**`, login, registro, catálogo de animales por refugio, alertas activas, `/actuator/**`).

**Variables de entorno** (`.env`, cargadas con `spring-dotenv`): `SUPABASE_DB_PASSWORD`, `SUPABASE_SERVICE_KEY`, `SUPABASE_PUBLIC_URL`, `JWT_SECRET`, más `SUPABASE_STORAGE_BUCKET`/`MAIL_HOST`/`MAIL_PORT` con defaults en `docker-compose.yml`.

**Contenedores:** `docker-compose.yml` define `backend` (Dockerfile multi-stage `maven:3.9.6` → `eclipse-temurin:21-jre`), `frontend` (multi-stage `node:20-alpine` → `nginx:1.25-alpine`) y `mailpit`. Supabase (Postgres + Storage) corre aparte, vía Supabase CLI directo en el host (`npx supabase start`), no dentro de Docker Compose.

---

## 5. Pruebas de software

**59 tests unitarios** (JUnit 5 + Mockito + AssertJ, ya incluidos en `spring-boot-starter-test`) sobre los 6 servicios de aplicación más críticos del núcleo de negocio:

| Servicio | Tests | Cubre |
|---|---|---|
| `AuthService` | 13 | Registro, login, activación de rol, recuperación/reseteo de password |
| `GestionAnimalService` | 11 | CRUD de animales, flujo completo de solicitud de adopción (solicitar/aprobar/confirmar/rechazar), regla de rechazo automático de solicitudes competidoras |
| `GestionRefugioService` | 9 | Alta, geocoding, aprobación/rechazo, promoción de usuario a rol REFUGIO |
| `GestionApadrinamientoService` | 9 | Apadrinar, cancelar por padrino/refugio, reglas de auto-apadrinamiento y duplicados |
| `GestionAlertaService` | 10 | Alertas urgentes, inscripciones a convocatorias, aceptar/rechazar/cancelar |
| `GestionUsuarioService` | 7 | Bloquear/desbloquear/editar usuario (admin) |

Cada suite mockea únicamente los *ports* de salida (`UsuarioRepositoryPort`, `AnimalRepositoryPort`, etc.), sin infraestructura real — posible gracias a la arquitectura hexagonal. `mvn test` corre en verde: **59/59 passing, 0 failures, 0 errors**.

---

## 6. Documentación de uso

Instalación y ejecución local: ver [`README.md`](../README.md).

**Configuración:** ver sección 4 (variables de entorno) y `.env.example`.

---

## 7. Aspectos destacables

- **Multi-candidato en adopciones**: un animal permanece `DISPONIBLE` mientras reciba postulaciones; al aprobar una, las demás pendientes se rechazan automáticamente y se notifica a cada postulante vía evento de dominio.
- **Mapa propio sin servicios externos**: geocoding local por comuna (346 comunas) y renderizado SVG con D3 (zoom, clustering) — sin depender de Google Maps, Mapbox ni Leaflet+tiles externos, respetando la restricción *Zero External Services*.
- **Sistema de eventos de dominio**: `EventPublisherPort` desacopla la lógica de negocio (aprobar solicitud, cambiar estado de refugio, cancelar apadrinamiento) del envío de notificaciones in-app/email, permitiendo agregar nuevos canales sin tocar los servicios existentes.
- **Reutilización arquitectónica**: el módulo de Voluntariado se implementó extendiendo Alertas/RespuestaAlerta (convocatoria = alerta con tipo de ayuda, inscripción = respuesta) en vez de duplicar un modelo paralelo de Convocatoria/Inscripción — menos código, mismo flujo de aprobación/rechazo ya probado en Alertas.
- **Despliegue con verificación de integridad**: el pipeline original de CI/CD validaba por SHA que el servidor remoto quedara efectivamente en el commit esperado antes de reportar éxito en el deploy.

---

## 8. Limitaciones

- **Sin tests de integración/E2E**: la cobertura actual es 100% unitaria sobre la capa de aplicación (core). Falta Testcontainers para probar adaptadores JPA reales y `spring-security-test` para controladores. Queda como trabajo futuro.
- **Sin perfil `prod` de Spring separado**: backend usa el mismo `application.yml` en todos los entornos, diferenciando por variables de entorno inyectadas vía Docker Compose en vez de perfiles Spring dedicados.
- **`EstadoPostulacion`**: nombre de enum legacy (debería llamarse `EstadoSolicitud`), funcional pero con deuda de nomenclatura.
- **Supabase local vía CLI en el host** (no dockerizado dentro de `docker-compose.yml`): funciona, pero acopla el despliegue a que la instancia tenga Node/npm además de Docker.
