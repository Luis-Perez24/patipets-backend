package com.patipets.core.application.useCase;

import com.patipets.core.domain.models.Alerta;
import java.util.List;

public interface GestionAlertaUseCase {
    Alerta crear(String titulo, String descripcion, String nivelUrgencia, Long refugioId, Long creadoPor);
    List<Alerta> listarPorRefugio(Long refugioId);
    List<Alerta> listarActivas(int page, int size);
    Alerta marcarResuelta(Long id);
    void eliminar(Long id);
}
