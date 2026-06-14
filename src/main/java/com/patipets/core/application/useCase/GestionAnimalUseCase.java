package com.patipets.core.application.useCase;

import com.patipets.core.domain.models.Animal;
import com.patipets.core.domain.models.SolicitudAdopcion;
import java.util.List;

public interface GestionAnimalUseCase {
    Animal crearAnimal(String nombre, String especie, String raza, Integer edad,
                       String tamano, String estadoSalud, String historia,
                       Long refugioId, List<String> fotos);
    Animal actualizarAnimal(Long id, String nombre, String especie, String raza,
                            Integer edad, String tamano, String estadoSalud,
                            String historia, String estadoAdopcion, List<String> fotos);
    void eliminarAnimal(Long id);
    List<Animal> listarPorRefugio(Long refugioId);
    SolicitudAdopcion solicitar(Long animalId, Long adoptanteId, String nombreCompleto,
                                String numeroContacto, String direccion, String nivelActividad,
                                Integer horasSolo, String cuidadoVacaciones, String tipoVivienda,
                                String descripcionEspacio, Boolean tieneNinos, Boolean tieneOtrasMascotas);
    List<SolicitudAdopcion> listarSolicitudesPorAdoptante(Long adoptanteId);
    List<SolicitudAdopcion> listarSolicitudesPorRefugio(Long refugioId);
    List<SolicitudAdopcion> listarSolicitudesPendientes(int page, int size);
    SolicitudAdopcion aprobarSolicitud(Long solicitudId);
    SolicitudAdopcion rechazarSolicitud(Long solicitudId);
}
