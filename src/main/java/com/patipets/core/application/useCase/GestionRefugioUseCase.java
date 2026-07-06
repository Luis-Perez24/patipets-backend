package com.patipets.core.application.useCase;

import com.patipets.core.domain.models.Animal;
import com.patipets.core.domain.models.PaginatedResult;
import com.patipets.core.domain.models.Refugio;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface GestionRefugioUseCase {
    Refugio solicitar(String nombre, String descripcion, String direccion, String region, String comuna,
                      Double latitud, Double longitud, Integer capacidad,
                      String email, String numeroContacto, Long usuarioId,
                      MultipartFile foto) throws IOException;
    Refugio aprobar(Long id);
    Refugio rechazar(Long id);
    List<Refugio> listarPendientes();
    boolean perteneceAUsuario(Long refugioId, Long usuarioId);
    Optional<Refugio> obtenerPorId(Long id);
    List<Refugio> listarMisRefugios(Long usuarioId);
    PaginatedResult<Refugio> listarTodos(String estado, String region, String busqueda, int page, int size);
    RefugioHistorial obtenerHistorial(Long refugioId);
}
