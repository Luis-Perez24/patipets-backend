package com.patipets.core.application.ports.output;

import com.patipets.core.domain.models.Alerta;
import java.util.List;
import java.util.Optional;

public interface AlertaRepositoryPort {
    Alerta save(Alerta alerta);
    Optional<Alerta> findById(Long id);
    List<Alerta> findByRefugioId(Long refugioId);
    List<Alerta> findActivas(int page, int size);
    void deleteById(Long id);
}
