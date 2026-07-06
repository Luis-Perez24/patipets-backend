package com.patipets.infrastructure.persistence.repository;

import com.patipets.core.domain.enums.Rol;
import com.patipets.infrastructure.persistence.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;
import java.util.Optional;

public interface UsuarioJpaRepository extends JpaRepository<UsuarioEntity, Long>,
                                               JpaSpecificationExecutor<UsuarioEntity> {
    Optional<UsuarioEntity> findByEmail(String email);
    boolean existsByEmail(String email);
    long countByRol(Rol rol);
    List<UsuarioEntity> findByRolAndActivoTrue(Rol rol);
    Optional<UsuarioEntity> findByResetToken(String resetToken);
}
