ALTER TABLE refugios
    ADD COLUMN usuario_id BIGINT REFERENCES usuarios(id),
    ADD COLUMN fecha_creacion TIMESTAMP NOT NULL DEFAULT NOW();
