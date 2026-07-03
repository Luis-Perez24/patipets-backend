package com.patipets.core.application.services;

import com.patipets.core.application.events.EstadoSolicitudCambiadoEvent;
import com.patipets.core.application.ports.output.AnimalRepositoryPort;
import com.patipets.core.application.ports.output.EventPublisherPort;
import com.patipets.core.application.ports.output.ImageStoragePort;
import com.patipets.core.application.ports.output.RefugioRepositoryPort;
import com.patipets.core.application.ports.output.SolicitudAdopcionRepositoryPort;
import com.patipets.core.application.useCase.GestionAnimalUseCase;
import com.patipets.core.domain.enums.CuidadoVacaciones;
import com.patipets.core.domain.enums.EstadoAnimal;
import com.patipets.core.domain.enums.EstadoPostulacion;
import com.patipets.core.domain.enums.EstadoRefugio;
import com.patipets.core.domain.enums.NivelActividad;
import com.patipets.core.domain.enums.TipoVivienda;
import com.patipets.core.domain.models.Animal;
import com.patipets.core.domain.models.Refugio;
import com.patipets.core.domain.models.SolicitudAdopcion;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GestionAnimalService implements GestionAnimalUseCase {

    private static final int MAX_FOTOS_POR_ANIMAL = 5;

    private final AnimalRepositoryPort animalRepository;
    private final SolicitudAdopcionRepositoryPort solicitudRepository;
    private final RefugioRepositoryPort refugioRepository;
    private final ImageStoragePort imageStoragePort;
    private final EventPublisherPort eventPublisher;

    public GestionAnimalService(AnimalRepositoryPort animalRepository,
                                 SolicitudAdopcionRepositoryPort solicitudRepository,
                                 RefugioRepositoryPort refugioRepository,
                                 ImageStoragePort imageStoragePort,
                                 EventPublisherPort eventPublisher) {
        this.animalRepository = animalRepository;
        this.solicitudRepository = solicitudRepository;
        this.refugioRepository = refugioRepository;
        this.imageStoragePort = imageStoragePort;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Animal crearAnimal(String nombre, String especie, String raza, Integer edad,
                               String tamano, String personalidad, String estadoSalud,
                               String historia, Long refugioId, List<MultipartFile> archivos) throws IOException {
        Refugio refugio = refugioRepository.findById(refugioId)
                .orElseThrow(() -> new IllegalArgumentException("Refugio no encontrado: " + refugioId));
        if (refugio.getEstado() != EstadoRefugio.APROBADO) {
            throw new IllegalArgumentException("No se pueden agregar animales a un refugio que no está aprobado");
        }
        if (archivos != null && archivos.size() > MAX_FOTOS_POR_ANIMAL) {
            throw new IllegalArgumentException("Máximo " + MAX_FOTOS_POR_ANIMAL + " fotos por animal");
        }

        List<String> fotos = subirFotos(archivos);
        try {
            Animal animal = new Animal(
                    null, nombre, especie, raza, edad, tamano, personalidad, estadoSalud, historia,
                    EstadoAnimal.DISPONIBLE, refugioId, null, null, fotos, LocalDateTime.now()
            );
            return animalRepository.save(animal);
        } catch (RuntimeException e) {
            eliminarFotos(fotos);
            throw e;
        }
    }

    @Override
    public Animal actualizarAnimal(Long id, Long refugioId, String nombre, String especie, String raza,
                                    Integer edad, String tamano, String personalidad,
                                    String estadoSalud, String historia, String estadoAdopcion,
                                    List<String> fotosAMantener, List<MultipartFile> archivosNuevos) throws IOException {
        Animal existente = animalRepository.findById(id)
                .filter(a -> a.getRefugioId().equals(refugioId))
                .orElseThrow(() -> new IllegalArgumentException("Animal no encontrado: " + id));
        EstadoAnimal nuevoEstado = estadoAdopcion != null
                ? EstadoAnimal.valueOf(estadoAdopcion.toUpperCase())
                : existente.getEstadoAdopcion();

        List<String> aMantener = fotosAMantener != null ? fotosAMantener : List.of();
        int totalFotos = aMantener.size() + (archivosNuevos != null ? archivosNuevos.size() : 0);
        if (totalFotos > MAX_FOTOS_POR_ANIMAL) {
            throw new IllegalArgumentException("Máximo " + MAX_FOTOS_POR_ANIMAL + " fotos por animal");
        }

        List<String> fotosActuales = existente.getFotos() != null ? existente.getFotos() : List.of();
        List<String> fotosARemover = fotosActuales.stream()
                .filter(url -> !aMantener.contains(url))
                .collect(Collectors.toList());

        List<String> fotosNuevas = subirFotos(archivosNuevos);
        List<String> fotosFinales = new ArrayList<>(aMantener);
        fotosFinales.addAll(fotosNuevas);

        Animal actualizado = new Animal(
                id, nombre, especie, raza, edad, tamano, personalidad, estadoSalud, historia,
                nuevoEstado, existente.getRefugioId(), null, null, fotosFinales, existente.getFechaRegistro()
        );

        Animal guardado;
        try {
            guardado = animalRepository.save(actualizado);
        } catch (RuntimeException e) {
            eliminarFotos(fotosNuevas);
            throw e;
        }
        eliminarFotos(fotosARemover);
        return guardado;
    }

    @Override
    public void eliminarAnimal(Long id, Long refugioId) {
        Animal existente = animalRepository.findById(id)
                .filter(a -> a.getRefugioId().equals(refugioId))
                .orElseThrow(() -> new IllegalArgumentException("Animal no encontrado: " + id));
        animalRepository.deleteById(id);
        eliminarFotos(existente.getFotos());
    }

    private List<String> subirFotos(List<MultipartFile> archivos) throws IOException {
        if (archivos == null || archivos.isEmpty()) {
            return List.of();
        }
        List<String> urls = new ArrayList<>();
        for (MultipartFile archivo : archivos) {
            urls.add(imageStoragePort.upload(archivo));
        }
        return urls;
    }

    private void eliminarFotos(List<String> urls) {
        if (urls == null) {
            return;
        }
        for (String url : urls) {
            imageStoragePort.delete(url);
        }
    }

    @Override
    public List<Animal> listarPorRefugio(Long refugioId) {
        return animalRepository.findByRefugioId(refugioId);
    }

    @Override
    public SolicitudAdopcion solicitar(Long animalId, Long adoptanteId, String nombreCompleto,
                                        String numeroContacto, String direccion, String nivelActividad,
                                        Integer horasSolo, String cuidadoVacaciones, String tipoVivienda,
                                        String descripcionEspacio, Boolean tieneNinos, Boolean tieneOtrasMascotas) {
        Animal animal = animalRepository.findById(animalId)
                .orElseThrow(() -> new IllegalArgumentException("Animal no encontrado: " + animalId));
        if (animal.getEstadoAdopcion() != EstadoAnimal.DISPONIBLE) {
            throw new IllegalStateException("El animal no está disponible para adopción");
        }
        SolicitudAdopcion solicitud = new SolicitudAdopcion(
                null, animalId, adoptanteId, animal.getRefugioId(),
                EstadoPostulacion.PENDIENTE, LocalDateTime.now(), LocalDateTime.now(),
                nombreCompleto, numeroContacto, direccion,
                NivelActividad.valueOf(nivelActividad.toUpperCase()),
                horasSolo, CuidadoVacaciones.valueOf(cuidadoVacaciones.toUpperCase()),
                TipoVivienda.valueOf(tipoVivienda.toUpperCase()), descripcionEspacio,
                tieneNinos, tieneOtrasMascotas
        );
        return solicitudRepository.save(solicitud);
    }

    @Override
    public List<SolicitudAdopcion> listarSolicitudesPorAdoptante(Long adoptanteId) {
        return solicitudRepository.findByAdoptanteId(adoptanteId);
    }

    @Override
    public List<SolicitudAdopcion> listarSolicitudesPorRefugio(Long refugioId) {
        return solicitudRepository.findByRefugioId(refugioId);
    }

    @Override
    public List<SolicitudAdopcion> listarSolicitudesPendientes(int page, int size) {
        return solicitudRepository.findByEstado("PENDIENTE", page, size);
    }

    @Override
    public SolicitudAdopcion obtenerSolicitudPorId(Long solicitudId) {
        return solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada: " + solicitudId));
    }

    @Override
    public SolicitudAdopcion aprobarSolicitud(Long solicitudId) {
        SolicitudAdopcion solicitud = solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada: " + solicitudId));
        if (solicitud.getEstado() != EstadoPostulacion.PENDIENTE) {
            throw new IllegalStateException("La solicitud no está en estado PENDIENTE");
        }
        SolicitudAdopcion actualizada = new SolicitudAdopcion(
                solicitudId, solicitud.getAnimalId(), solicitud.getAdoptanteId(),
                solicitud.getRefugioId(), EstadoPostulacion.APROBADA,
                solicitud.getFechaCreacion(), LocalDateTime.now(),
                solicitud.getNombreCompleto(), solicitud.getNumeroContacto(),
                solicitud.getDireccion(), solicitud.getNivelActividad(),
                solicitud.getHorasSolo(), solicitud.getCuidadoVacaciones(),
                solicitud.getTipoVivienda(), solicitud.getDescripcionEspacio(),
                solicitud.getTieneNinos(), solicitud.getTieneOtrasMascotas()
        );
        Animal animal = animalRepository.findById(solicitud.getAnimalId())
                .orElseThrow(() -> new IllegalArgumentException("Animal no encontrado"));
        animalRepository.save(new Animal(
                animal.getId(), animal.getNombre(), animal.getEspecie(), animal.getRaza(),
                animal.getEdad(), animal.getTamano(), animal.getPersonalidad(),
                animal.getEstadoSalud(), animal.getHistoria(), EstadoAnimal.ADOPTADO,
                animal.getRefugioId(), null, null, animal.getFotos(), animal.getFechaRegistro()
        ));

        List<SolicitudAdopcion> otrasPendientes = solicitudRepository.findByAnimalIdAndEstado(
                solicitud.getAnimalId(), EstadoPostulacion.PENDIENTE.name());
        for (SolicitudAdopcion otra : otrasPendientes) {
            if (!otra.getId().equals(solicitudId)) {
                solicitudRepository.save(new SolicitudAdopcion(
                        otra.getId(), otra.getAnimalId(), otra.getAdoptanteId(),
                        otra.getRefugioId(), EstadoPostulacion.RECHAZADA,
                        otra.getFechaCreacion(), LocalDateTime.now(),
                        otra.getNombreCompleto(), otra.getNumeroContacto(),
                        otra.getDireccion(), otra.getNivelActividad(),
                        otra.getHorasSolo(), otra.getCuidadoVacaciones(),
                        otra.getTipoVivienda(), otra.getDescripcionEspacio(),
                        otra.getTieneNinos(), otra.getTieneOtrasMascotas()
                ));
            }
        }

        SolicitudAdopcion guardada = solicitudRepository.save(actualizada);
        eventPublisher.publicar(new EstadoSolicitudCambiadoEvent(
                guardada.getId(), guardada.getAdoptanteId(), guardada.getAnimalId(),
                guardada.getEstado().name()));
        return guardada;
    }

    @Override
    public SolicitudAdopcion rechazarSolicitud(Long solicitudId) {
        SolicitudAdopcion solicitud = solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada: " + solicitudId));
        if (solicitud.getEstado() != EstadoPostulacion.PENDIENTE) {
            throw new IllegalStateException("La solicitud no está en estado PENDIENTE");
        }
        SolicitudAdopcion actualizada = new SolicitudAdopcion(
                solicitudId, solicitud.getAnimalId(), solicitud.getAdoptanteId(),
                solicitud.getRefugioId(), EstadoPostulacion.RECHAZADA,
                solicitud.getFechaCreacion(), LocalDateTime.now(),
                solicitud.getNombreCompleto(), solicitud.getNumeroContacto(),
                solicitud.getDireccion(), solicitud.getNivelActividad(),
                solicitud.getHorasSolo(), solicitud.getCuidadoVacaciones(),
                solicitud.getTipoVivienda(), solicitud.getDescripcionEspacio(),
                solicitud.getTieneNinos(), solicitud.getTieneOtrasMascotas()
        );
        Animal animal = animalRepository.findById(solicitud.getAnimalId())
                .orElseThrow(() -> new IllegalArgumentException("Animal no encontrado"));
        animalRepository.save(new Animal(
                animal.getId(), animal.getNombre(), animal.getEspecie(), animal.getRaza(),
                animal.getEdad(), animal.getTamano(), animal.getPersonalidad(),
                animal.getEstadoSalud(), animal.getHistoria(), EstadoAnimal.DISPONIBLE,
                animal.getRefugioId(), null, null, animal.getFotos(), animal.getFechaRegistro()
        ));
        SolicitudAdopcion guardada = solicitudRepository.save(actualizada);
        eventPublisher.publicar(new EstadoSolicitudCambiadoEvent(
                guardada.getId(), guardada.getAdoptanteId(), guardada.getAnimalId(),
                guardada.getEstado().name()));
        return guardada;
    }
}
