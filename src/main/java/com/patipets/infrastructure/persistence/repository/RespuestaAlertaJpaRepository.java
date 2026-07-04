package com.patipets.infrastructure.persistence.repository;

import com.patipets.infrastructure.persistence.entity.RespuestaAlertaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RespuestaAlertaJpaRepository extends JpaRepository<RespuestaAlertaEntity, Long> {

    List<RespuestaAlertaEntity> findByAlertaIdOrderByCreatedAtDesc(Long alertaId);

    List<RespuestaAlertaEntity> findByUsuarioIdOrderByCreatedAtDesc(Long usuarioId);

    boolean existsByAlertaIdAndUsuarioId(Long alertaId, Long usuarioId);
}
