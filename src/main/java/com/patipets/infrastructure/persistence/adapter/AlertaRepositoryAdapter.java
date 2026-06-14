package com.patipets.infrastructure.persistence.adapter;

import com.patipets.core.application.ports.output.AlertaRepositoryPort;
import com.patipets.core.domain.enums.NivelUrgencia;
import com.patipets.core.domain.models.Alerta;
import com.patipets.infrastructure.persistence.entity.AlertaEntity;
import com.patipets.infrastructure.persistence.repository.AlertaJpaRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AlertaRepositoryAdapter implements AlertaRepositoryPort {

    private final AlertaJpaRepository jpaRepository;

    public AlertaRepositoryAdapter(AlertaJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Alerta save(Alerta alerta) {
        AlertaEntity entity = toEntity(alerta);
        AlertaEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Alerta> findById(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Alerta> findByRefugioId(Long refugioId) {
        return jpaRepository.findByRefugioIdOrderByCreatedAtDesc(refugioId)
                .stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Alerta> findActivas(int page, int size) {
        return jpaRepository.findByActivaTrueOrderByCreatedAtDesc(
                        PageRequest.of(page, size, Sort.by("createdAt").descending()))
                .stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    private Alerta toDomain(AlertaEntity entity) {
        return new Alerta(
                entity.getId(), entity.getTitulo(), entity.getDescripcion(),
                NivelUrgencia.valueOf(entity.getNivelUrgencia()),
                entity.getRefugioId(), entity.getCreadoPor(),
                entity.isActiva(), entity.getCreatedAt()
        );
    }

    private AlertaEntity toEntity(Alerta alerta) {
        AlertaEntity entity = new AlertaEntity();
        entity.setId(alerta.getId());
        entity.setTitulo(alerta.getTitulo());
        entity.setDescripcion(alerta.getDescripcion());
        entity.setNivelUrgencia(alerta.getNivelUrgencia().name());
        entity.setRefugioId(alerta.getRefugioId());
        entity.setCreadoPor(alerta.getCreadoPor());
        entity.setActiva(alerta.isActiva());
        entity.setCreatedAt(alerta.getCreatedAt());
        return entity;
    }
}
