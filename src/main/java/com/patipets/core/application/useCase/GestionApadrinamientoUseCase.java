package com.patipets.core.application.useCase;

import com.patipets.core.domain.models.Apadrinamiento;
import java.util.List;

public interface GestionApadrinamientoUseCase {
    Apadrinamiento apadrinar(Long padrinoId, Long animalId, String tipoApoyo);
    Apadrinamiento cancelar(Long id, Long usuarioId);
    List<Apadrinamiento> listarPorPadrino(Long padrinoId);
    List<Apadrinamiento> listarPorAnimal(Long animalId);
    List<Apadrinamiento> listarPorRefugio(Long refugioId);
}
