# Nginx del gateway de Storage, con su configuracion horneada en la imagen.
#
# No se monta nginx.conf como bind mount de una ruta relativa del repo:
# Coolify construye este servicio en un directorio de build efimero
# (/artifacts/...) pero resuelve los `volumes:` de rutas relativas del
# compose contra otro directorio separado
# (/data/coolify/applications/<uuid>/) que no replica el contenido del
# repo. Si el archivo de origen no existe ahi, Docker crea una carpeta
# vacia en su lugar y el mount falla contra el archivo destino dentro del
# contenedor. Copiarlo en el build evita depender de ese mecanismo.
FROM nginx:1.27-alpine
COPY nginx.conf /etc/nginx/conf.d/default.conf
