CREATE TABLE IF NOT EXISTS usuarios (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    rol VARCHAR(50) NOT NULL DEFAULT 'CIUDADANO',
    foto_perfil VARCHAR(500),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_registro TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_usuarios_email ON usuarios(email);

CREATE TABLE IF NOT EXISTS refugios (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    direccion VARCHAR(500),
    region VARCHAR(100) NOT NULL,
    latitud DOUBLE PRECISION,
    longitud DOUBLE PRECISION,
    capacidad INTEGER,
    estado VARCHAR(50) NOT NULL DEFAULT 'PENDIENTE'
);

CREATE INDEX IF NOT EXISTS idx_refugio_estado ON refugios(estado);
CREATE INDEX IF NOT EXISTS idx_refugio_region ON refugios(region);

CREATE TABLE IF NOT EXISTS animales (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    especie VARCHAR(100) NOT NULL,
    raza VARCHAR(100),
    edad INTEGER,
    tamano VARCHAR(50),
    estado_salud VARCHAR(100),
    historia TEXT,
    estado_adopcion VARCHAR(50) NOT NULL DEFAULT 'DISPONIBLE',
    refugio_id BIGINT NOT NULL REFERENCES refugios(id),
    fecha_registro TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_animal_refugio ON animales(refugio_id);
CREATE INDEX IF NOT EXISTS idx_animal_estado ON animales(estado_adopcion);
CREATE INDEX IF NOT EXISTS idx_animal_especie ON animales(especie);

CREATE TABLE IF NOT EXISTS animal_fotos (
    animal_id BIGINT NOT NULL REFERENCES animales(id),
    url_foto VARCHAR(500)
);
