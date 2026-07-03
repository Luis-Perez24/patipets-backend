package com.patipets.infrastructure.persistence.repository;

import com.patipets.infrastructure.persistence.entity.UsuarioRefugioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRefugioJpaRepository extends JpaRepository<UsuarioRefugioEntity, Long> {

    boolean existsByUsuarioIdAndRefugioId(Long usuarioId, Long refugioId);
}
