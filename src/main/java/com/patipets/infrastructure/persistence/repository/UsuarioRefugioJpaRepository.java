package com.patipets.infrastructure.persistence.repository;

import com.patipets.infrastructure.persistence.entity.UsuarioRefugioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UsuarioRefugioJpaRepository extends JpaRepository<UsuarioRefugioEntity, Long> {

    boolean existsByUsuarioIdAndRefugioId(Long usuarioId, Long refugioId);
    List<UsuarioRefugioEntity> findByUsuarioId(Long usuarioId);
}
