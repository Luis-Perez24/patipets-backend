ALTER TABLE refugios
    ADD COLUMN email VARCHAR(255),
    ADD COLUMN numero_contacto VARCHAR(50),
    ADD COLUMN comuna VARCHAR(100);

ALTER TABLE animales
    ADD COLUMN personalidad VARCHAR(50);

ALTER TABLE usuarios
    ADD COLUMN numero_contacto VARCHAR(50),
    ADD COLUMN ubicacion VARCHAR(255),
    ADD COLUMN biografia TEXT;
