package com.patipets.infrastructure.persistence.adapter;

import com.patipets.core.application.ports.output.ApadrinamientoRepositoryPort;
import com.patipets.core.domain.enums.TipoApoyo;
import com.patipets.core.domain.models.Apadrinamiento;
import com.patipets.infrastructure.persistence.entity.ApadrinamientoEntity;
import com.patipets.infrastructure.persistence.repository.ApadrinamientoJpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ApadrinamientoRepositoryAdapter implements ApadrinamientoRepositoryPort {

    private final ApadrinamientoJpaRepository jpaRepository;

    public ApadrinamientoRepositoryAdapter(ApadrinamientoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Apadrinamiento save(Apadrinamiento apadrinamiento) {
        ApadrinamientoEntity entity = toEntity(apadrinamiento);
        ApadrinamientoEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Apadrinamiento> findById(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Apadrinamiento> findByPadrinoId(Long padrinoId) {
        return jpaRepository.findByPadrinoIdOrderByFechaInicioDesc(padrinoId)
                .stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Apadrinamiento> findByAnimalId(Long animalId) {
        return jpaRepository.findByAnimalIdOrderByFechaInicioDesc(animalId)
                .stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Apadrinamiento> findByRefugioId(Long refugioId) {
        return jpaRepository.findByRefugioIdOrderByFechaInicioDesc(refugioId)
                .stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public boolean existsByPadrinoIdAndAnimalIdAndActivoTrue(Long padrinoId, Long animalId) {
        return jpaRepository.existsByPadrinoIdAndAnimalIdAndActivoTrue(padrinoId, animalId);
    }

    private Apadrinamiento toDomain(ApadrinamientoEntity entity) {
        return new Apadrinamiento(
                entity.getId(), entity.getPadrinoId(), entity.getAnimalId(),
                entity.getRefugioId(), TipoApoyo.valueOf(entity.getTipoApoyo()),
                entity.getFechaInicio(), entity.isActivo()
        );
    }

    private ApadrinamientoEntity toEntity(Apadrinamiento apadrinamiento) {
        ApadrinamientoEntity entity = new ApadrinamientoEntity();
        entity.setId(apadrinamiento.getId());
        entity.setPadrinoId(apadrinamiento.getPadrinoId());
        entity.setAnimalId(apadrinamiento.getAnimalId());
        entity.setRefugioId(apadrinamiento.getRefugioId());
        entity.setTipoApoyo(apadrinamiento.getTipoApoyo().name());
        entity.setFechaInicio(apadrinamiento.getFechaInicio());
        entity.setActivo(apadrinamiento.isActivo());
        return entity;
    }
}
