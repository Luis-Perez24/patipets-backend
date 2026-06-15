UPDATE usuarios SET
    numero_contacto = '+56999999901',
    ubicacion = 'Santiago, Chile',
    biografia = 'Administrador de PatiPets'
WHERE email = 'admin@patipets.cl';

UPDATE usuarios SET
    numero_contacto = '+56999999902',
    ubicacion = 'Valparaíso, Chile',
    biografia = 'Amante de los animales'
WHERE email = 'ciudadano@patipets.cl';

UPDATE usuarios SET
    numero_contacto = '+56999999903',
    ubicacion = 'Concepción, Chile',
    biografia = 'Refugio dedicado al rescate animal'
WHERE email = 'refugio@patipets.cl';

UPDATE refugios SET email = 'patitafeliz@correo.cl', numero_contacto = '+56911111111', comuna = 'Santiago' WHERE nombre = 'Patita Feliz';
UPDATE refugios SET email = 'huellitas@correo.cl', numero_contacto = '+56922222222', comuna = 'Concepción' WHERE nombre = 'Huellitas del Sur';
UPDATE refugios SET email = 'colitas@correo.cl', numero_contacto = '+56933333333', comuna = 'Valparaíso' WHERE nombre = 'Colitas Contentas';
UPDATE refugios SET email = 'nuevohogar@correo.cl', numero_contacto = '+56944444444', comuna = 'Antofagasta' WHERE nombre = 'Nuevo Hogar';
UPDATE refugios SET email = 'refugioviejo@correo.cl', numero_contacto = '+56955555555', comuna = 'Temuco' WHERE nombre = 'Refugio Viejo';

UPDATE animales SET personalidad = 'Juguetón' WHERE nombre = 'Luna' AND especie = 'Perro';
UPDATE animales SET personalidad = 'Juguetón' WHERE nombre = 'Milo';
UPDATE animales SET personalidad = 'Tranquilo' WHERE nombre = 'Simba';
UPDATE animales SET personalidad = 'Tímido' WHERE nombre = 'Canela';
UPDATE animales SET personalidad = 'Dulce' WHERE nombre = 'Niebla';
UPDATE animales SET personalidad = 'Protector' WHERE nombre = 'Thor';
UPDATE animales SET personalidad = 'Juguetón' WHERE nombre = 'Pelusa';
UPDATE animales SET personalidad = 'Tranquilo' WHERE nombre = 'Rex';
UPDATE animales SET personalidad = 'Inteligente' WHERE nombre = 'Copito';
UPDATE animales SET personalidad = 'Cariñoso' WHERE nombre = 'Luna' AND especie = 'Gato';
UPDATE animales SET personalidad = 'Agradecido' WHERE nombre = 'Rocky';
UPDATE animales SET personalidad = 'Tranquilo' WHERE nombre = 'Blanquita';
