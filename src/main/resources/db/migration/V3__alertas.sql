CREATE TABLE IF NOT EXISTS alertas (
    id BIGSERIAL PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    descripcion TEXT,
    nivel_urgencia VARCHAR(50) NOT NULL,
    refugio_id BIGINT NOT NULL REFERENCES refugios(id),
    creado_por BIGINT NOT NULL REFERENCES usuarios(id),
    activa BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT alertas_nivel_urgencia_check CHECK (nivel_urgencia IN ('BAJO', 'MEDIO', 'URGENTE'))
);

CREATE INDEX IF NOT EXISTS idx_alerta_refugio ON alertas(refugio_id);
CREATE INDEX IF NOT EXISTS idx_alerta_activa ON alertas(activa);
