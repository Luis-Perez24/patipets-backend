package com.patipets.infrastructure.persistence.repository;

import com.patipets.infrastructure.persistence.entity.ApadrinamientoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ApadrinamientoJpaRepository extends JpaRepository<ApadrinamientoEntity, Long> {

    List<ApadrinamientoEntity> findByPadrinoIdOrderByFechaInicioDesc(Long padrinoId);

    List<ApadrinamientoEntity> findByAnimalIdOrderByFechaInicioDesc(Long animalId);

    List<ApadrinamientoEntity> findByRefugioIdOrderByFechaInicioDesc(Long refugioId);

    boolean existsByPadrinoIdAndAnimalIdAndActivoTrue(Long padrinoId, Long animalId);
}
