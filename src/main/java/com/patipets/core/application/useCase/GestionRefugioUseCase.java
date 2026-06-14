package com.patipets.core.application.useCase;

import com.patipets.core.domain.models.Refugio;
import java.util.List;

public interface GestionRefugioUseCase {
    Refugio solicitar(String nombre, String direccion, String region,
                      Double latitud, Double longitud, Integer capacidad);
    Refugio aprobar(Long id);
    Refugio rechazar(Long id);
    List<Refugio> listarPendientes();
}
