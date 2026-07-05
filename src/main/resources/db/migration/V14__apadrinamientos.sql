CREATE TABLE IF NOT EXISTS apadrinamientos (
    id BIGSERIAL PRIMARY KEY,
    padrino_id BIGINT NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    animal_id BIGINT NOT NULL REFERENCES animales(id) ON DELETE CASCADE,
    refugio_id BIGINT NOT NULL REFERENCES refugios(id) ON DELETE CASCADE,
    tipo_apoyo VARCHAR(50) NOT NULL DEFAULT 'OTRO',
    fecha_inicio TIMESTAMP NOT NULL DEFAULT NOW(),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT apadrinamientos_tipo_apoyo_check CHECK (tipo_apoyo IN ('VISITAS', 'INSUMOS', 'PASEOS', 'ATENCION', 'OTRO'))
);

CREATE INDEX IF NOT EXISTS idx_apadrinamiento_padrino ON apadrinamientos(padrino_id);
CREATE INDEX IF NOT EXISTS idx_apadrinamiento_animal ON apadrinamientos(animal_id);
CREATE INDEX IF NOT EXISTS idx_apadrinamiento_refugio ON apadrinamientos(refugio_id);
