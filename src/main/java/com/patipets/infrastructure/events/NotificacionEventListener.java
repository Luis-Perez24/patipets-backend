package com.patipets.infrastructure.events;

import com.patipets.core.application.events.AlertaUrgentePublicadaEvent;
import com.patipets.core.application.events.ApadrinamientoCanceladoPorPadrinoEvent;
import com.patipets.core.application.events.ApadrinamientoCanceladoPorRefugioEvent;
import com.patipets.core.application.events.EstadoSolicitudCambiadoEvent;
import com.patipets.core.application.events.InscripcionVoluntariadoAceptadaEvent;
import com.patipets.core.application.events.InscripcionVoluntariadoCanceladaEvent;
import com.patipets.core.application.events.InscripcionVoluntariadoRechazadaEvent;
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

    @Async
    @EventListener
    public void onInscripcionVoluntariadoCancelada(InscripcionVoluntariadoCanceladaEvent evento) {
        gestionNotificacionUseCase.procesarCancelacionInscripcionVoluntariado(
                evento.getInscripcionId(), evento.getVoluntarioId(), evento.getAlertaId(),
                evento.getAlertaTitulo(), evento.getMotivo());
    }

    @Async
    @EventListener
    public void onInscripcionVoluntariadoAceptada(InscripcionVoluntariadoAceptadaEvent evento) {
        gestionNotificacionUseCase.procesarAceptacionInscripcionVoluntariado(
                evento.getInscripcionId(), evento.getVoluntarioId(), evento.getAlertaId(),
                evento.getAlertaTitulo());
    }

    @Async
    @EventListener
    public void onInscripcionVoluntariadoRechazada(InscripcionVoluntariadoRechazadaEvent evento) {
        gestionNotificacionUseCase.procesarRechazoInscripcionVoluntariado(
                evento.getInscripcionId(), evento.getVoluntarioId(), evento.getAlertaId(),
                evento.getAlertaTitulo(), evento.getMotivo());
    }

    @Async
    @EventListener
    public void onApadrinamientoCanceladoPorRefugio(ApadrinamientoCanceladoPorRefugioEvent evento) {
        gestionNotificacionUseCase.procesarCancelacionApadrinamientoPorRefugio(
                evento.getApadrinamientoId(), evento.getPadrinoId(), evento.getAnimalId(),
                evento.getAnimalNombre(), evento.getMotivo());
    }

    @Async
    @EventListener
    public void onApadrinamientoCanceladoPorPadrino(ApadrinamientoCanceladoPorPadrinoEvent evento) {
        gestionNotificacionUseCase.procesarCancelacionApadrinamientoPorPadrino(
                evento.getApadrinamientoId(), evento.getRefugioId(), evento.getMotivo());
    }
}
