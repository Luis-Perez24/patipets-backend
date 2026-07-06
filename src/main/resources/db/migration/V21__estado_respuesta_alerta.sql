ALTER TABLE respuestas_alerta
    ADD COLUMN IF NOT EXISTS estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVA',
    ADD COLUMN IF NOT EXISTS fecha_cancelacion TIMESTAMP,
    ADD COLUMN IF NOT EXISTS motivo_cancelacion TEXT;

ALTER TABLE respuestas_alerta
    DROP CONSTRAINT IF EXISTS respuestas_alerta_estado_check;

ALTER TABLE respuestas_alerta
    ADD CONSTRAINT respuestas_alerta_estado_check
    CHECK (estado IN ('ACTIVA', 'CANCELADA'));

CREATE INDEX IF NOT EXISTS idx_respuesta_estado ON respuestas_alerta(estado);
