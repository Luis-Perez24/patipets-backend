package com.patipets.infrastructure.persistence.adapter;

import com.patipets.core.application.ports.output.RefugioRepositoryPort;
import com.patipets.core.domain.enums.EstadoRefugio;
import com.patipets.core.domain.models.Refugio;
import com.patipets.infrastructure.persistence.entity.RefugioEntity;
import com.patipets.infrastructure.persistence.repository.RefugioJpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RefugioRepositoryAdapter implements RefugioRepositoryPort {

    private final RefugioJpaRepository jpaRepository;

    public RefugioRepositoryAdapter(RefugioJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<Refugio> findAllAprobados() {
        return jpaRepository.findByEstado(EstadoRefugio.APROBADO)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Refugio> findByEstado(EstadoRefugio estado) {
        return jpaRepository.findByEstado(estado)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Refugio> findById(Long id) {
        return jpaRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public Refugio save(Refugio refugio) {
        RefugioEntity entity = toEntity(refugio);
        RefugioEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public long countRegistrados() {
        return jpaRepository.count();
    }

    @Override
    public long countByEstado(EstadoRefugio estado) {
        return jpaRepository.countByEstado(estado);
    }

    private Refugio toDomain(RefugioEntity entity) {
        return new Refugio(
                entity.getId(),
                entity.getNombre(),
                entity.getDireccion(),
                entity.getRegion(),
                entity.getComuna(),
                entity.getLatitud(),
                entity.getLongitud(),
                entity.getCapacidad(),
                entity.getEmail(),
                entity.getNumeroContacto(),
                entity.getEstado()
        );
    }

    private RefugioEntity toEntity(Refugio refugio) {
        RefugioEntity entity = new RefugioEntity();
        entity.setId(refugio.getId());
        entity.setNombre(refugio.getNombre());
        entity.setDireccion(refugio.getDireccion());
        entity.setRegion(refugio.getRegion());
        entity.setComuna(refugio.getComuna());
        entity.setLatitud(refugio.getLatitud());
        entity.setLongitud(refugio.getLongitud());
        entity.setCapacidad(refugio.getCapacidad());
        entity.setEmail(refugio.getEmail());
        entity.setNumeroContacto(refugio.getNumeroContacto());
        entity.setEstado(refugio.getEstado());
        return entity;
    }
}
