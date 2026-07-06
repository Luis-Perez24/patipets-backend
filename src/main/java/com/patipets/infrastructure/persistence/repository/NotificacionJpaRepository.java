package com.patipets.infrastructure.persistence.repository;

import com.patipets.infrastructure.persistence.entity.NotificacionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface NotificacionJpaRepository extends JpaRepository<NotificacionEntity, Long> {

    Page<NotificacionEntity> findByUsuarioIdOrderByFechaCreacionDesc(Long usuarioId, Pageable pageable);

    long countByUsuarioIdAndLeidaFalse(Long usuarioId);

    @Modifying
    @Query("UPDATE NotificacionEntity n SET n.leida = true, n.fechaCreacion = :now WHERE n.usuarioId = :usuarioId AND n.leida = false")
    int marcarTodasLeidas(@Param("usuarioId") Long usuarioId, @Param("now") LocalDateTime now);
}
