package com.patipets.core.application.useCase;

import com.patipets.core.domain.models.Notificacion;
import java.util.List;

public interface GestionNotificacionUseCase {
    List<Notificacion> listarPorUsuario(Long usuarioId, int page, int size);
    long contarNoLeidas(Long usuarioId);
    Notificacion marcarLeida(Long id, Long usuarioId);
    int marcarTodasLeidas(Long usuarioId);
    void procesarAlertaUrgente(Long alertaId, Long refugioId, String titulo, String descripcion, String nivelUrgencia);
    void procesarCambioEstadoSolicitud(Long solicitudId, Long adoptanteId, Long animalId, String nuevoEstado);
    void procesarCambioEstadoRefugio(Long refugioId, Long usuarioId, String nombreRefugio, String nuevoEstado);
    void procesarCancelacionInscripcionVoluntariado(Long inscripcionId, Long voluntarioId,
                                                    Long alertaId, String alertaTitulo, String motivo);
    void procesarAceptacionInscripcionVoluntariado(Long inscripcionId, Long voluntarioId,
                                                   Long alertaId, String alertaTitulo);
    void procesarRechazoInscripcionVoluntariado(Long inscripcionId, Long voluntarioId,
                                                Long alertaId, String alertaTitulo, String motivo);
    void procesarCancelacionApadrinamientoPorRefugio(Long apadrinamientoId, Long padrinoId,
                                                       Long animalId, String animalNombre, String motivo);
    void procesarCancelacionApadrinamientoPorPadrino(Long apadrinamientoId, Long refugioId, String motivo);
}
