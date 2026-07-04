package com.patipets.core.application.useCase;

import com.patipets.core.domain.models.Refugio;
import java.util.List;
import java.util.Optional;

public interface GestionRefugioUseCase {
    Refugio solicitar(String nombre, String direccion, String region, String comuna,
                      Double latitud, Double longitud, Integer capacidad,
                      String email, String numeroContacto, Long usuarioId);
    Refugio aprobar(Long id);
    Refugio rechazar(Long id);
    List<Refugio> listarPendientes();
    boolean perteneceAUsuario(Long refugioId, Long usuarioId);
    Optional<Refugio> obtenerPorId(Long id);
}
