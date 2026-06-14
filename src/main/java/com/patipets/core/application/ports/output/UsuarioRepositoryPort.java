package com.patipets.core.application.ports.output;

import com.patipets.core.domain.enums.Rol;
import com.patipets.core.domain.models.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepositoryPort {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findById(Long id);
    Usuario save(Usuario usuario);
    boolean existsByEmail(String email);
    List<Usuario> findAll(Rol rol, Boolean activo, String fechaDesde, String fechaHasta, int page, int size);
    long countAll();
    long countByRol(Rol rol);
}
