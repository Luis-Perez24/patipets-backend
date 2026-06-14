package com.patipets.core.application.ports.output;

import com.patipets.core.domain.models.SolicitudAdopcion;
import java.util.List;
import java.util.Optional;

public interface SolicitudAdopcionRepositoryPort {
    SolicitudAdopcion save(SolicitudAdopcion solicitud);
    Optional<SolicitudAdopcion> findById(Long id);
    List<SolicitudAdopcion> findByAdoptanteId(Long adoptanteId);
    List<SolicitudAdopcion> findByRefugioId(Long refugioId);
    List<SolicitudAdopcion> findByAnimalId(Long animalId);
    List<SolicitudAdopcion> findByAnimalIdAndEstado(Long animalId, String estado);
    List<SolicitudAdopcion> findByEstado(String estado, int page, int size);
    long countByRefugioIdAndEstado(Long refugioId, String estado);
    long countByEstado(String estado);
}
