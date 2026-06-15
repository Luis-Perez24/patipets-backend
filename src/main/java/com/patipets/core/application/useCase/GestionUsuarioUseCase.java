package com.patipets.core.application.useCase;

import com.patipets.core.domain.models.Usuario;
import java.util.List;

public interface GestionUsuarioUseCase {
    List<Usuario> listar(String rol, Boolean activo, String fechaDesde, String fechaHasta, int page, int size);
    Usuario obtener(Long id);
    Usuario bloquear(Long id);
    Usuario desbloquear(Long id);
    Usuario editar(Long id, String nombre, String email, String rol,
                   String numeroContacto, String ubicacion, String biografia);
    long contarTotal();
}
