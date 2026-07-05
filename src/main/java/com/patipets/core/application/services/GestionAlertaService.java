package com.patipets.core.application.services;

import com.patipets.core.application.events.AlertaUrgentePublicadaEvent;
import com.patipets.core.application.ports.output.AlertaRepositoryPort;
import com.patipets.core.application.ports.output.EventPublisherPort;
import com.patipets.core.application.ports.output.RespuestaAlertaRepositoryPort;
import com.patipets.core.application.useCase.GestionAlertaUseCase;
import com.patipets.core.domain.enums.NivelUrgencia;
import com.patipets.core.domain.enums.TipoAyudaVoluntariado;
import com.patipets.core.domain.models.Alerta;
import com.patipets.core.domain.models.RespuestaAlerta;
import java.time.LocalDateTime;
import java.util.List;

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
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("El título es obligatorio");
        }
        NivelUrgencia nivel;
        try {
            nivel = NivelUrgencia.valueOf(nivelUrgencia.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Nivel de urgencia inválido: " + nivelUrgencia);
        }
        Alerta alerta = new Alerta(
                null, titulo, descripcion, nivel, refugioId, creadoPor, true, LocalDateTime.now()
        );
        Alerta creada = alertaRepository.save(alerta);
        eventPublisher.publicar(new AlertaUrgentePublicadaEvent(
                creada.getId(), creada.getRefugioId(), creada.getTitulo(),
                creada.getDescripcion(), creada.getNivelUrgencia().name()));
        return creada;
    }

    @Override
    public List<Alerta> listarPorRefugio(Long refugioId) {
        return alertaRepository.findByRefugioId(refugioId);
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
                alerta.getRefugioId(), alerta.getCreadoPor(), false, alerta.getCreatedAt()
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
                null, alertaId, usuarioId, tipo, mensaje, LocalDateTime.now()
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
}
