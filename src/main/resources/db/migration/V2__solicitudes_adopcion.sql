CREATE TABLE IF NOT EXISTS solicitudes_adopcion (
    id BIGSERIAL PRIMARY KEY,
    animal_id BIGINT NOT NULL,
    adoptante_id BIGINT NOT NULL,
    refugio_id BIGINT NOT NULL,
    estado VARCHAR(50) NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    nombre_completo VARCHAR(255) NOT NULL DEFAULT '',
    numero_contacto VARCHAR(50) NOT NULL DEFAULT '',
    direccion VARCHAR(500) NOT NULL DEFAULT '',
    nivel_actividad VARCHAR(50) NOT NULL DEFAULT 'MODERADO',
    horas_solo INTEGER NOT NULL DEFAULT 0,
    cuidado_vacaciones VARCHAR(50) NOT NULL DEFAULT 'OTRO',
    tipo_vivienda VARCHAR(50) NOT NULL DEFAULT 'OTRO',
    descripcion_espacio TEXT,
    tiene_ninos BOOLEAN NOT NULL DEFAULT FALSE,
    tiene_otras_mascotas BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE INDEX IF NOT EXISTS idx_solicitud_animal ON solicitudes_adopcion(animal_id);
CREATE INDEX IF NOT EXISTS idx_solicitud_adoptante ON solicitudes_adopcion(adoptante_id);
CREATE INDEX IF NOT EXISTS idx_solicitud_refugio ON solicitudes_adopcion(refugio_id);
CREATE INDEX IF NOT EXISTS idx_solicitud_estado ON solicitudes_adopcion(estado);
