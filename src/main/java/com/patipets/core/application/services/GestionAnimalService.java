package com.patipets.core.application.services;

import com.patipets.core.application.ports.output.AnimalRepositoryPort;
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
import java.time.LocalDateTime;
import java.util.List;

public class GestionAnimalService implements GestionAnimalUseCase {

    private final AnimalRepositoryPort animalRepository;
    private final SolicitudAdopcionRepositoryPort solicitudRepository;
    private final RefugioRepositoryPort refugioRepository;

    public GestionAnimalService(AnimalRepositoryPort animalRepository,
                                 SolicitudAdopcionRepositoryPort solicitudRepository,
                                 RefugioRepositoryPort refugioRepository) {
        this.animalRepository = animalRepository;
        this.solicitudRepository = solicitudRepository;
        this.refugioRepository = refugioRepository;
    }

    @Override
    public Animal crearAnimal(String nombre, String especie, String raza, Integer edad,
                               String tamano, String estadoSalud, String historia,
                               Long refugioId, List<String> fotos) {
        Refugio refugio = refugioRepository.findById(refugioId)
                .orElseThrow(() -> new IllegalArgumentException("Refugio no encontrado: " + refugioId));
        if (refugio.getEstado() != EstadoRefugio.APROBADO) {
            throw new IllegalArgumentException("No se pueden agregar animales a un refugio que no está aprobado");
        }
        Animal animal = new Animal(
                null, nombre, especie, raza, edad, tamano, estadoSalud, historia,
                EstadoAnimal.DISPONIBLE, refugioId, null, null, fotos, LocalDateTime.now()
        );
        return animalRepository.save(animal);
    }

    @Override
    public Animal actualizarAnimal(Long id, String nombre, String especie, String raza,
                                    Integer edad, String tamano, String estadoSalud,
                                    String historia, String estadoAdopcion, List<String> fotos) {
        Animal existente = animalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Animal no encontrado: " + id));
        EstadoAnimal nuevoEstado = estadoAdopcion != null
                ? EstadoAnimal.valueOf(estadoAdopcion.toUpperCase())
                : existente.getEstadoAdopcion();
        Animal actualizado = new Animal(
                id, nombre, especie, raza, edad, tamano, estadoSalud, historia,
                nuevoEstado, existente.getRefugioId(), null, null, fotos, existente.getFechaRegistro()
        );
        return animalRepository.save(actualizado);
    }

    @Override
    public void eliminarAnimal(Long id) {
        if (animalRepository.findById(id).isEmpty()) {
            throw new IllegalArgumentException("Animal no encontrado: " + id);
        }
        animalRepository.deleteById(id);
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
                animal.getEdad(), animal.getTamano(), animal.getEstadoSalud(),
                animal.getHistoria(), EstadoAnimal.ADOPTADO, animal.getRefugioId(),
                null, null, animal.getFotos(), animal.getFechaRegistro()
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

        return solicitudRepository.save(actualizada);
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
                animal.getEdad(), animal.getTamano(), animal.getEstadoSalud(),
                animal.getHistoria(), EstadoAnimal.DISPONIBLE, animal.getRefugioId(),
                null, null, animal.getFotos(), animal.getFechaRegistro()
        ));
        return solicitudRepository.save(actualizada);
    }
}
