package com.patipets.infrastructure.persistence.adapter;

import com.patipets.core.application.ports.output.UsuarioRepositoryPort;
import com.patipets.core.domain.enums.Rol;
import com.patipets.core.domain.models.Usuario;
import com.patipets.infrastructure.persistence.entity.UsuarioEntity;
import com.patipets.infrastructure.persistence.repository.UsuarioJpaRepository;
import jakarta.persistence.criteria.Predicate;
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
    public List<Usuario> findAll(Rol rol, Boolean activo, String fechaDesde,
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
        return jpaRepository.findAll(spec, PageRequest.of(page, size, Sort.by("fechaRegistro").descending()))
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public long countAll() {
        return jpaRepository.count();
    }

    @Override
    public long countByRol(Rol rol) {
        return jpaRepository.countByRol(rol);
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
        entity.setBiografia(domain.getBiografia());
        entity.setActivo(domain.isActivo());
        entity.setFechaRegistro(domain.getFechaRegistro());
        return entity;
    }
}
