package com.patipets.core.application.ports.output;

import com.patipets.core.domain.models.Animal;
import java.util.List;
import java.util.Optional;

public interface AnimalRepositoryPort {
    List<Animal> findAllDisponibles(String especie, String raza, Integer edadMin,
                                    Integer edadMax, String tamano, String region);
    Optional<Animal> findById(Long id);
    List<Animal> findByRefugioId(Long refugioId);
    Animal save(Animal animal);
    void deleteById(Long id);
    long countDisponibles();
    long countByRefugioId(Long refugioId);
}
