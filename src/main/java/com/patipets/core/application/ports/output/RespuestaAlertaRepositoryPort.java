package com.patipets.core.application.ports.output;

import com.patipets.core.domain.models.RespuestaAlerta;
import java.util.List;
import java.util.Optional;

public interface RespuestaAlertaRepositoryPort {
    RespuestaAlerta save(RespuestaAlerta respuesta);
    Optional<RespuestaAlerta> findById(Long id);
    List<RespuestaAlerta> findByAlertaId(Long alertaId);
    List<RespuestaAlerta> findByUsuarioId(Long usuarioId);
    List<RespuestaAlerta> findByRefugioId(Long refugioId);
    boolean existsByAlertaIdAndUsuarioId(Long alertaId, Long usuarioId);
}
