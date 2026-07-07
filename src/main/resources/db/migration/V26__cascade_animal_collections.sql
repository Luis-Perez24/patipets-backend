-- V24__cascade_animal_collections.sql

-- animal_fotos
ALTER TABLE animal_fotos DROP CONSTRAINT IF EXISTS animal_fotos_animal_id_fkey;
ALTER TABLE animal_fotos ADD CONSTRAINT animal_fotos_animal_id_fkey FOREIGN KEY (animal_id) REFERENCES animales(id) ON DELETE CASCADE;

-- animal_estado_salud
ALTER TABLE animal_estado_salud DROP CONSTRAINT IF EXISTS animal_estado_salud_animal_id_fkey;
ALTER TABLE animal_estado_salud ADD CONSTRAINT animal_estado_salud_animal_id_fkey FOREIGN KEY (animal_id) REFERENCES animales(id) ON DELETE CASCADE;

-- animal_personalidad
ALTER TABLE animal_personalidad DROP CONSTRAINT IF EXISTS animal_personalidad_animal_id_fkey;
ALTER TABLE animal_personalidad ADD CONSTRAINT animal_personalidad_animal_id_fkey FOREIGN KEY (animal_id) REFERENCES animales(id) ON DELETE CASCADE;

-- solicitudes_adopcion (Añadimos FK con CASCADE)
ALTER TABLE solicitudes_adopcion DROP CONSTRAINT IF EXISTS solicitudes_adopcion_animal_id_fkey;
ALTER TABLE solicitudes_adopcion ADD CONSTRAINT solicitudes_adopcion_animal_id_fkey FOREIGN KEY (animal_id) REFERENCES animales(id) ON DELETE CASCADE;
