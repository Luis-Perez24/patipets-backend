package com.patipets.infrastructure.persistence.repository;

import com.patipets.infrastructure.persistence.entity.SolicitudAdopcionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SolicitudAdopcionJpaRepository extends JpaRepository<SolicitudAdopcionEntity, Long> {

    List<SolicitudAdopcionEntity> findByAdoptanteId(Long adoptanteId);

    List<SolicitudAdopcionEntity> findByRefugioId(Long refugioId);

    List<SolicitudAdopcionEntity> findByAnimalId(Long animalId);

    List<SolicitudAdopcionEntity> findByAnimalIdAndEstado(Long animalId, String estado);

    long countByRefugioIdAndEstado(Long refugioId, String estado);

    long countByEstado(String estado);

    Page<SolicitudAdopcionEntity> findByEstado(String estado, Pageable pageable);
}
