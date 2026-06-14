package com.patipets.core.application.services;

import com.patipets.core.application.ports.output.AnimalRepositoryPort;
import com.patipets.core.application.useCase.ConsultarCatalogoPublicoUseCase;
import com.patipets.core.domain.models.Animal;
import java.util.List;
import java.util.Optional;

public class ConsultarCatalogoPublicoService implements ConsultarCatalogoPublicoUseCase {

    private final AnimalRepositoryPort animalRepository;

    public ConsultarCatalogoPublicoService(AnimalRepositoryPort animalRepository) {
        this.animalRepository = animalRepository;
    }

    @Override
    public List<Animal> obtenerTodos(String especie, String raza, Integer edadMin,
                                     Integer edadMax, String tamano, String region) {
        return animalRepository.findAllDisponibles(especie, raza, edadMin, edadMax, tamano, region);
    }

    @Override
    public Optional<Animal> obtenerPorId(Long id) {
        return animalRepository.findById(id);
    }
}
