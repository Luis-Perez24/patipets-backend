package com.patipets.infrastructure.persistence.repository;

import com.patipets.infrastructure.persistence.entity.AlertaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AlertaJpaRepository extends JpaRepository<AlertaEntity, Long> {

    List<AlertaEntity> findByRefugioIdOrderByCreatedAtDesc(Long refugioId);

    Page<AlertaEntity> findByActivaTrueOrderByCreatedAtDesc(Pageable pageable);

    long countByRefugioIdAndActivaTrue(Long refugioId);
}
