package com.patipets.core.application.services;

import com.patipets.core.application.events.SolicitudRefugioCambiadaEvent;
import com.patipets.core.application.ports.output.AnimalRepositoryPort;
import com.patipets.core.application.ports.output.EventPublisherPort;
import com.patipets.core.application.ports.output.ImageStoragePort;
import com.patipets.core.application.ports.output.RefugioRepositoryPort;
import com.patipets.core.application.ports.output.SolicitudAdopcionRepositoryPort;
import com.patipets.core.application.ports.output.UsuarioRepositoryPort;
import com.patipets.core.application.useCase.GestionRefugioUseCase;
import com.patipets.core.application.useCase.RefugioHistorial;
import com.patipets.core.domain.enums.EstadoAnimal;
import com.patipets.core.domain.enums.EstadoRefugio;
import com.patipets.core.domain.enums.Rol;
import com.patipets.core.domain.models.Animal;
import com.patipets.core.domain.models.PaginatedResult;
import com.patipets.core.domain.models.Refugio;
import com.patipets.core.domain.models.Usuario;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class GestionRefugioService implements GestionRefugioUseCase {

    private final RefugioRepositoryPort refugioRepository;
    private final AnimalRepositoryPort animalRepository;
    private final SolicitudAdopcionRepositoryPort solicitudRepository;
    private final ImageStoragePort imageStoragePort;
    private final UsuarioRepositoryPort usuarioRepository;
    private final EventPublisherPort eventPublisher;
    private final GeocodingService geocodingService;

    public GestionRefugioService(RefugioRepositoryPort refugioRepository,
                                  AnimalRepositoryPort animalRepository,
                                  SolicitudAdopcionRepositoryPort solicitudRepository,
                                  ImageStoragePort imageStoragePort,
                                  UsuarioRepositoryPort usuarioRepository,
                                  EventPublisherPort eventPublisher,
                                  GeocodingService geocodingService) {
        this.refugioRepository = refugioRepository;
        this.animalRepository = animalRepository;
        this.solicitudRepository = solicitudRepository;
        this.imageStoragePort = imageStoragePort;
        this.usuarioRepository = usuarioRepository;
        this.eventPublisher = eventPublisher;
        this.geocodingService = geocodingService;
    }

    @Override
    public Refugio solicitar(String nombre, String descripcion, String direccion, String region, String comuna,
                              Double latitud, Double longitud, Integer capacidad,
                              String email, String numeroContacto, Long usuarioId,
                              MultipartFile foto) throws IOException {
        if (usuarioId == null) {
            throw new IllegalArgumentException("Usuario no autenticado");
        }
        List<Refugio> existentes = refugioRepository.findSolicitudesByUsuario(usuarioId);
        boolean tieneSolicitudActiva = existentes.stream()
                .anyMatch(r -> r.getEstado() == EstadoRefugio.PENDIENTE || r.getEstado() == EstadoRefugio.APROBADO);
        if (tieneSolicitudActiva) {
            throw new IllegalArgumentException("Ya tienes una solicitud de refugio pendiente o aprobada. Solo se permite un refugio por usuario.");
        }

        String fotoUrl = (foto != null && !foto.isEmpty()) ? imageStoragePort.upload(foto) : null;

        Double finalLatitud = latitud;
        Double finalLongitud = longitud;
        if (finalLatitud == null || finalLongitud == null) {
            double[] resolved = geocodingService.geocode(direccion, comuna, region)
                    .orElse(new double[]{0.0, 0.0});
            finalLatitud = resolved[0];
            finalLongitud = resolved[1];
        }

        Refugio nuevo = new Refugio(
                null, nombre, descripcion, direccion, region, comuna,
                finalLatitud, finalLongitud, capacidad, email, numeroContacto,
                EstadoRefugio.PENDIENTE, fotoUrl,
                usuarioId, null, LocalDateTime.now()
        );
        Refugio guardado = refugioRepository.save(nuevo);
        return guardado;
    }

    @Override
    public Refugio actualizar(Long id, String nombre, String descripcion, String direccion,
                              String region, String comuna, Double latitud, Double longitud,
                              Integer capacidad, String email, String numeroContacto,
                              MultipartFile foto) throws IOException {
        Refugio existente = refugioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Refugio no encontrado: " + id));
        if (existente.getEstado() != EstadoRefugio.APROBADO) {
            throw new IllegalStateException(
                    "Solo se puede editar un refugio aprobado (estado actual: " + existente.getEstado() + ")");
        }

        String nuevaDireccion = direccion != null ? direccion : existente.getDireccion();
        String nuevaRegion = region != null ? region : existente.getRegion();
        String nuevaComuna = comuna != null ? comuna : existente.getComuna();
        boolean direccionCambio = (direccion != null && !direccion.equals(existente.getDireccion()))
                || (region != null && !region.equals(existente.getRegion()))
                || (comuna != null && !comuna.equals(existente.getComuna()));

        Double nuevaLat = existente.getLatitud();
        Double nuevaLon = existente.getLongitud();
        if (latitud != null && longitud != null) {
            nuevaLat = latitud;
            nuevaLon = longitud;
        } else if (direccionCambio) {
            double[] resolved = geocodingService.geocode(nuevaDireccion, nuevaComuna, nuevaRegion)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "No se pudo geolocalizar la nueva dirección. Verifica región, comuna y dirección."));
            nuevaLat = resolved[0];
            nuevaLon = resolved[1];
        }

        String fotoAnterior = existente.getFoto();
        String fotoUrl = fotoAnterior;
        if (foto != null && !foto.isEmpty()) {
            fotoUrl = imageStoragePort.upload(foto);
        }

        Refugio actualizado = new Refugio(
                existente.getId(),
                nombre != null ? nombre : existente.getNombre(),
                descripcion != null ? descripcion : existente.getDescripcion(),
                nuevaDireccion,
                nuevaRegion,
                nuevaComuna,
                nuevaLat, nuevaLon,
                capacidad != null ? capacidad : existente.getCapacidad(),
                email != null ? email : existente.getEmail(),
                numeroContacto != null ? numeroContacto : existente.getNumeroContacto(),
                existente.getEstado(),
                fotoUrl,
                existente.getUsuarioId(),
                existente.getUsuarioNombre(),
                existente.getFechaCreacion()
        );

        Refugio guardado = refugioRepository.save(actualizado);

        if (foto != null && !foto.isEmpty() && fotoAnterior != null && !fotoAnterior.equals(fotoUrl)) {
            try {
                imageStoragePort.delete(fotoAnterior);
            } catch (RuntimeException ignored) {
                // best-effort
            }
        }

        return guardado;
    }

    @Override
    public Refugio aprobar(Long id) {
        Refugio refugio = refugioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Refugio no encontrado: " + id));
        if (refugio.getEstado() != EstadoRefugio.PENDIENTE) {
            throw new IllegalStateException("El refugio no está en estado PENDIENTE");
        }
        Refugio actualizado = new Refugio(
                refugio.getId(), refugio.getNombre(), refugio.getDescripcion(), refugio.getDireccion(),
                refugio.getRegion(), refugio.getComuna(), refugio.getLatitud(),
                refugio.getLongitud(), refugio.getCapacidad(),
                refugio.getEmail(), refugio.getNumeroContacto(), EstadoRefugio.APROBADO,
                refugio.getFoto(), refugio.getUsuarioId(), refugio.getUsuarioNombre(),
                refugio.getFechaCreacion()
        );
        Refugio guardado = refugioRepository.save(actualizado);
        refugioRepository.vincularUsuario(refugio.getUsuarioId(), guardado.getId());
        promoverAAdminRefugio(refugio.getUsuarioId());
        eventPublisher.publicar(new SolicitudRefugioCambiadaEvent(
                guardado.getId(), guardado.getUsuarioId(), guardado.getNombre(), guardado.getEstado().name()));
        return guardado;
    }

    @Override
    public Refugio rechazar(Long id) {
        Refugio refugio = refugioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Refugio no encontrado: " + id));
        if (refugio.getEstado() != EstadoRefugio.PENDIENTE) {
            throw new IllegalStateException("El refugio no está en estado PENDIENTE");
        }
        return cambiarEstadoPendiente(refugio, EstadoRefugio.RECHAZADO, id);
    }

    @Override
    public Refugio cancelarPorUsuario(Long id, Long usuarioId) {
        Refugio refugio = refugioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Refugio no encontrado: " + id));
        if (refugio.getUsuarioId() == null || !refugio.getUsuarioId().equals(usuarioId)) {
            throw new IllegalArgumentException("No puedes cancelar una solicitud que no te pertenece");
        }
        if (refugio.getEstado() != EstadoRefugio.PENDIENTE) {
            throw new IllegalStateException("La solicitud no está en estado PENDIENTE");
        }
        return cambiarEstadoPendiente(refugio, EstadoRefugio.RECHAZADO, id);
    }

    private Refugio cambiarEstadoPendiente(Refugio refugio, EstadoRefugio nuevoEstado, Long id) {
        Refugio actualizado = new Refugio(
                refugio.getId(), refugio.getNombre(), refugio.getDescripcion(), refugio.getDireccion(),
                refugio.getRegion(), refugio.getComuna(), refugio.getLatitud(),
                refugio.getLongitud(), refugio.getCapacidad(),
                refugio.getEmail(), refugio.getNumeroContacto(), nuevoEstado,
                refugio.getFoto(), refugio.getUsuarioId(), refugio.getUsuarioNombre(),
                refugio.getFechaCreacion()
        );
        Refugio guardado = refugioRepository.save(actualizado);
        eventPublisher.publicar(new SolicitudRefugioCambiadaEvent(
                guardado.getId(), guardado.getUsuarioId(), guardado.getNombre(), guardado.getEstado().name()));
        return guardado;
    }

    private void promoverAAdminRefugio(Long usuarioId) {
        if (usuarioId == null) {
            return;
        }
        usuarioRepository.findById(usuarioId).ifPresent(usuario -> {
            if (usuario.getRol() == Rol.CIUDADANO) {
                Usuario promovido = new Usuario(
                        usuario.getId(), usuario.getNombre(), usuario.getEmail(), usuario.getPassword(),
                        Rol.REFUGIO, usuario.getFotoPerfil(), usuario.getNumeroContacto(), usuario.getUbicacion(),
                        usuario.getRegion(), usuario.getComuna(), usuario.getDireccion(), usuario.getBiografia(),
                        usuario.isActivo(), usuario.getFechaRegistro()
                );
                usuarioRepository.save(promovido);
            }
        });
    }

    @Override
    public List<Refugio> listarPendientes() {
        return refugioRepository.findByEstado(EstadoRefugio.PENDIENTE);
    }

    @Override
    public boolean perteneceAUsuario(Long refugioId, Long usuarioId) {
        return refugioRepository.perteneceAUsuario(refugioId, usuarioId);
    }

    @Override
    public Optional<Refugio> obtenerPorId(Long id) {
        return refugioRepository.findById(id);
    }

    @Override
    public List<Refugio> listarMisRefugios(Long usuarioId) {
        return refugioRepository.findSolicitudesByUsuario(usuarioId);
    }

    @Override
    public PaginatedResult<Refugio> listarTodos(String estado, String region, String busqueda, int page, int size) {
        return refugioRepository.findAll(estado, region, busqueda, page, size);
    }

    @Override
    public RefugioHistorial obtenerHistorial(Long refugioId) {
        Refugio refugio = refugioRepository.findById(refugioId)
                .orElseThrow(() -> new IllegalArgumentException("Refugio no encontrado: " + refugioId));
        if (refugio.getEstado() != EstadoRefugio.APROBADO) {
            throw new IllegalArgumentException("El refugio no está aprobado");
        }
        long totalAnimales = animalRepository.countByRefugioId(refugioId);
        long totalAdopciones = solicitudRepository.countByRefugioIdAndEstado(refugioId, "COMPLETADA");
        List<Animal> animalesAdoptados = animalRepository.findByRefugioIdAndEstado(refugioId, EstadoAnimal.ADOPTADO.name());
        return new RefugioHistorial(refugio.getId(), refugio.getNombre(),
                totalAnimales, totalAdopciones, animalesAdoptados);
    }
}
