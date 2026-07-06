package com.patipets.infrastructure.persistence.repository;

import com.patipets.infrastructure.persistence.entity.RespuestaAlertaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface RespuestaAlertaJpaRepository extends JpaRepository<RespuestaAlertaEntity, Long> {

    List<RespuestaAlertaEntity> findByAlertaIdOrderByCreatedAtDesc(Long alertaId);

    List<RespuestaAlertaEntity> findByUsuarioIdOrderByCreatedAtDesc(Long usuarioId);

    @Query("SELECT r FROM RespuestaAlertaEntity r WHERE r.alertaId IN " +
            "(SELECT a.id FROM AlertaEntity a WHERE a.refugioId = :refugioId) " +
            "ORDER BY r.createdAt DESC")
    List<RespuestaAlertaEntity> findByRefugioIdOrderByCreatedAtDesc(@Param("refugioId") Long refugioId);

    boolean existsByAlertaIdAndUsuarioId(Long alertaId, Long usuarioId);
}
