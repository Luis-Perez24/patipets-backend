-- Habilita el login del rol que usa Supabase Storage para conectarse a la base.
--
-- La imagen supabase/postgres ya crea el rol `supabase_storage_admin` y el
-- esquema `storage`, pero deja el rol sin contrasena: storage-api no puede
-- autenticarse hasta que se le asigna una.
--
-- `supabase_storage_admin` es un rol reservado y solo un superusuario puede
-- modificarlo. Los scripts de este directorio corren como `supabase_admin`,
-- que si lo es, de modo que el ALTER funciona aca y no desde `postgres`.
--
-- Se reutiliza POSTGRES_PASSWORD en vez de un secreto aparte: ambos roles son
-- administrativos de la misma base y viven en la misma red interna, asi que un
-- segundo secreto no agregaria aislamiento real, solo una variable mas que
-- mantener sincronizada.
--
-- Este script solo corre en la primera inicializacion del volumen. Si se cambia
-- POSTGRES_PASSWORD sobre un volumen existente, hay que repetir el ALTER a mano.

\set storage_pass `echo "$POSTGRES_PASSWORD"`

ALTER ROLE supabase_storage_admin WITH LOGIN PASSWORD :'storage_pass';
