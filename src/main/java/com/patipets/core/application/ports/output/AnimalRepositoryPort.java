package com.patipets.core.application.ports.output;

import com.patipets.core.domain.models.Animal;
import com.patipets.core.domain.models.PaginatedResult;
import java.util.List;
import java.util.Optional;

public interface AnimalRepositoryPort {
    List<Animal> findAllDisponibles(String especie, String raza, Integer edadMin,
                                    Integer edadMax, String tamano, String region);
    PaginatedResult<Animal> findAll(String estado, Long refugioId, String especie, String busqueda, int page, int size);
    Optional<Animal> findById(Long id);
    List<Animal> findByRefugioId(Long refugioId);
    List<Animal> findByRefugioIdAndEstado(Long refugioId, String estado);
    Animal save(Animal animal);
    void deleteById(Long id);
    long countDisponibles();
    long countByRefugioId(Long refugioId);
}
