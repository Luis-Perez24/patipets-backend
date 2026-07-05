CREATE TABLE IF NOT EXISTS respuestas_alerta (
    id BIGSERIAL PRIMARY KEY,
    alerta_id BIGINT NOT NULL REFERENCES alertas(id) ON DELETE CASCADE,
    usuario_id BIGINT NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    tipo_ayuda VARCHAR(50) NOT NULL DEFAULT 'OTRO',
    mensaje TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT respuestas_alerta_tipo_ayuda_check CHECK (tipo_ayuda IN ('PASEO', 'LIMPIEZA', 'TRANSPORTE', 'ATENCION', 'OTRO'))
);

CREATE INDEX IF NOT EXISTS idx_respuesta_alerta ON respuestas_alerta(alerta_id);
CREATE INDEX IF NOT EXISTS idx_respuesta_usuario ON respuestas_alerta(usuario_id);
