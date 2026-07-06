package com.patipets.core.application.ports.output;

import com.patipets.core.domain.models.Notificacion;
import java.util.List;
import java.util.Optional;

public interface NotificacionRepositoryPort {
    Notificacion save(Notificacion notificacion);
    Optional<Notificacion> findById(Long id);
    List<Notificacion> findByUsuarioId(Long usuarioId, int page, int size);
    long countByUsuarioIdAndLeidaFalse(Long usuarioId);
    int marcarTodasLeidas(Long usuarioId);
}
