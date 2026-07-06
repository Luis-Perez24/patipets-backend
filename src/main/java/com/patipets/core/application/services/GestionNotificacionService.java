package com.patipets.core.application.services;

import com.patipets.core.application.ports.output.AnimalRepositoryPort;
import com.patipets.core.application.ports.output.EmailSenderPort;
import com.patipets.core.application.ports.output.NotificacionRepositoryPort;
import com.patipets.core.application.ports.output.RefugioRepositoryPort;
import com.patipets.core.application.ports.output.SolicitudAdopcionRepositoryPort;
import com.patipets.core.application.ports.output.UsuarioRepositoryPort;
import com.patipets.core.application.useCase.GestionNotificacionUseCase;
import com.patipets.core.domain.enums.Rol;
import com.patipets.core.domain.enums.TipoNotificacion;
import com.patipets.core.domain.models.Animal;
import com.patipets.core.domain.models.Notificacion;
import com.patipets.core.domain.models.Refugio;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GestionNotificacionService implements GestionNotificacionUseCase {

    private final NotificacionRepositoryPort notificacionRepository;
    private final UsuarioRepositoryPort usuarioRepository;
    private final SolicitudAdopcionRepositoryPort solicitudRepository;
    private final AnimalRepositoryPort animalRepository;
    private final RefugioRepositoryPort refugioRepository;
    private final EmailSenderPort emailSender;

    public GestionNotificacionService(NotificacionRepositoryPort notificacionRepository,
                                       UsuarioRepositoryPort usuarioRepository,
                                       SolicitudAdopcionRepositoryPort solicitudRepository,
                                       AnimalRepositoryPort animalRepository,
                                       RefugioRepositoryPort refugioRepository,
                                       EmailSenderPort emailSender) {
        this.notificacionRepository = notificacionRepository;
        this.usuarioRepository = usuarioRepository;
        this.solicitudRepository = solicitudRepository;
        this.animalRepository = animalRepository;
        this.refugioRepository = refugioRepository;
        this.emailSender = emailSender;
    }

    @Override
    public List<Notificacion> listarPorUsuario(Long usuarioId, int page, int size) {
        return notificacionRepository.findByUsuarioId(usuarioId, page, size);
    }

    @Override
    public long contarNoLeidas(Long usuarioId) {
        return notificacionRepository.countByUsuarioIdAndLeidaFalse(usuarioId);
    }

    @Override
    public Notificacion marcarLeida(Long id, Long usuarioId) {
        Notificacion notificacion = notificacionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notificación no encontrada: " + id));
        if (!notificacion.getUsuarioId().equals(usuarioId)) {
            throw new IllegalArgumentException("Notificación no encontrada: " + id);
        }
        Notificacion leida = new Notificacion(
                notificacion.getId(), notificacion.getUsuarioId(), notificacion.getTipo(),
                notificacion.getTitulo(), notificacion.getMensaje(), notificacion.getEntidadRelacionadaId(),
                true, notificacion.getFechaCreacion()
        );
        return notificacionRepository.save(leida);
    }

    @Override
    public int marcarTodasLeidas(Long usuarioId) {
        return notificacionRepository.marcarTodasLeidas(usuarioId);
    }

    @Override
    public void procesarAlertaUrgente(Long alertaId, Long refugioId, String titulo, String descripcion, String nivelUrgencia) {
        Refugio refugio = refugioRepository.findById(refugioId).orElse(null);
        String nombreRefugio = refugio != null ? refugio.getNombre() : "Un refugio";
        String mensaje = String.format("%s publicó una alerta urgente (%s): %s", nombreRefugio, nivelUrgencia, titulo);

        Set<Long> destinatarios = new HashSet<>();
        usuarioRepository.findActivosByRol(Rol.VOLUNTARIO).forEach(u -> destinatarios.add(u.getId()));
        usuarioRepository.findActivosByRol(Rol.PADRINO).forEach(u -> destinatarios.add(u.getId()));
        solicitudRepository.findByRefugioId(refugioId).forEach(s -> destinatarios.add(s.getAdoptanteId()));

        for (Long usuarioId : destinatarios) {
            notificar(usuarioId, TipoNotificacion.ALERTA_URGENTE, titulo, mensaje, alertaId);
        }
    }

    @Override
    public void procesarCambioEstadoSolicitud(Long solicitudId, Long adoptanteId, Long animalId, String nuevoEstado) {
        Animal animal = animalRepository.findById(animalId).orElse(null);
        String nombreAnimal = animal != null ? animal.getNombre() : "el animal";
        String titulo;
        String mensaje;
        switch (nuevoEstado) {
            case "APROBADA" -> {
                titulo = "Tu solicitud de adopción fue aprobada";
                mensaje = String.format(
                        "Tu solicitud para adoptar a %s fue aprobada. El refugio está coordinando la entrega.",
                        nombreAnimal);
            }
            case "RECHAZADA" -> {
                titulo = "Tu solicitud de adopción fue rechazada";
                mensaje = String.format("Tu solicitud para adoptar a %s fue rechazada.", nombreAnimal);
            }
            case "COMPLETADA" -> {
                titulo = "¡Adopción confirmada!";
                mensaje = String.format("La adopción de %s fue confirmada. ¡Felicidades!", nombreAnimal);
            }
            default -> {
                titulo = "Actualización de tu solicitud de adopción";
                mensaje = String.format("Tu solicitud de adopción para %s cambió a estado: %s", nombreAnimal, nuevoEstado);
            }
        }
        notificar(adoptanteId, TipoNotificacion.CAMBIO_ESTADO_SOLICITUD, titulo, mensaje, solicitudId);
    }

    @Override
    public void procesarCambioEstadoRefugio(Long refugioId, Long usuarioId, String nombreRefugio, String nuevoEstado) {
        String titulo;
        String mensaje;
        switch (nuevoEstado) {
            case "APROBADO" -> {
                titulo = "Tu solicitud de refugio fue aprobada";
                mensaje = String.format(
                        "¡Felicidades! Tu refugio \"%s\" fue aprobado. Ya puedes administrarlo desde tu perfil (vuelve a iniciar sesión para ver los permisos actualizados).",
                        nombreRefugio);
            }
            case "RECHAZADO" -> {
                titulo = "Tu solicitud de refugio fue rechazada";
                mensaje = String.format("Tu solicitud para registrar el refugio \"%s\" fue rechazada.", nombreRefugio);
            }
            default -> {
                titulo = "Actualización de tu solicitud de refugio";
                mensaje = String.format("Tu solicitud de refugio \"%s\" cambió a estado: %s", nombreRefugio, nuevoEstado);
            }
        }
        notificar(usuarioId, TipoNotificacion.CAMBIO_ESTADO_REFUGIO, titulo, mensaje, refugioId);
    }

    @Override
    public void procesarCancelacionInscripcionVoluntariado(Long inscripcionId, Long voluntarioId,
                                                          Long alertaId, String alertaTitulo, String motivo) {
        String titulo = "Inscripción a convocatoria cancelada";
        String mensaje = String.format(
                "El refugio canceló tu inscripción a la convocatoria \"%s\".%s",
                alertaTitulo,
                (motivo != null && !motivo.isBlank()) ? " Motivo: " + motivo : "");
        notificar(voluntarioId, TipoNotificacion.INSCRIPCION_VOLUNTARIADO_CANCELADA, titulo, mensaje, inscripcionId);
    }

    @Override
    public void procesarAceptacionInscripcionVoluntariado(Long inscripcionId, Long voluntarioId,
                                                            Long alertaId, String alertaTitulo) {
        String titulo = "Inscripción aceptada";
        String mensaje = String.format("El refugio aceptó tu inscripción a la convocatoria \"%s\".", alertaTitulo);
        notificar(voluntarioId, TipoNotificacion.INSCRIPCION_VOLUNTARIADO_ACEPTADA, titulo, mensaje, inscripcionId);
    }

    @Override
    public void procesarRechazoInscripcionVoluntariado(Long inscripcionId, Long voluntarioId,
                                                         Long alertaId, String alertaTitulo, String motivo) {
        String titulo = "Inscripción rechazada";
        String mensaje = String.format(
                "El refugio rechazó tu inscripción a la convocatoria \"%s\".%s",
                alertaTitulo,
                (motivo != null && !motivo.isBlank()) ? " Motivo: " + motivo : "");
        notificar(voluntarioId, TipoNotificacion.INSCRIPCION_VOLUNTARIADO_RECHAZADA, titulo, mensaje, inscripcionId);
    }

    @Override
    public void procesarCancelacionApadrinamientoPorRefugio(Long apadrinamientoId, Long padrinoId,
                                                              Long animalId, String animalNombre, String motivo) {
        String titulo = "Apadrinamiento cancelado por el refugio";
        String mensaje = String.format(
                "El refugio canceló tu apadrinamiento a %s.%s",
                animalNombre,
                (motivo != null && !motivo.isBlank()) ? " Motivo: " + motivo : "");
        notificar(padrinoId, TipoNotificacion.APADRINAMIENTO_CANCELADO_POR_REFUGIO, titulo, mensaje, apadrinamientoId);
    }

    @Override
    public void procesarCancelacionApadrinamientoPorPadrino(Long apadrinamientoId, Long refugioId, String motivo) {
        String titulo = "Apadrinamiento cancelado por el padrino";
        String sufijo = (motivo != null && !motivo.isBlank()) ? " Motivo: " + motivo : "";
        String mensaje = "Un padrino canceló su apadrinamiento." + sufijo;
        notificar(refugioId, TipoNotificacion.APADRINAMIENTO_CANCELADO_POR_PADRINO, titulo, mensaje, apadrinamientoId);
    }

    private void notificar(Long usuarioId, TipoNotificacion tipo, String titulo, String mensaje, Long entidadRelacionadaId) {
        Notificacion notificacion = new Notificacion(
                null, usuarioId, tipo, titulo, mensaje, entidadRelacionadaId, false, LocalDateTime.now()
        );
        notificacionRepository.save(notificacion);
        usuarioRepository.findById(usuarioId).ifPresent(usuario ->
                emailSender.enviar(usuario.getEmail(), titulo, mensaje));
    }
}
