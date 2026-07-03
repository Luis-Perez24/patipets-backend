package com.patipets.infrastructure.persistence.adapter;

import com.patipets.core.application.ports.output.NotificacionRepositoryPort;
import com.patipets.core.domain.enums.TipoNotificacion;
import com.patipets.core.domain.models.Notificacion;
import com.patipets.infrastructure.persistence.entity.NotificacionEntity;
import com.patipets.infrastructure.persistence.repository.NotificacionJpaRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class NotificacionRepositoryAdapter implements NotificacionRepositoryPort {

    private final NotificacionJpaRepository jpaRepository;

    public NotificacionRepositoryAdapter(NotificacionJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Notificacion save(Notificacion notificacion) {
        NotificacionEntity entity = toEntity(notificacion);
        NotificacionEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Notificacion> findById(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Notificacion> findByUsuarioId(Long usuarioId, int page, int size) {
        return jpaRepository.findByUsuarioIdOrderByFechaCreacionDesc(
                        usuarioId, PageRequest.of(page, size, Sort.by("fechaCreacion").descending()))
                .stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public long countByUsuarioIdAndLeidaFalse(Long usuarioId) {
        return jpaRepository.countByUsuarioIdAndLeidaFalse(usuarioId);
    }

    private Notificacion toDomain(NotificacionEntity entity) {
        return new Notificacion(
                entity.getId(), entity.getUsuarioId(), TipoNotificacion.valueOf(entity.getTipo()),
                entity.getTitulo(), entity.getMensaje(), entity.getEntidadRelacionadaId(),
                entity.isLeida(), entity.getFechaCreacion()
        );
    }

    private NotificacionEntity toEntity(Notificacion notificacion) {
        NotificacionEntity entity = new NotificacionEntity();
        entity.setId(notificacion.getId());
        entity.setUsuarioId(notificacion.getUsuarioId());
        entity.setTipo(notificacion.getTipo().name());
        entity.setTitulo(notificacion.getTitulo());
        entity.setMensaje(notificacion.getMensaje());
        entity.setEntidadRelacionadaId(notificacion.getEntidadRelacionadaId());
        entity.setLeida(notificacion.isLeida());
        entity.setFechaCreacion(notificacion.getFechaCreacion());
        return entity;
    }
}
