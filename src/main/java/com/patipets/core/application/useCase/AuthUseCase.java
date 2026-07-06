package com.patipets.core.application.useCase;

import com.patipets.core.domain.models.Usuario;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Optional;

public interface AuthUseCase {
    Usuario registrar(String nombre, String email, String password, String numeroContacto,
                       String region, String comuna, String direccion);
    Usuario autenticar(String email, String password);
    Usuario activarRol(Long usuarioId, String nuevoRol);
    Usuario actualizarPerfil(Long usuarioId, String nombre, String numeroContacto,
                             String region, String comuna, String direccion,
                             String biografia, String fotoPerfil);
    Usuario actualizarFotoPerfil(Long usuarioId, MultipartFile foto) throws IOException;
    Optional<Usuario> obtenerPorId(Long id);
    void eliminarCuenta(Long id);
}
