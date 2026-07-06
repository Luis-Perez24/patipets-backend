package com.patipets.infrastructure.persistence.adapter;

import com.patipets.core.application.ports.output.RespuestaAlertaRepositoryPort;
import com.patipets.core.domain.enums.TipoAyudaVoluntariado;
import com.patipets.core.domain.models.RespuestaAlerta;
import com.patipets.infrastructure.persistence.entity.RespuestaAlertaEntity;
import com.patipets.infrastructure.persistence.repository.RespuestaAlertaJpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RespuestaAlertaRepositoryAdapter implements RespuestaAlertaRepositoryPort {

    private final RespuestaAlertaJpaRepository jpaRepository;

    public RespuestaAlertaRepositoryAdapter(RespuestaAlertaJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public RespuestaAlerta save(RespuestaAlerta respuesta) {
        RespuestaAlertaEntity entity = toEntity(respuesta);
        RespuestaAlertaEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<RespuestaAlerta> findById(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<RespuestaAlerta> findByAlertaId(Long alertaId) {
        return jpaRepository.findByAlertaIdOrderByCreatedAtDesc(alertaId)
                .stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<RespuestaAlerta> findByUsuarioId(Long usuarioId) {
        return jpaRepository.findByUsuarioIdOrderByCreatedAtDesc(usuarioId)
                .stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<RespuestaAlerta> findByRefugioId(Long refugioId) {
        return jpaRepository.findByRefugioIdOrderByCreatedAtDesc(refugioId)
                .stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public boolean existsByAlertaIdAndUsuarioId(Long alertaId, Long usuarioId) {
        return jpaRepository.existsByAlertaIdAndUsuarioId(alertaId, usuarioId);
    }

    private RespuestaAlerta toDomain(RespuestaAlertaEntity entity) {
        TipoAyudaVoluntariado tipo = null;
        try {
            tipo = TipoAyudaVoluntariado.valueOf(entity.getTipoAyuda());
        } catch (IllegalArgumentException ignored) {
            tipo = null;
        }
        return new RespuestaAlerta(
                entity.getId(), entity.getAlertaId(), entity.getUsuarioId(),
                tipo,
                entity.getMensaje(), entity.getDisponibilidad(), entity.getCreatedAt(),
                entity.getEstado(), entity.getFechaCancelacion(), entity.getMotivoCancelacion()
        );
    }

    private RespuestaAlertaEntity toEntity(RespuestaAlerta respuesta) {
        RespuestaAlertaEntity entity = new RespuestaAlertaEntity();
        entity.setId(respuesta.getId());
        entity.setAlertaId(respuesta.getAlertaId());
        entity.setUsuarioId(respuesta.getUsuarioId());
        entity.setTipoAyuda(respuesta.getTipoAyuda() != null ? respuesta.getTipoAyuda().name() : "OTROS");
        entity.setMensaje(respuesta.getMensaje());
        entity.setDisponibilidad(respuesta.getDisponibilidad());
        entity.setCreatedAt(respuesta.getCreatedAt());
        entity.setEstado(respuesta.getEstado() != null ? respuesta.getEstado() : "ACTIVA");
        entity.setFechaCancelacion(respuesta.getFechaCancelacion());
        entity.setMotivoCancelacion(respuesta.getMotivoCancelacion());
        return entity;
    }
}
