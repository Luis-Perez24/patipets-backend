package com.patipets.core.application.ports.output;

import com.patipets.core.domain.enums.EstadoRefugio;
import com.patipets.core.domain.models.Refugio;
import java.util.List;
import java.util.Optional;

public interface RefugioRepositoryPort {
    List<Refugio> findAllAprobados();
    List<Refugio> findByEstado(EstadoRefugio estado);
    Optional<Refugio> findById(Long id);
    Refugio save(Refugio refugio);
    long countRegistrados();
    long countByEstado(EstadoRefugio estado);
    void vincularUsuario(Long usuarioId, Long refugioId);
    boolean perteneceAUsuario(Long refugioId, Long usuarioId);
}
