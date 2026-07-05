INSERT INTO usuarios (nombre, email, password, rol, activo, fecha_registro)
SELECT 'Padrino Ejemplo', 'padrino@patipets.cl',
       '$2b$12$cTY0M4ti7zErl1nuubcRDetKvKHnsH/zZsDjmk1w6fZLtA30onTAK',
       'PADRINO', TRUE, NOW()
WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE email = 'padrino@patipets.cl');

INSERT INTO apadrinamientos (padrino_id, animal_id, refugio_id, tipo_apoyo, fecha_inicio, activo)
SELECT u.id, a.id, a.refugio_id, 'VISITAS', NOW() - INTERVAL '15 days', TRUE
FROM usuarios u, animales a
WHERE u.email = 'padrino@patipets.cl' AND a.nombre = 'Simba' AND a.especie = 'Gato';

INSERT INTO apadrinamientos (padrino_id, animal_id, refugio_id, tipo_apoyo, fecha_inicio, activo)
SELECT u.id, a.id, a.refugio_id, 'INSUMOS', NOW() - INTERVAL '10 days', TRUE
FROM usuarios u, animales a
WHERE u.email = 'padrino@patipets.cl' AND a.nombre = 'Niebla';

INSERT INTO apadrinamientos (padrino_id, animal_id, refugio_id, tipo_apoyo, fecha_inicio, activo)
SELECT u.id, a.id, a.refugio_id, 'PASEOS', NOW() - INTERVAL '5 days', TRUE
FROM usuarios u, animales a
WHERE u.email = 'padrino@patipets.cl' AND a.nombre = 'Luna' AND a.especie = 'Perro';
