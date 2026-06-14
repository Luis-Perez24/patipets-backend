package com.patipets.infrastructure.persistence.repository;

import com.patipets.core.domain.enums.EstadoAnimal;
import com.patipets.infrastructure.persistence.entity.AnimalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface AnimalJpaRepository extends JpaRepository<AnimalEntity, Long> {

    List<AnimalEntity> findByEstadoAdopcion(EstadoAnimal estadoAdopcion);

    List<AnimalEntity> findByRefugioId(Long refugioId);

    @Query("SELECT a FROM AnimalEntity a WHERE a.estadoAdopcion = 'DISPONIBLE' " +
           "AND (:especie IS NULL OR a.especie = :especie) " +
           "AND (:raza IS NULL OR a.raza = :raza) " +
           "AND (:edadMin IS NULL OR a.edad >= :edadMin) " +
           "AND (:edadMax IS NULL OR a.edad <= :edadMax) " +
           "AND (:tamano IS NULL OR a.tamano = :tamano)")
    List<AnimalEntity> buscarDisponiblesConFiltros(
            @Param("especie") String especie,
            @Param("raza") String raza,
            @Param("edadMin") Integer edadMin,
            @Param("edadMax") Integer edadMax,
            @Param("tamano") String tamano);

    long countByEstadoAdopcion(EstadoAnimal estadoAdopcion);

    long countByRefugioId(Long refugioId);
}
