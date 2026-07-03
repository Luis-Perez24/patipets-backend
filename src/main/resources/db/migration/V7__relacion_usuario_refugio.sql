CREATE TABLE IF NOT EXISTS usuario_refugio (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    refugio_id BIGINT NOT NULL REFERENCES refugios(id) ON DELETE CASCADE,
    UNIQUE (usuario_id, refugio_id)
);

CREATE INDEX IF NOT EXISTS idx_usuario_refugio_usuario ON usuario_refugio(usuario_id);
CREATE INDEX IF NOT EXISTS idx_usuario_refugio_refugio ON usuario_refugio(refugio_id);

INSERT INTO usuario_refugio (usuario_id, refugio_id)
SELECT u.id, r.id
FROM usuarios u
JOIN refugios r ON r.nombre = 'Patita Feliz'
WHERE u.email = 'refugio@patipets.cl'
ON CONFLICT DO NOTHING;
