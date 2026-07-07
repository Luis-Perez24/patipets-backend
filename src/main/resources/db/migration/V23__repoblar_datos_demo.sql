-- Limpieza completa de datos de demo/prueba (orden que respeta las FKs)
DELETE FROM solicitudes_adopcion;
DELETE FROM apadrinamientos;
DELETE FROM alertas;
DELETE FROM usuario_refugio;
DELETE FROM animal_fotos;
DELETE FROM animales;
DELETE FROM refugios;

ALTER SEQUENCE refugios_id_seq RESTART WITH 1;
ALTER SEQUENCE animales_id_seq RESTART WITH 1;

ALTER TABLE animales ALTER COLUMN personalidad TYPE VARCHAR(150);

-- ============================================================
-- REFUGIOS
-- ============================================================
INSERT INTO refugios (nombre, direccion, region, comuna, latitud, longitud, capacidad, estado, email, numero_contacto, descripcion, fecha_creacion) VALUES
('Patita Feliz', 'Av. Libertador Bernardo O''Higgins 1234', 'Metropolitana', 'Santiago', -33.4489, -70.6693, 50, 'APROBADO', 'patitafeliz@correo.cl', '+56911111111',
 'Patita Feliz nace en 2014 cuando un grupo de vecinos de Santiago Centro decidió unir esfuerzos para rescatar animales abandonados en la vía pública. Hoy contamos con instalaciones para 50 animales, un equipo veterinario permanente y un programa activo de esterilización gratuita para la comuna. Nuestro objetivo no es solo encontrar hogares, sino asegurarnos de que cada adopción sea responsable y para toda la vida.',
 '2014-03-10'),
('Huellitas del Sur', 'Calle Los Alerces 567', 'Biobío', 'Concepción', -36.8269, -73.0498, 30, 'APROBADO', 'huellitas@correo.cl', '+56922222222',
 'Huellitas del Sur opera en Concepción desde 2016, enfocado en el rescate de perros y gatos de la región del Biobío afectados por abandono o maltrato. Contamos con un equipo de voluntarios permanentes, convenios con clínicas veterinarias locales para esterilización a bajo costo, y un programa de padrinazgo para animales con necesidades especiales. Creemos que cada animal, sin importar su edad o condición de salud, merece una segunda oportunidad.',
 '2016-06-01'),
('Colitas Contentas', 'Pasaje El Mirador 890', 'Valparaíso', 'Valparaíso', -33.0472, -71.6127, 40, 'APROBADO', 'colitas@correo.cl', '+56933333333',
 'Colitas Contentas nació como un proyecto familiar en Valparaíso, cuando sus fundadores comenzaron a alimentar perros callejeros del sector puerto y terminaron adoptando a los primeros cuatro residentes del refugio. Hoy contamos con capacidad para 40 animales, un patio amplio con vista al mar y un fuerte compromiso con la esterilización comunitaria. Trabajamos de la mano con la municipalidad en campañas de tenencia responsable.',
 '2015-09-20'),
('Nuevo Hogar', 'Av. Angamos 345', 'Antofagasta', 'Antofagasta', -23.6509, -70.3975, 20, 'APROBADO', 'nuevohogar@correo.cl', '+56944444444',
 'Nuevo Hogar es un refugio joven ubicado en Antofagasta, creado en 2021 para dar respuesta al creciente número de animales abandonados en la región debido a la migración y el clima extremo del desierto. Contamos con instalaciones techadas adaptadas al calor, un sistema de agua permanente y alianzas con veterinarios de la zona para atención de urgencia. Buscamos crecer junto a la comunidad local para ofrecer más espacio cada año.',
 '2021-01-15'),
('Huellas del Bosque', 'Camino a Cajón, Km 8', 'Araucanía', 'Temuco', -38.7396, -72.5901, 25, 'APROBADO', 'huellasdelbosque@correo.cl', '+56955555555',
 'Huellas del Bosque trabaja en Temuco desde hace más de una década, rescatando animales abandonados en zonas rurales de la Araucanía, muchas veces tras eventos climáticos que dejan a las mascotas sin hogar. Nuestro refugio cuenta con un amplio terreno rodeado de bosque nativo, ideal para la rehabilitación de animales con traumas o desconfianza hacia las personas. Creemos en procesos de adopción sin apuro, priorizando siempre el bienestar del animal por sobre la rapidez del proceso.',
 '2014-11-05');

-- Re-vincular la cuenta demo de rol REFUGIO al nuevo "Patita Feliz"
INSERT INTO usuario_refugio (usuario_id, refugio_id)
SELECT u.id, r.id FROM usuarios u, refugios r
WHERE u.email = 'refugio@patipets.cl' AND r.nombre = 'Patita Feliz';

UPDATE refugios SET usuario_id = (SELECT id FROM usuarios WHERE email = 'refugio@patipets.cl')
WHERE nombre = 'Patita Feliz';

-- ============================================================
-- ANIMALES - Refugio 1: Patita Feliz
-- ============================================================
INSERT INTO animales (nombre, especie, raza, edad, tamano, estado_salud, personalidad, historia, estado_adopcion, refugio_id, fecha_registro) VALUES
('Luna', 'Perro', 'Labrador', 2, 'Grande', 'Sano', 'Juguetona, Cariñosa, Sociable con otros perros',
 'Luna llegó al refugio hace dos años tras ser rescatada de la calle en pleno invierno. Al principio era desconfiada, pero con paciencia se transformó en una perra extremadamente cariñosa y juguetona. Se lleva muy bien con otros perros y disfruta las caminatas largas. El equipo de Patita Feliz la describe como una de las más nobles del refugio, siempre atenta a los visitantes que llegan a conocer a los animales.',
 'DISPONIBLE', 1, NOW() - INTERVAL '10 days'),
('Milo', 'Perro', 'Golden Retriever', 1, 'Grande', 'Sano', 'Enérgico, Juguetón, Necesita ejercicio diario',
 'Milo es un cachorro lleno de energía que llegó al refugio hace un mes. Le encanta jugar a la pelota y busca una familia activa que lo saque a pasear todos los días.',
 'DISPONIBLE', 1, NOW() - INTERVAL '5 days'),
('Simba', 'Gato', 'Naranjo', 3, 'Mediano', 'Sano', 'Tranquilo, Independiente, Cariñoso a su manera',
 'Simba es un gato adulto tranquilo que prefiere la vida hogareña antes que la aventura. Pasa gran parte del día durmiendo en los lugares más soleados del refugio, pero cuando decide socializar es extremadamente cariñoso. Es ideal para un departamento, ya que no necesita grandes espacios para ser feliz, solo un buen cojín y comida a tiempo.',
 'DISPONIBLE', 1, NOW() - INTERVAL '15 days'),
('Canela', 'Perro', 'Beagle', 4, 'Pequeño', 'En tratamiento', 'Tímida al principio, Leal, Cariñosa',
 'Canela llegó al refugio con una alergia alimentaria que requirió varios meses de tratamiento veterinario. Hoy está estable y solo necesita una dieta especial que el refugio puede indicar en detalle. Es una perra tímida al principio, pero muy leal con quienes le dan tiempo para confiar. Adora las tardes tranquilas y el contacto físico una vez que se siente segura.',
 'DISPONIBLE', 1, NOW() - INTERVAL '20 days'),
('Rocky', 'Perro', 'Mestizo', 2, 'Mediano', 'Sano', 'Agradecido, Juguetón, Muy sociable',
 E'Rocky fue rescatado de la perrera municipal apenas dos días antes de que se cumpliera el plazo límite. Cuando llegó a Patita Feliz estaba desnutrido, asustado y desconfiaba de cualquier persona que se le acercara demasiado rápido. El equipo del refugio trabajó semana tras semana con él, usando refuerzo positivo y mucha paciencia, hasta que Rocky empezó a mover la cola cada vez que veía llegar a los voluntarios.\n\nHoy es un perro completamente distinto: juguetón, agradecido y sorprendentemente sociable tanto con otros perros como con gatos, algo poco común en perros con su historial. Le encanta perseguir pelotas, recibir rascados detrás de las orejas y dormir la siesta al sol del patio trasero. El refugio destaca que Rocky sería un compañero ideal para una familia con otras mascotas, ya que se adapta con facilidad y nunca muestra agresividad. Su transformación es una de las historias favoritas del equipo de Patita Feliz.',
 'DISPONIBLE', 1, NOW() - INTERVAL '7 days');

-- ============================================================
-- ANIMALES - Refugio 2: Huellitas del Sur
-- ============================================================
INSERT INTO animales (nombre, especie, raza, edad, tamano, estado_salud, personalidad, historia, estado_adopcion, refugio_id, fecha_registro) VALUES
('Niebla', 'Gato', 'Siamés', 2, 'Pequeño', 'Sano', 'Dulce, Curiosa, Le encanta dormir al sol',
 'Niebla fue encontrada durmiendo en el techo de una casa vecina al refugio. Es una gata tranquila que disfruta las tardes de sol junto a la ventana.',
 'DISPONIBLE', 2, NOW() - INTERVAL '8 days'),
('Thor', 'Perro', 'Pastor Australiano', 5, 'Grande', 'Sano', 'Protector, Leal, Obediente',
 E'Thor llegó a Huellitas del Sur hace cinco años, cuando apenas era un cachorro entregado por una familia que ya no podía hacerse cargo de un perro de su tamaño. Desde el primer día mostró un temperamento protector y observador, siempre atento a cualquier movimiento cerca de la entrada del refugio. Con el tiempo se convirtió en una especie de guardián no oficial del lugar, alertando al personal ante cualquier visita inesperada.\n\nA pesar de su instinto protector, Thor es un perro extremadamente leal y obediente con quienes conoce. Responde a órdenes básicas sin problema y disfruta las sesiones de entrenamiento como una forma de ejercicio mental. Necesita una familia con experiencia en perros grandes y de temperamento fuerte, que sepa canalizar su energía protectora de forma positiva. El equipo del refugio confía en que, con el entorno correcto, Thor puede ser el compañero más leal que alguien podría pedir.',
 'DISPONIBLE', 2, NOW() - INTERVAL '30 days'),
('Pelusa', 'Gato', 'Persa', 1, 'Pequeño', 'Sano', 'Juguetona, Curiosa',
 'Pelusa es una gatita persa juguetona y curiosa, siempre explorando cada rincón del refugio. Busca un hogar con paciencia para sus travesuras.',
 'DISPONIBLE', 2, NOW() - INTERVAL '3 days'),
('Rex', 'Perro', 'Mestizo', 7, 'Grande', 'Crónico', 'Tranquilo, Paciente, Cariñoso',
 'Rex es un perro mayor con artritis controlada que requiere medicación diaria y superficies blandas para descansar. A pesar de su edad, es un perro tranquilo y cariñoso que disfruta simplemente estar cerca de las personas. Fue adoptado recientemente por una familia que se comprometió a seguir su tratamiento de por vida.',
 'ADOPTADO', 2, NOW() - INTERVAL '60 days'),
('Kiwi', 'Otro', 'Hámster Sirio', 1, 'Pequeño', 'Sano', 'Curioso, Tranquilo, Ideal para espacios pequeños',
 'Kiwi es un hámster tranquilo y curioso, ideal para quienes viven en espacios pequeños. Le encanta acumular semillas en sus cachetes.',
 'DISPONIBLE', 2, NOW() - INTERVAL '2 days');

-- ============================================================
-- ANIMALES - Refugio 3: Colitas Contentas
-- ============================================================
INSERT INTO animales (nombre, especie, raza, edad, tamano, estado_salud, personalidad, historia, estado_adopcion, refugio_id, fecha_registro) VALUES
('Copito', 'Perro', 'Poodle', 3, 'Pequeño', 'Sano', 'Inteligente, Obediente, Juguetón',
 'Copito es un poodle pequeño e inteligente que aprende trucos nuevos con sorprendente rapidez. Fue entregado voluntariamente por su familia anterior debido a un cambio de ciudad. Es obediente, juguetón y se adapta bien tanto a departamentos como a casas con patio. Le encanta que le cepillen el pelaje todas las semanas.',
 'DISPONIBLE', 3, NOW() - INTERVAL '12 days'),
('Mora', 'Gato', 'Bicolor', 6, 'Mediano', 'Sano', 'Cariñosa, Tranquila, Le gusta el sol de la ventana',
 'Mora es una gata adulta de pelaje bicolor que fue rescatada de un basural en Valparaíso. Le tomó tiempo confiar en las personas, pero hoy es una gata tranquila y cariñosa que disfruta el sol de la ventana durante horas. No le gusta compartir espacio con otros gatos, por lo que se recomienda que sea la única mascota felina del hogar.',
 'DISPONIBLE', 3, NOW() - INTERVAL '25 days'),
('Toby', 'Perro', 'Mestizo', 2, 'Mediano', 'Sano', 'Agradecido, Sociable, Juguetón',
 'Toby llegó al refugio tras ser encontrado vagando por las calles de Valparaíso. Es un perro agradecido y sociable que se lleva bien con todos.',
 'DISPONIBLE', 3, NOW() - INTERVAL '7 days'),
('Blanquita', 'Gato', 'Blanco', 8, 'Pequeño', 'En tratamiento', 'Tranquila, Cariñosa, Necesita dieta especial',
 E'Blanquita es la residente más antigua de Colitas Contentas. Llegó al refugio hace ya varios años como una gata adulta con problemas renales que requieren una dieta especial y controles veterinarios periódicos. A pesar de su condición, ha vivido tranquila y estable gracias a los cuidados constantes del equipo, y su calidad de vida es excelente mientras siga la rutina indicada por el veterinario.\n\nEs una gata serena, de pocas exigencias, que disfruta principalmente de un lugar cálido donde dormir y caricias suaves detrás de las orejas. No le interesa jugar demasiado, pero ronronea con facilidad apenas siente compañía cerca. El refugio busca para ella una familia paciente, consciente de que adoptar a una gata mayor con necesidades especiales es un compromiso distinto, pero también una de las adopciones más gratificantes: Blanquita tiene mucho cariño para dar a quien le dé una última etapa tranquila.',
 'DISPONIBLE', 3, NOW() - INTERVAL '40 days'),
('Duna', 'Perro', 'Pastor Australiano', 3, 'Mediano', 'Sano', 'Enérgica, Leal, Le encanta correr',
 'Duna es una pastora australiana llena de energía que necesita ejercicio diario para estar feliz. Fue encontrada corriendo suelta cerca de la carretera y, tras semanas de búsqueda, nunca apareció su familia original. Es extremadamente leal e inteligente, ideal para una familia activa que disfrute de las caminatas largas o el trote.',
 'DISPONIBLE', 3, NOW() - INTERVAL '18 days');

-- ============================================================
-- ANIMALES - Refugio 4: Nuevo Hogar
-- ============================================================
INSERT INTO animales (nombre, especie, raza, edad, tamano, estado_salud, personalidad, historia, estado_adopcion, refugio_id, fecha_registro) VALUES
('Coco', 'Perro', 'Golden Retriever', 1, 'Grande', 'Sano', 'Juguetón, Cariñoso, Ideal para familias con niños',
 'Coco es un cachorro de golden retriever que llegó al refugio junto a sus hermanos de camada. Es juguetón, cariñoso y particularmente paciente con los niños que visitan Nuevo Hogar. Todavía está aprendiendo modales básicos, pero avanza rápido gracias a las sesiones de entrenamiento semanales del refugio.',
 'DISPONIBLE', 4, NOW() - INTERVAL '9 days'),
('Nala', 'Gato', 'Atigrada', 2, 'Mediano', 'Sano', 'Independiente, Curiosa, Cazadora nata',
 'Nala es una gata atigrada con un instinto cazador muy marcado, siempre atenta a cualquier movimiento en el patio. Es independiente y no exige demasiada atención, pero cuando busca cariño se acurruca sin aviso. Se recomienda un hogar con acceso a una ventana o balcón seguro.',
 'DISPONIBLE', 4, NOW() - INTERVAL '14 days'),
('Otto', 'Perro', 'Bulldog Francés', 2, 'Pequeño', 'Sano', 'Tranquilo, Cariñoso, Un poco terco',
 'Otto es un bulldog francés tranquilo y algo terco, pero increíblemente cariñoso una vez que te conoce. Busca un hogar sin muchas escaleras.',
 'DISPONIBLE', 4, NOW() - INTERVAL '4 days'),
('Michi', 'Gato', 'Británico de Pelo Corto', 4, 'Mediano', 'Sano', 'Tranquilo, Observador, Le gusta la rutina',
 'Michi es un gato de pelo corto tranquilo y observador, de esos que prefieren mirar el mundo desde la altura de un mueble antes que participar del caos. Llegó al refugio hace varios meses tras ser encontrado en buen estado cerca de una plaza. Se adapta rápido a las rutinas y no da mayores sorpresas.',
 'DISPONIBLE', 4, NOW() - INTERVAL '22 days'),
('Bruno', 'Perro', 'Cavalier King Charles', 6, 'Pequeño', 'Sano', 'Dulce, Paciente, El favorito de los niños del refugio',
 E'Bruno lleva más tiempo que cualquier otro animal esperando una familia en Nuevo Hogar. Llegó como cachorro hace seis años, pero por ser un perro de tamaño mediano y edad avanzada, ha sido sistemáticamente pasado por alto por adoptantes que buscan cachorros más jóvenes. Aun así, mantiene un ánimo dulce y paciente que sorprende a quienes lo conocen.\n\nEs, sin duda, el favorito de los niños que visitan el refugio en las jornadas de puertas abiertas: se sienta pacientemente a que lo abracen y nunca muestra signos de molestia. Le encanta recibir cariño, dormir en superficies suaves y acompañar a quien esté cerca, sin exigir demasiada actividad física. El equipo de Nuevo Hogar cree firmemente que Bruno merece pasar sus años dorados en un hogar de verdad, no en un refugio, por más cuidado que se le entregue ahí.',
 'DISPONIBLE', 4, NOW() - INTERVAL '35 days');

-- ============================================================
-- ANIMALES - Refugio 5: Huellas del Bosque
-- ============================================================
INSERT INTO animales (nombre, especie, raza, edad, tamano, estado_salud, personalidad, historia, estado_adopcion, refugio_id, fecha_registro) VALUES
('Mishu', 'Gato', 'Abisinio', 3, 'Mediano', 'Sano', 'Activa, Inteligente, Le encanta trepar',
 'Mishu es una gata abisinia muy activa que disfruta trepar cualquier superficie disponible en el refugio. Es curiosa e inteligente, por lo que necesita estímulo constante para no aburrirse. Se lleva bien con otros gatos siempre que respeten su espacio.',
 'DISPONIBLE', 5, NOW() - INTERVAL '11 days'),
('Rayo', 'Perro', 'Mestizo', 4, 'Mediano', 'Sano', 'Veloz, Juguetón, Sociable',
 'Rayo es un perro mestizo veloz y juguetón que fue rescatado junto a su hermana cerca de un camino rural en la región. Le encanta correr en el patio del refugio y socializa sin problema con otros perros y visitantes. Está en busca de una familia que disfrute las actividades al aire libre.',
 'DISPONIBLE', 5, NOW() - INTERVAL '16 days'),
('Perla', 'Gato', 'Bicolor', 5, 'Pequeño', 'Sano', 'Reservada al principio, Muy leal una vez que confía',
 E'Perla llegó a Huellas del Bosque después de ser encontrada en condiciones difíciles cerca de un vertedero en las afueras de Temuco. Al principio era sumamente reservada, se escondía apenas alguien entraba a la sala y rechazaba cualquier intento de contacto físico. El equipo del refugio decidió no forzar el proceso y simplemente le dio tiempo, alimento y un espacio seguro donde sentirse tranquila.\n\nMeses después, Perla comenzó a acercarse por voluntad propia a quienes la alimentaban todos los días, hasta convertirse en una gata profundamente leal con su círculo de confianza. Sigue siendo cautelosa con desconocidos, pero quienes logran ganarse su confianza descubren a una compañera fiel y cariñosa. El refugio recomienda su adopción a personas con experiencia en gatos tímidos, dispuestas a respetar sus tiempos sin apresurar el vínculo.',
 'DISPONIBLE', 5, NOW() - INTERVAL '50 days'),
('Fresia', 'Perro', 'Beagle', 1, 'Pequeño', 'Sano', 'Curiosa, Juguetona, Olfato incansable',
 'Fresia es una cachorra beagle con un olfato incansable y una energía contagiosa. Le encanta seguir rastros por el patio del refugio.',
 'DISPONIBLE', 5, NOW() - INTERVAL '6 days'),
('Pepe', 'Otro', 'Perico Australiano', 1, 'Pequeño', 'Sano', 'Sociable, Ruidoso por las mañanas, Aprende a silbar',
 'Pepe es un perico australiano sociable que se ha vuelto parte del ambiente diario de Huellas del Bosque con su canto matutino. Está aprendiendo a silbar algunas melodías gracias a la paciencia del equipo de voluntarios. Es ideal para quienes buscan una mascota activa y comunicativa.',
 'DISPONIBLE', 5, NOW() - INTERVAL '13 days');

-- ============================================================
-- FOTOS (URLs reales verificadas de Unsplash, 1 a 5 por animal)
-- ============================================================
INSERT INTO animal_fotos (animal_id, url_foto)
SELECT a.id, f.url FROM animales a
JOIN (VALUES
  ('Luna', 1, 'https://images.unsplash.com/photo-1552053831-71594a27632d?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80'),
  ('Luna', 1, 'https://images.unsplash.com/photo-1587300003388-59208cc962cb?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80'),
  ('Milo', 1, 'https://images.unsplash.com/photo-1507146426996-ef05306b995a?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80'),
  ('Simba', 1, 'https://images.unsplash.com/photo-1519052537078-e6302a4968d4?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80'),
  ('Simba', 1, 'https://images.unsplash.com/photo-1548681528-6a5c45b66b42?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80'),
  ('Canela', 1, 'https://images.unsplash.com/photo-1543466835-00a7907e9de1?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80'),
  ('Rocky', 1, 'https://images.unsplash.com/photo-1615751072497-5f5169febe17?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80'),
  ('Rocky', 1, 'https://images.unsplash.com/photo-1548199973-03cce0bbc87b?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80'),
  ('Rocky', 1, 'https://images.unsplash.com/photo-1450778869180-41d0601e046e?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80'),

  ('Niebla', 2, 'https://images.unsplash.com/photo-1518791841217-8f162f1e1131?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80'),
  ('Thor', 2, 'https://images.unsplash.com/photo-1576201836106-db1758fd1c97?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80'),
  ('Thor', 2, 'https://images.unsplash.com/photo-1548199973-03cce0bbc87b?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80'),
  ('Pelusa', 2, 'https://images.unsplash.com/photo-1592194996308-7b43878e84a6?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80'),
  ('Rex', 2, 'https://images.unsplash.com/photo-1615751072497-5f5169febe17?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80'),
  ('Kiwi', 2, 'https://images.unsplash.com/photo-1425082661705-1834bfd09dca?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80'),

  ('Copito', 3, 'https://images.unsplash.com/photo-1560807707-8cc77767d783?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80'),
  ('Mora', 3, 'https://images.unsplash.com/photo-1514888286974-6c03e2ca1dba?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80'),
  ('Toby', 3, 'https://images.unsplash.com/photo-1548199973-03cce0bbc87b?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80'),
  ('Blanquita', 3, 'https://images.unsplash.com/photo-1520315342629-6ea920342047?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80'),
  ('Duna', 3, 'https://images.unsplash.com/photo-1576201836106-db1758fd1c97?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80'),

  ('Coco', 4, 'https://images.unsplash.com/photo-1587300003388-59208cc962cb?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80'),
  ('Coco', 4, 'https://images.unsplash.com/photo-1507146426996-ef05306b995a?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80'),
  ('Nala', 4, 'https://images.unsplash.com/photo-1495360010541-f48722b34f7d?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80'),
  ('Nala', 4, 'https://images.unsplash.com/photo-1518791841217-8f162f1e1131?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80'),
  ('Otto', 4, 'https://images.unsplash.com/photo-1583512603805-3cc6b41f3edb?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80'),
  ('Michi', 4, 'https://images.unsplash.com/photo-1592194996308-7b43878e84a6?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80'),
  ('Bruno', 4, 'https://images.unsplash.com/photo-1560807707-8cc77767d783?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80'),

  ('Mishu', 5, 'https://images.unsplash.com/photo-1571566882372-1598d88abd90?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80'),
  ('Rayo', 5, 'https://images.unsplash.com/photo-1548199973-03cce0bbc87b?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80'),
  ('Rayo', 5, 'https://images.unsplash.com/photo-1450778869180-41d0601e046e?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80'),
  ('Perla', 5, 'https://images.unsplash.com/photo-1543852786-1cf6624b9987?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80'),
  ('Fresia', 5, 'https://images.unsplash.com/photo-1543466835-00a7907e9de1?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80'),
  ('Pepe', 5, 'https://images.unsplash.com/photo-1552728089-57bdde30beb3?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80')
) AS f(nombre, refugio_id, url)
ON a.nombre = f.nombre AND a.refugio_id = f.refugio_id;
