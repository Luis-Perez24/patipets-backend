package com.patipets.infrastructure.persistence.adapter;

import com.patipets.core.application.ports.output.UsuarioRepositoryPort;
import com.patipets.core.domain.enums.Rol;
import com.patipets.core.domain.models.PaginatedResult;
import com.patipets.core.domain.models.Usuario;
import com.patipets.infrastructure.persistence.entity.UsuarioEntity;
import com.patipets.infrastructure.persistence.repository.UsuarioJpaRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioRepositoryAdapter implements UsuarioRepositoryPort {

    private final UsuarioJpaRepository jpaRepository;

    public UsuarioRepositoryAdapter(UsuarioJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
        return jpaRepository.findByEmail(email).map(this::toDomain);
    }

    @Override
    public Optional<Usuario> findById(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Usuario save(Usuario usuario) {
        UsuarioEntity entity = toEntity(usuario);
        UsuarioEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public PaginatedResult<Usuario> findAll(Rol rol, Boolean activo, String fechaDesde,
                                             String fechaHasta, int page, int size) {
        Specification<UsuarioEntity> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (rol != null) {
                predicates.add(cb.equal(root.get("rol"), rol));
            }
            if (activo != null) {
                predicates.add(cb.equal(root.get("activo"), activo));
            }
            if (fechaDesde != null) {
                LocalDateTime desde = LocalDate.parse(fechaDesde).atStartOfDay();
                predicates.add(cb.greaterThanOrEqualTo(root.get("fechaRegistro"), desde));
            }
            if (fechaHasta != null) {
                LocalDateTime hasta = LocalDate.parse(fechaHasta).atTime(LocalTime.MAX);
                predicates.add(cb.lessThanOrEqualTo(root.get("fechaRegistro"), hasta));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        Page<UsuarioEntity> pageResult = jpaRepository.findAll(spec, PageRequest.of(page, size, Sort.by("fechaRegistro").descending()));
        List<Usuario> items = pageResult.getContent().stream()
                .map(this::toDomain)
                .toList();
        return new PaginatedResult<>(items, pageResult.getTotalPages(), pageResult.getTotalElements(), pageResult.getNumber(), pageResult.getSize());
    }

    @Override
    public long countAll() {
        return jpaRepository.count();
    }

    @Override
    public long countByRol(Rol rol) {
        return jpaRepository.countByRol(rol);
    }

    @Override
    public List<Usuario> findActivosByRol(Rol rol) {
        return jpaRepository.findByRolAndActivoTrue(rol).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    private Usuario toDomain(UsuarioEntity entity) {
        return new Usuario(
                entity.getId(),
                entity.getNombre(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getRol(),
                entity.getFotoPerfil(),
                entity.getNumeroContacto(),
                entity.getUbicacion(),
                entity.getRegion(),
                entity.getComuna(),
                entity.getDireccion(),
                entity.getBiografia(),
                entity.isActivo(),
                entity.getFechaRegistro()
        );
    }

    private UsuarioEntity toEntity(Usuario domain) {
        UsuarioEntity entity = new UsuarioEntity();
        entity.setId(domain.getId());
        entity.setNombre(domain.getNombre());
        entity.setEmail(domain.getEmail());
        entity.setPassword(domain.getPassword());
        entity.setRol(domain.getRol());
        entity.setFotoPerfil(domain.getFotoPerfil());
        entity.setNumeroContacto(domain.getNumeroContacto());
        entity.setUbicacion(domain.getUbicacion());
        entity.setRegion(domain.getRegion());
        entity.setComuna(domain.getComuna());
        entity.setDireccion(domain.getDireccion());
        entity.setBiografia(domain.getBiografia());
        entity.setActivo(domain.isActivo());
        entity.setFechaRegistro(domain.getFechaRegistro());
        return entity;
    }
}
