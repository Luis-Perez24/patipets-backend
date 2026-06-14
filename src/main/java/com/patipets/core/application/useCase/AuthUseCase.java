package com.patipets.core.application.useCase;

import com.patipets.core.domain.models.Usuario;
import java.util.Optional;

public interface AuthUseCase {
    Usuario registrar(String nombre, String email, String password);
    Usuario autenticar(String email, String password);
    Usuario activarRol(Long usuarioId, String nuevoRol);
    Optional<Usuario> obtenerPorId(Long id);
}
