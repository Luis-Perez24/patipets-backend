INSERT INTO usuarios (nombre, email, password, rol, activo, fecha_registro)
SELECT 'Voluntario Ejemplo', 'voluntario@patipets.cl',
       '$2b$12$cTY0M4ti7zErl1nuubcRDetKvKHnsH/zZsDjmk1w6fZLtA30onTAK',
       'VOLUNTARIO', TRUE, NOW()
WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE email = 'voluntario@patipets.cl');

INSERT INTO alertas (titulo, descripcion, nivel_urgencia, refugio_id, creado_por, activa, created_at)
SELECT 'Se necesitan paseadores de perros', 'Buscamos voluntarios para pasear a 5 perros grandes durante la mañana. Se requiere disponibilidad de al menos 2 horas.',
       'MEDIO', r.id, u.id, TRUE, NOW() - INTERVAL '3 days'
FROM refugios r, usuarios u
WHERE r.nombre = 'Patita Feliz' AND u.email = 'refugio@patipets.cl';

INSERT INTO alertas (titulo, descripcion, nivel_urgencia, refugio_id, creado_por, activa, created_at)
SELECT 'Apoyo en limpieza de instalaciones', 'Necesitamos ayuda para limpiar y desinfectar las jaulas y áreas comunes del refugio. Se proveen implementos de limpieza.',
       'BAJO', r.id, u.id, TRUE, NOW() - INTERVAL '7 days'
FROM refugios r, usuarios u
WHERE r.nombre = 'Huellitas del Sur' AND u.email = 'refugio@patipets.cl';

INSERT INTO alertas (titulo, descripcion, nivel_urgencia, refugio_id, creado_por, activa, created_at)
SELECT 'Emergencia: transporte de animales rescatados', 'Acabamos de rescatar 8 perros de un criadero clandestino. Necesitamos urgentemente transporte para llevarlos al veterinario. Se requiere vehículo grande.',
       'URGENTE', r.id, u.id, TRUE, NOW() - INTERVAL '1 day'
FROM refugios r, usuarios u
WHERE r.nombre = 'Patita Feliz' AND u.email = 'refugio@patipets.cl';

INSERT INTO respuestas_alerta (alerta_id, usuario_id, tipo_ayuda, mensaje, created_at)
SELECT a.id, u.id, 'PASEO', 'Puedo ir los lunes, miércoles y viernes en la mañana. Tengo experiencia paseando perros grandes.',
       NOW() - INTERVAL '2 days'
FROM alertas a, usuarios u
WHERE a.titulo = 'Se necesitan paseadores de perros' AND u.email = 'voluntario@patipets.cl';

INSERT INTO respuestas_alerta (alerta_id, usuario_id, tipo_ayuda, mensaje, created_at)
SELECT a.id, u.id, 'TRANSPORTE', 'Tengo una camioneta y puedo ayudar con el transporte. Estoy disponible todo el día.',
       NOW() - INTERVAL '12 hours'
FROM alertas a, usuarios u
WHERE a.titulo = 'Emergencia: transporte de animales rescatados' AND u.email = 'voluntario@patipets.cl';
