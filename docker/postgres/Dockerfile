# Igual motivo que docker/gateway/Dockerfile: el script de init se hornea
# en la imagen en vez de montarse como bind mount de una ruta relativa,
# porque el directorio contra el que Coolify resuelve esas rutas no
# replica el contenido del repo.
FROM supabase/postgres:15.8.1.060
COPY init/01-storage-role.sql /docker-entrypoint-initdb.d/zzz-01-storage-role.sql
