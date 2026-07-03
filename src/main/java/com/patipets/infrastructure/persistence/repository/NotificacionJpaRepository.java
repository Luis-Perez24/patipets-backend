package com.patipets.infrastructure.persistence.repository;

import com.patipets.infrastructure.persistence.entity.NotificacionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificacionJpaRepository extends JpaRepository<NotificacionEntity, Long> {

    Page<NotificacionEntity> findByUsuarioIdOrderByFechaCreacionDesc(Long usuarioId, Pageable pageable);

    long countByUsuarioIdAndLeidaFalse(Long usuarioId);
}
