package com.patipets.infrastructure.persistence.adapter;

import com.patipets.core.application.ports.output.RefugioRepositoryPort;
import com.patipets.core.domain.enums.EstadoRefugio;
import com.patipets.core.domain.models.PaginatedResult;
import com.patipets.core.domain.models.Refugio;
import com.patipets.infrastructure.persistence.entity.RefugioEntity;
import com.patipets.infrastructure.persistence.entity.UsuarioEntity;
import com.patipets.infrastructure.persistence.entity.UsuarioRefugioEntity;
import com.patipets.infrastructure.persistence.repository.RefugioJpaRepository;
import com.patipets.infrastructure.persistence.repository.UsuarioJpaRepository;
import com.patipets.infrastructure.persistence.repository.UsuarioRefugioJpaRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class RefugioRepositoryAdapter implements RefugioRepositoryPort {

    private final RefugioJpaRepository jpaRepository;
    private final UsuarioRefugioJpaRepository usuarioRefugioJpaRepository;
    private final UsuarioJpaRepository usuarioJpaRepository;

    public RefugioRepositoryAdapter(RefugioJpaRepository jpaRepository,
                                     UsuarioRefugioJpaRepository usuarioRefugioJpaRepository,
                                     UsuarioJpaRepository usuarioJpaRepository) {
        this.jpaRepository = jpaRepository;
        this.usuarioRefugioJpaRepository = usuarioRefugioJpaRepository;
        this.usuarioJpaRepository = usuarioJpaRepository;
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
        List<RefugioEntity> entities = jpaRepository.findByEstado(estado);
        List<Long> usuarioIds = entities.stream()
                .map(RefugioEntity::getUsuarioId)
                .filter(id -> id != null)
                .distinct()
                .collect(Collectors.toList());
        Map<Long, String> userNames = usuarioJpaRepository.findAllById(usuarioIds).stream()
                .collect(Collectors.toMap(UsuarioEntity::getId, UsuarioEntity::getNombre));
        return entities.stream()
                .map(e -> toDomain(e, userNames.get(e.getUsuarioId())))
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

    @Override
    public void vincularUsuario(Long usuarioId, Long refugioId) {
        if (!usuarioRefugioJpaRepository.existsByUsuarioIdAndRefugioId(usuarioId, refugioId)) {
            usuarioRefugioJpaRepository.save(new UsuarioRefugioEntity(usuarioId, refugioId));
        }
    }

    @Override
    public boolean perteneceAUsuario(Long refugioId, Long usuarioId) {
        return usuarioRefugioJpaRepository.existsByUsuarioIdAndRefugioId(usuarioId, refugioId);
    }

    @Override
    public List<Refugio> findByUsuario(Long usuarioId) {
        List<Long> refugioIds = usuarioRefugioJpaRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(UsuarioRefugioEntity::getRefugioId)
                .collect(Collectors.toList());
        return jpaRepository.findAllById(refugioIds)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Refugio> findSolicitudesByUsuario(Long usuarioId) {
        return jpaRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public PaginatedResult<Refugio> findAll(String estado, String region, String busqueda, int page, int size) {
        Specification<RefugioEntity> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (estado != null && !estado.isEmpty()) {
                predicates.add(cb.equal(root.get("estado"), EstadoRefugio.valueOf(estado)));
            }
            if (region != null && !region.isEmpty()) {
                predicates.add(cb.equal(root.get("region"), region));
            }
            if (busqueda != null && !busqueda.isEmpty()) {
                String pattern = "%" + busqueda.toLowerCase() + "%";
                predicates.add(cb.like(cb.lower(root.get("nombre")), pattern));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        Page<RefugioEntity> pageResult = jpaRepository.findAll(spec,
                PageRequest.of(page, size, Sort.by("fechaCreacion").descending()));
        List<Refugio> items = pageResult.getContent().stream()
                .map(this::toDomain)
                .toList();
        return new PaginatedResult<>(items, pageResult.getTotalPages(),
                pageResult.getTotalElements(), pageResult.getNumber(), pageResult.getSize());
    }

    private Refugio toDomain(RefugioEntity entity) {
        return toDomain(entity, null);
    }

    private Refugio toDomain(RefugioEntity entity, String usuarioNombre) {
        return new Refugio(
                entity.getId(),
                entity.getNombre(),
                entity.getDescripcion(),
                entity.getDireccion(),
                entity.getRegion(),
                entity.getComuna(),
                entity.getLatitud(),
                entity.getLongitud(),
                entity.getCapacidad(),
                entity.getEmail(),
                entity.getNumeroContacto(),
                entity.getEstado(),
                entity.getFoto(),
                entity.getUsuarioId(),
                usuarioNombre,
                entity.getFechaCreacion()
        );
    }

    private RefugioEntity toEntity(Refugio refugio) {
        RefugioEntity entity = new RefugioEntity();
        entity.setId(refugio.getId());
        entity.setNombre(refugio.getNombre());
        entity.setDescripcion(refugio.getDescripcion());
        entity.setDireccion(refugio.getDireccion());
        entity.setRegion(refugio.getRegion());
        entity.setComuna(refugio.getComuna());
        entity.setLatitud(refugio.getLatitud());
        entity.setLongitud(refugio.getLongitud());
        entity.setCapacidad(refugio.getCapacidad());
        entity.setEmail(refugio.getEmail());
        entity.setNumeroContacto(refugio.getNumeroContacto());
        entity.setEstado(refugio.getEstado());
        entity.setFoto(refugio.getFoto());
        entity.setUsuarioId(refugio.getUsuarioId());
        entity.setFechaCreacion(refugio.getFechaCreacion());
        return entity;
    }
}
