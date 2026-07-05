ALTER TABLE alertas
    ADD COLUMN IF NOT EXISTS tipo_ayuda VARCHAR(50),
    ADD COLUMN IF NOT EXISTS fecha DATE,
    ADD COLUMN IF NOT EXISTS perfil_requerido TEXT;

ALTER TABLE respuestas_alerta
    ADD COLUMN IF NOT EXISTS disponibilidad TEXT;
