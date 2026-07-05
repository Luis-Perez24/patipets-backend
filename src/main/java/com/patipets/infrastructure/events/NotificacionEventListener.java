package com.patipets.infrastructure.events;

import com.patipets.core.application.events.AlertaUrgentePublicadaEvent;
import com.patipets.core.application.events.EstadoSolicitudCambiadoEvent;
import com.patipets.core.application.events.SolicitudRefugioCambiadaEvent;
import com.patipets.core.application.useCase.GestionNotificacionUseCase;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class NotificacionEventListener {

    private final GestionNotificacionUseCase gestionNotificacionUseCase;

    public NotificacionEventListener(GestionNotificacionUseCase gestionNotificacionUseCase) {
        this.gestionNotificacionUseCase = gestionNotificacionUseCase;
    }

    @Async
    @EventListener
    public void onAlertaUrgentePublicada(AlertaUrgentePublicadaEvent evento) {
        gestionNotificacionUseCase.procesarAlertaUrgente(
                evento.getAlertaId(), evento.getRefugioId(), evento.getTitulo(),
                evento.getDescripcion(), evento.getNivelUrgencia());
    }

    @Async
    @EventListener
    public void onEstadoSolicitudCambiado(EstadoSolicitudCambiadoEvent evento) {
        gestionNotificacionUseCase.procesarCambioEstadoSolicitud(
                evento.getSolicitudId(), evento.getAdoptanteId(), evento.getAnimalId(), evento.getNuevoEstado());
    }

    @Async
    @EventListener
    public void onSolicitudRefugioCambiada(SolicitudRefugioCambiadaEvent evento) {
        gestionNotificacionUseCase.procesarCambioEstadoRefugio(
                evento.getRefugioId(), evento.getUsuarioId(), evento.getNombreRefugio(), evento.getNuevoEstado());
    }
}
