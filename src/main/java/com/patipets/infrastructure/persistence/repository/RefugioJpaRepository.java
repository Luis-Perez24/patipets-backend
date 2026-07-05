package com.patipets.infrastructure.persistence.repository;

import com.patipets.core.domain.enums.EstadoRefugio;
import com.patipets.infrastructure.persistence.entity.RefugioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;

public interface RefugioJpaRepository extends JpaRepository<RefugioEntity, Long>,
                                               JpaSpecificationExecutor<RefugioEntity> {

    List<RefugioEntity> findByEstado(EstadoRefugio estado);

    long countByEstado(EstadoRefugio estado);
}
