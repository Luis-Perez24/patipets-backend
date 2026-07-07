CREATE TABLE IF NOT EXISTS animal_estado_salud (
    animal_id BIGINT NOT NULL REFERENCES animales(id),
    estado VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS animal_personalidad (
    animal_id BIGINT NOT NULL REFERENCES animales(id),
    rasgo VARCHAR(255)
);

-- Migrar datos existentes (como un solo elemento por ahora, si hubiera múltiples separados por coma no se separarán automáticamente, pero servirá para no perderlos)
INSERT INTO animal_estado_salud (animal_id, estado)
SELECT id, estado_salud FROM animales WHERE estado_salud IS NOT NULL AND trim(estado_salud) != '';

INSERT INTO animal_personalidad (animal_id, rasgo)
SELECT id, personalidad FROM animales WHERE personalidad IS NOT NULL AND trim(personalidad) != '';

-- Eliminar columnas antiguas
ALTER TABLE animales DROP COLUMN IF EXISTS estado_salud;
ALTER TABLE animales DROP COLUMN IF EXISTS personalidad;
