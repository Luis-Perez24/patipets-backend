-- Migración: Actualizar valores de tipo_ayuda de singular a plural
-- para mantener consistencia con el enum TipoAyudaVoluntariado del backend

-- 1) Primero eliminar el CHECK constraint existente
ALTER TABLE respuestas_alerta DROP CONSTRAINT IF EXISTS respuestas_alerta_tipo_ayuda_check;

-- 2) Migrar datos existentes de singular a plural
UPDATE alertas SET tipo_ayuda = 'PASEOS' WHERE tipo_ayuda = 'PASEO';
UPDATE alertas SET tipo_ayuda = 'LIMPIEZAS' WHERE tipo_ayuda = 'LIMPIEZA';
UPDATE alertas SET tipo_ayuda = 'TRANSPORTES' WHERE tipo_ayuda = 'TRANSPORTE';
UPDATE alertas SET tipo_ayuda = 'ATENCIONES' WHERE tipo_ayuda = 'ATENCION';
UPDATE alertas SET tipo_ayuda = 'OTROS' WHERE tipo_ayuda = 'OTRO';

UPDATE respuestas_alerta SET tipo_ayuda = 'PASEOS' WHERE tipo_ayuda = 'PASEO';
UPDATE respuestas_alerta SET tipo_ayuda = 'LIMPIEZAS' WHERE tipo_ayuda = 'LIMPIEZA';
UPDATE respuestas_alerta SET tipo_ayuda = 'TRANSPORTES' WHERE tipo_ayuda = 'TRANSPORTE';
UPDATE respuestas_alerta SET tipo_ayuda = 'ATENCIONES' WHERE tipo_ayuda = 'ATENCION';
UPDATE respuestas_alerta SET tipo_ayuda = 'OTROS' WHERE tipo_ayuda = 'OTRO';

-- 3) Agregar el nuevo CHECK constraint con los valores en plural
ALTER TABLE respuestas_alerta ADD CONSTRAINT respuestas_alerta_tipo_ayuda_check
    CHECK (tipo_ayuda IN ('PASEOS', 'LIMPIEZAS', 'TRANSPORTES', 'ATENCIONES', 'OTROS'));
