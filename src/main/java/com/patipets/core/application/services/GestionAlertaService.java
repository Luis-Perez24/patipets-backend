package com.patipets.core.application.services;

import com.patipets.core.application.events.AlertaUrgentePublicadaEvent;
import com.patipets.core.application.events.InscripcionVoluntariadoAceptadaEvent;
import com.patipets.core.application.events.InscripcionVoluntariadoCanceladaEvent;
import com.patipets.core.application.events.InscripcionVoluntariadoRechazadaEvent;
import com.patipets.core.application.ports.output.AlertaRepositoryPort;
import com.patipets.core.application.ports.output.EventPublisherPort;
import com.patipets.core.application.ports.output.RespuestaAlertaRepositoryPort;
import com.patipets.core.application.useCase.GestionAlertaUseCase;
import com.patipets.core.domain.enums.NivelUrgencia;
import com.patipets.core.domain.enums.TipoAyudaVoluntariado;
import com.patipets.core.domain.models.Alerta;
import com.patipets.core.domain.models.RespuestaAlerta;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class GestionAlertaService implements GestionAlertaUseCase {

    private final AlertaRepositoryPort alertaRepository;
    private final EventPublisherPort eventPublisher;
    private final RespuestaAlertaRepositoryPort respuestaRepository;

    public GestionAlertaService(AlertaRepositoryPort alertaRepository,
                                 EventPublisherPort eventPublisher,
                                 RespuestaAlertaRepositoryPort respuestaRepository) {
        this.alertaRepository = alertaRepository;
        this.eventPublisher = eventPublisher;
        this.respuestaRepository = respuestaRepository;
    }

    @Override
    public Alerta crear(String titulo, String descripcion, String nivelUrgencia,
                        Long refugioId, Long creadoPor) {
        return crear(titulo, descripcion, nivelUrgencia, refugioId, creadoPor, null, null, null);
    }

    @Override
    public Alerta crear(String titulo, String descripcion, String nivelUrgencia,
                        Long refugioId, Long creadoPor,
                        String tipoAyuda, String fecha, String perfilRequerido) {
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("El título es obligatorio");
        }
        NivelUrgencia nivel;
        try {
            nivel = NivelUrgencia.valueOf(nivelUrgencia.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Nivel de urgencia inválido: " + nivelUrgencia);
        }
        TipoAyudaVoluntariado tipo = null;
        if (tipoAyuda != null && !tipoAyuda.isBlank()) {
            try {
                tipo = TipoAyudaVoluntariado.valueOf(tipoAyuda.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Tipo de ayuda inválido: " + tipoAyuda);
            }
        }
        LocalDate fechaParsed = null;
        if (fecha != null && !fecha.isBlank()) {
            fechaParsed = LocalDate.parse(fecha);
        }
        Alerta alerta = new Alerta(
                null, titulo, descripcion, nivel, refugioId, creadoPor, true, LocalDateTime.now(),
                tipo, fechaParsed, perfilRequerido
        );
        Alerta creada = alertaRepository.save(alerta);
        eventPublisher.publicar(new AlertaUrgentePublicadaEvent(
                creada.getId(), creada.getRefugioId(), creada.getTitulo(),
                creada.getDescripcion(), creada.getNivelUrgencia().name()));
        return creada;
    }

    @Override
    public List<Alerta> listarPorRefugio(Long refugioId) {
        return listarPorRefugio(refugioId, null);
    }

    @Override
    public List<Alerta> listarPorRefugio(Long refugioId, String tipo) {
        List<Alerta> todas = alertaRepository.findByRefugioId(refugioId);
        if (tipo == null || "todas".equalsIgnoreCase(tipo)) {
            return todas;
        }
        if ("convocatoria".equalsIgnoreCase(tipo)) {
            return todas.stream()
                    .filter(a -> a.getTipoAyuda() != null)
                    .collect(Collectors.toList());
        }
        if ("alerta".equalsIgnoreCase(tipo)) {
            return todas.stream()
                    .filter(a -> a.getTipoAyuda() == null)
                    .collect(Collectors.toList());
        }
        return todas;
    }

    @Override
    public List<Alerta> listarActivas(int page, int size) {
        return alertaRepository.findActivas(page, size);
    }

    @Override
    public Alerta marcarResuelta(Long id) {
        Alerta alerta = alertaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Alerta no encontrada: " + id));
        if (!alerta.isActiva()) {
            throw new IllegalStateException("La alerta ya está resuelta");
        }
        Alerta resuelta = new Alerta(
                id, alerta.getTitulo(), alerta.getDescripcion(), alerta.getNivelUrgencia(),
                alerta.getRefugioId(), alerta.getCreadoPor(), false, alerta.getCreatedAt(),
                alerta.getTipoAyuda(), alerta.getFecha(), alerta.getPerfilRequerido()
        );
        return alertaRepository.save(resuelta);
    }

    @Override
    public void eliminar(Long id) {
        if (alertaRepository.findById(id).isEmpty()) {
            throw new IllegalArgumentException("Alerta no encontrada: " + id);
        }
        alertaRepository.deleteById(id);
    }

    @Override
    public RespuestaAlerta responder(Long alertaId, Long usuarioId, String tipoAyuda, String mensaje) {
        return responder(alertaId, usuarioId, tipoAyuda, mensaje, null);
    }

    @Override
    public RespuestaAlerta responder(Long alertaId, Long usuarioId, String tipoAyuda, String mensaje, String disponibilidad) {
        Alerta alerta = alertaRepository.findById(alertaId)
                .orElseThrow(() -> new IllegalArgumentException("Alerta no encontrada: " + alertaId));
        if (!alerta.isActiva()) {
            throw new IllegalStateException("No puedes responder a una alerta resuelta");
        }
        if (respuestaRepository.existsByAlertaIdAndUsuarioId(alertaId, usuarioId)) {
            throw new IllegalArgumentException("Ya has respondido a esta alerta");
        }
        TipoAyudaVoluntariado tipo;
        try {
            tipo = TipoAyudaVoluntariado.valueOf(tipoAyuda.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Tipo de ayuda inválido: " + tipoAyuda);
        }
        RespuestaAlerta respuesta = new RespuestaAlerta(
                null, alertaId, usuarioId, tipo, mensaje, disponibilidad, LocalDateTime.now()
        );
        return respuestaRepository.save(respuesta);
    }

    @Override
    public List<RespuestaAlerta> listarRespuestasPorUsuario(Long usuarioId) {
        return respuestaRepository.findByUsuarioId(usuarioId);
    }

    @Override
    public List<RespuestaAlerta> listarRespuestasPorAlerta(Long alertaId) {
        return respuestaRepository.findByAlertaId(alertaId);
    }

    @Override
    public List<RespuestaAlerta> listarRespuestasPorRefugio(Long refugioId) {
        return respuestaRepository.findByRefugioId(refugioId);
    }

    @Override
    public RespuestaAlerta cancelarRespuestaPorRefugio(Long respuestaId, Long refugioId, String motivo) {
        RespuestaAlerta existente = respuestaRepository.findById(respuestaId)
                .orElseThrow(() -> new IllegalArgumentException("Inscripción no encontrada: " + respuestaId));
        Alerta alerta = alertaRepository.findById(existente.getAlertaId())
                .orElseThrow(() -> new IllegalArgumentException("Alerta no encontrada: " + existente.getAlertaId()));
        if (!alerta.getRefugioId().equals(refugioId)) {
            throw new IllegalArgumentException("La inscripción no pertenece a este refugio");
        }
        if ("CANCELADA".equals(existente.getEstado())) {
            throw new IllegalStateException("La inscripción ya está cancelada");
        }
        RespuestaAlerta cancelada = new RespuestaAlerta(
                existente.getId(), existente.getAlertaId(), existente.getUsuarioId(),
                existente.getTipoAyuda(), existente.getMensaje(), existente.getDisponibilidad(),
                existente.getCreatedAt(),
                "CANCELADA", java.time.LocalDateTime.now(), motivo
        );
        RespuestaAlerta guardada = respuestaRepository.save(cancelada);
        eventPublisher.publicar(new InscripcionVoluntariadoCanceladaEvent(
                guardada.getId(), guardada.getUsuarioId(), guardada.getAlertaId(),
                alerta.getTitulo(), motivo));
        return guardada;
    }

    @Override
    public RespuestaAlerta aceptarRespuesta(Long respuestaId, Long refugioId) {
        return cambiarEstadoRespuesta(respuestaId, refugioId, "ACEPTADA", null, InscripcionVoluntariadoAceptadaEvent.class);
    }

    @Override
    public RespuestaAlerta rechazarRespuesta(Long respuestaId, Long refugioId, String motivo) {
        return cambiarEstadoRespuesta(respuestaId, refugioId, "RECHAZADA", motivo, InscripcionVoluntariadoRechazadaEvent.class);
    }

    @Override
    public RespuestaAlerta cancelarRespuestaPorVoluntario(Long respuestaId, Long usuarioId, String motivo) {
        RespuestaAlerta existente = respuestaRepository.findById(respuestaId)
                .orElseThrow(() -> new IllegalArgumentException("Inscripción no encontrada: " + respuestaId));
        if (!existente.getUsuarioId().equals(usuarioId)) {
            throw new IllegalArgumentException("No puedes cancelar una inscripción que no te pertenece");
        }
        if ("CANCELADA".equals(existente.getEstado())) {
            throw new IllegalStateException("La inscripción ya está cancelada");
        }
        Alerta alerta = alertaRepository.findById(existente.getAlertaId())
                .orElseThrow(() -> new IllegalArgumentException("Alerta no encontrada: " + existente.getAlertaId()));
        RespuestaAlerta cancelada = new RespuestaAlerta(
                existente.getId(), existente.getAlertaId(), existente.getUsuarioId(),
                existente.getTipoAyuda(), existente.getMensaje(), existente.getDisponibilidad(),
                existente.getCreatedAt(),
                "CANCELADA", java.time.LocalDateTime.now(),
                motivo != null ? motivo : "Cancelada por el voluntario"
        );
        RespuestaAlerta guardada = respuestaRepository.save(cancelada);
        eventPublisher.publicar(new InscripcionVoluntariadoCanceladaEvent(
                guardada.getId(), guardada.getUsuarioId(), guardada.getAlertaId(),
                alerta.getTitulo(), motivo));
        return guardada;
    }

    private RespuestaAlerta cambiarEstadoRespuesta(Long respuestaId, Long refugioId, String nuevoEstado,
                                                   String motivo, Class<?> eventClass) {
        RespuestaAlerta existente = respuestaRepository.findById(respuestaId)
                .orElseThrow(() -> new IllegalArgumentException("Inscripción no encontrada: " + respuestaId));
        Alerta alerta = alertaRepository.findById(existente.getAlertaId())
                .orElseThrow(() -> new IllegalArgumentException("Alerta no encontrada: " + existente.getAlertaId()));
        if (!alerta.getRefugioId().equals(refugioId)) {
            throw new IllegalArgumentException("La inscripción no pertenece a este refugio");
        }
        if ("CANCELADA".equals(existente.getEstado()) || "RECHAZADA".equals(existente.getEstado())) {
            throw new IllegalStateException("La inscripción ya fue " + existente.getEstado().toLowerCase());
        }
        if ("ACEPTADA".equals(nuevoEstado) && "ACEPTADA".equals(existente.getEstado())) {
            throw new IllegalStateException("La inscripción ya fue aceptada");
        }
        RespuestaAlerta actualizada = new RespuestaAlerta(
                existente.getId(), existente.getAlertaId(), existente.getUsuarioId(),
                existente.getTipoAyuda(), existente.getMensaje(), existente.getDisponibilidad(),
                existente.getCreatedAt(),
                nuevoEstado, motivo != null ? java.time.LocalDateTime.now() : existente.getFechaCancelacion(),
                motivo
        );
        RespuestaAlerta guardada = respuestaRepository.save(actualizada);
        if (eventClass == InscripcionVoluntariadoAceptadaEvent.class) {
            eventPublisher.publicar(new InscripcionVoluntariadoAceptadaEvent(
                    guardada.getId(), guardada.getUsuarioId(), guardada.getAlertaId(), alerta.getTitulo()));
        } else if (eventClass == InscripcionVoluntariadoRechazadaEvent.class) {
            eventPublisher.publicar(new InscripcionVoluntariadoRechazadaEvent(
                    guardada.getId(), guardada.getUsuarioId(), guardada.getAlertaId(),
                    alerta.getTitulo(), motivo));
        }
        return guardada;
    }
}
