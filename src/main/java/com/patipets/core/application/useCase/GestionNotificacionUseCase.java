package com.patipets.core.application.useCase;

import com.patipets.core.domain.models.Notificacion;
import java.util.List;

public interface GestionNotificacionUseCase {
    List<Notificacion> listarPorUsuario(Long usuarioId, int page, int size);
    long contarNoLeidas(Long usuarioId);
    Notificacion marcarLeida(Long id, Long usuarioId);
    void procesarAlertaUrgente(Long alertaId, Long refugioId, String titulo, String descripcion, String nivelUrgencia);
    void procesarCambioEstadoSolicitud(Long solicitudId, Long adoptanteId, Long animalId, String nuevoEstado);
    void procesarCambioEstadoRefugio(Long refugioId, Long usuarioId, String nombreRefugio, String nuevoEstado);
}
