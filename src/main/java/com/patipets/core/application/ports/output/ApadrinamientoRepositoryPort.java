package com.patipets.core.application.ports.output;

import com.patipets.core.domain.models.Apadrinamiento;
import java.util.List;
import java.util.Optional;

public interface ApadrinamientoRepositoryPort {
    Apadrinamiento save(Apadrinamiento apadrinamiento);
    Optional<Apadrinamiento> findById(Long id);
    List<Apadrinamiento> findByPadrinoId(Long padrinoId);
    List<Apadrinamiento> findByAnimalId(Long animalId);
    List<Apadrinamiento> findByRefugioId(Long refugioId);
    boolean existsByPadrinoIdAndAnimalIdAndActivoTrue(Long padrinoId, Long animalId);
}
