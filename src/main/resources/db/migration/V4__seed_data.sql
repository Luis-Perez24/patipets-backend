INSERT INTO usuarios (nombre, email, password, rol, activo, fecha_registro) VALUES
('Admin PatiPets', 'admin@patipets.cl', '$2b$12$Hs52OOBj3bUuyOiuI8soy.yI8/jFqkCTM9VZYZ0g5rLPMSnTbSfGS', 'ADMIN', TRUE, NOW()),
('Ciudadano Ejemplo', 'ciudadano@patipets.cl', '$2b$12$cTY0M4ti7zErl1nuubcRDetKvKHnsH/zZsDjmk1w6fZLtA30onTAK', 'CIUDADANO', TRUE, NOW()),
('Refugio Ejemplo', 'refugio@patipets.cl', '$2b$12$O4hQwTSbufFKkXf5mhxDbeSm/lcDFIihhjba4t7Ge5QcPkuPIZaxi', 'REFUGIO', TRUE, NOW());

INSERT INTO refugios (nombre, direccion, region, latitud, longitud, capacidad, estado) VALUES
('Patita Feliz', 'Av. Libertador Bernardo O''Higgins 1234', 'Metropolitana', -33.4489, -70.6693, 50, 'APROBADO'),
('Huellitas del Sur', 'Calle Los Alerces 567', 'Biobío', -36.8269, -73.0498, 30, 'APROBADO'),
('Colitas Contentas', 'Pasaje El Mirador 890', 'Valparaíso', -33.0472, -71.6127, 40, 'APROBADO'),
('Nuevo Hogar', 'Av. Angamos 345', 'Antofagasta', -23.6509, -70.3975, 20, 'PENDIENTE'),
('Refugio Viejo', 'Camino Rural s/n', 'La Araucanía', -38.7396, -72.5901, 15, 'RECHAZADO');

INSERT INTO animales (nombre, especie, raza, edad, tamano, estado_salud, historia, estado_adopcion, refugio_id, fecha_registro) VALUES
('Luna', 'Perro', 'Labrador', 2, 'Grande', 'Sano', 'Luna fue rescatada de la calle. Es muy cariñosa y juguetona.', 'DISPONIBLE', 1, NOW() - INTERVAL '10 days'),
('Milo', 'Perro', 'Golden Retriever', 1, 'Grande', 'Sano', 'Cachorro lleno de energia, busca familia activa.', 'DISPONIBLE', 1, NOW() - INTERVAL '5 days'),
('Simba', 'Gato', 'Naranjo', 3, 'Mediano', 'Sano', 'Gato adulto muy tranquilo, ideal para departamento.', 'DISPONIBLE', 1, NOW() - INTERVAL '15 days'),
('Canela', 'Perro', 'Beagle', 4, 'Pequeño', 'Tratamiento', 'Requiere cuidado especial por alergia alimentaria.', 'DISPONIBLE', 1, NOW() - INTERVAL '20 days'),
('Niebla', 'Gato', 'Siames', 2, 'Pequeño', 'Sano', 'Gata siamesa rescatada de un arbol. Muy dulce.', 'DISPONIBLE', 2, NOW() - INTERVAL '8 days'),
('Thor', 'Perro', 'Pastor Aleman', 5, 'Grande', 'Sano', 'Excelente guardian, muy leal y obediente.', 'DISPONIBLE', 2, NOW() - INTERVAL '30 days'),
('Pelusa', 'Gato', 'Persa', 1, 'Pequeño', 'Sano', 'Cachorra juguetona, lista para adopcion.', 'DISPONIBLE', 2, NOW() - INTERVAL '3 days'),
('Rex', 'Perro', 'Dogo', 7, 'Grande', 'Cronico', 'Perro mayor con artritis, necesita familia paciente.', 'ADOPTADO', 2, NOW() - INTERVAL '60 days'),
('Copito', 'Perro', 'Poodle', 3, 'Pequeño', 'Sano', 'Perro muy inteligente y travieso.', 'DISPONIBLE', 3, NOW() - INTERVAL '12 days'),
('Luna', 'Gato', 'Negro', 6, 'Mediano', 'Sano', 'Gata adulta super carinosa.', 'DISPONIBLE', 3, NOW() - INTERVAL '25 days'),
('Rocky', 'Perro', 'Mestizo', 2, 'Mediano', 'Sano', 'Rescatado de la perrera, muy agradecido.', 'DISPONIBLE', 3, NOW() - INTERVAL '7 days'),
('Blanquita', 'Gato', 'Blanco', 8, 'Pequeño', 'Tratamiento', 'Gata mayor con problemas renales.', 'DISPONIBLE', 3, NOW() - INTERVAL '40 days');

INSERT INTO animal_fotos (animal_id, url_foto) VALUES
(1, 'https://patipets.cl/fotos/luna1.jpg'),
(1, 'https://patipets.cl/fotos/luna2.jpg'),
(2, 'https://patipets.cl/fotos/milo1.jpg'),
(3, 'https://patipets.cl/fotos/simba1.jpg'),
(5, 'https://patipets.cl/fotos/niebla1.jpg'),
(9, 'https://patipets.cl/fotos/copito1.jpg');
