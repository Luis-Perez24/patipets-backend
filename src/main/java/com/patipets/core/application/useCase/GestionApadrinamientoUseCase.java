package com.patipets.core.application.useCase;

import com.patipets.core.domain.models.Apadrinamiento;
import java.util.List;

public interface GestionApadrinamientoUseCase {
    Apadrinamiento apadrinar(Long padrinoId, Long animalId, String tipoApoyo, String compromiso);
    Apadrinamiento cancelar(Long id, Long usuarioId);
    Apadrinamiento cancelarPorRefugio(Long id, Long refugioId, String motivo);
    List<Apadrinamiento> listarPorPadrino(Long padrinoId);
    List<Apadrinamiento> listarPorAnimal(Long animalId);
    List<Apadrinamiento> listarPorRefugio(Long refugioId);
}
