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
        refugioRepository.vincularUsuario(usuarioId, guardado.getId());
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
        Refugio actualizado = new Refugio(
                refugio.getId(), refugio.getNombre(), refugio.getDescripcion(), refugio.getDireccion(),
                refugio.getRegion(), refugio.getComuna(), refugio.getLatitud(),
                refugio.getLongitud(), refugio.getCapacidad(),
                refugio.getEmail(), refugio.getNumeroContacto(), EstadoRefugio.RECHAZADO,
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
        return refugioRepository.findByUsuario(usuarioId);
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
