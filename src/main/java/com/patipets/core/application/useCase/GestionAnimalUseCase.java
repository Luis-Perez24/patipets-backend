package com.patipets.core.application.useCase;

import com.patipets.core.domain.models.Animal;
import com.patipets.core.domain.models.SolicitudAdopcion;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

public interface GestionAnimalUseCase {
    Animal crearAnimal(String nombre, String especie, String raza, Integer edad,
                       String tamano, String personalidad, String estadoSalud, String historia,
                       Long refugioId, List<MultipartFile> archivos) throws IOException;
    Animal actualizarAnimal(Long id, Long refugioId, String nombre, String especie, String raza,
                            Integer edad, String tamano, String personalidad,
                            String estadoSalud, String historia, String estadoAdopcion,
                            List<String> fotosAMantener, List<MultipartFile> archivosNuevos) throws IOException;
    void eliminarAnimal(Long id, Long refugioId);
    List<Animal> listarPorRefugio(Long refugioId);
    SolicitudAdopcion solicitar(Long animalId, Long adoptanteId, String nombreCompleto,
                                String numeroContacto, String direccion, String nivelActividad,
                                Integer horasSolo, String cuidadoVacaciones, String tipoVivienda,
                                String descripcionEspacio, Boolean tieneNinos, Boolean tieneOtrasMascotas);
    List<SolicitudAdopcion> listarSolicitudesPorAdoptante(Long adoptanteId);
    List<SolicitudAdopcion> listarSolicitudesPorRefugio(Long refugioId);
    List<SolicitudAdopcion> listarSolicitudesPendientes(int page, int size);
    SolicitudAdopcion obtenerSolicitudPorId(Long solicitudId);
    SolicitudAdopcion aprobarSolicitud(Long solicitudId);
    SolicitudAdopcion rechazarSolicitud(Long solicitudId);
    SolicitudAdopcion confirmarAdopcion(Long solicitudId);
}
