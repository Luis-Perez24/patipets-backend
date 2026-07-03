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
    public void procesarAlertaUrgente(Long alertaId, Long refugioId, String titulo, String descripcion, String nivelUrgencia) {
        Refugio refugio = refugioRepository.findById(refugioId).orElse(null);
        String nombreRefugio = refugio != null ? refugio.getNombre() : "Un refugio";
        String mensaje = String.format("%s publicó una alerta urgente (%s): %s", nombreRefugio, nivelUrgencia, titulo);

        Set<Long> destinatarios = new HashSet<>();
        usuarioRepository.findActivosByRol(Rol.VOLUNTARIO).forEach(u -> destinatarios.add(u.getId()));
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

    private void notificar(Long usuarioId, TipoNotificacion tipo, String titulo, String mensaje, Long entidadRelacionadaId) {
        Notificacion notificacion = new Notificacion(
                null, usuarioId, tipo, titulo, mensaje, entidadRelacionadaId, false, LocalDateTime.now()
        );
        notificacionRepository.save(notificacion);
        usuarioRepository.findById(usuarioId).ifPresent(usuario ->
                emailSender.enviar(usuario.getEmail(), titulo, mensaje));
    }
}
