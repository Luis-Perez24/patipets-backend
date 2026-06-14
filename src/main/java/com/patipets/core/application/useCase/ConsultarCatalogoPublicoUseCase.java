package com.patipets.core.application.useCase;

import com.patipets.core.domain.models.Animal;
import java.util.List;
import java.util.Optional;

public interface ConsultarCatalogoPublicoUseCase {
    List<Animal> obtenerTodos(String especie, String raza, Integer edadMin,
                              Integer edadMax, String tamano, String region);
    Optional<Animal> obtenerPorId(Long id);
}
