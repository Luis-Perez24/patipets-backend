package com.patipets.core.application.services;

import com.patipets.core.application.ports.output.UsuarioRepositoryPort;
import com.patipets.core.application.useCase.GestionUsuarioUseCase;
import com.patipets.core.domain.enums.Rol;
import com.patipets.core.domain.models.Usuario;
import java.util.List;

public class GestionUsuarioService implements GestionUsuarioUseCase {

    private final UsuarioRepositoryPort usuarioRepository;

    public GestionUsuarioService(UsuarioRepositoryPort usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public List<Usuario> listar(String rol, Boolean activo, String fechaDesde,
                                 String fechaHasta, int page, int size) {
        Rol rolEnum = rol != null ? Rol.valueOf(rol.toUpperCase()) : null;
        return usuarioRepository.findAll(rolEnum, activo, fechaDesde, fechaHasta, page, size);
    }

    @Override
    public Usuario obtener(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + id));
    }

    @Override
    public Usuario bloquear(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + id));
        if (!usuario.isActivo()) {
            throw new IllegalArgumentException("El usuario ya está bloqueado");
        }
        Usuario actualizado = new Usuario(
                usuario.getId(), usuario.getNombre(), usuario.getEmail(), usuario.getPassword(),
                usuario.getRol(), usuario.getFotoPerfil(), false, usuario.getFechaRegistro()
        );
        return usuarioRepository.save(actualizado);
    }

    @Override
    public Usuario desbloquear(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + id));
        if (usuario.isActivo()) {
            throw new IllegalArgumentException("El usuario ya está activo");
        }
        Usuario actualizado = new Usuario(
                usuario.getId(), usuario.getNombre(), usuario.getEmail(), usuario.getPassword(),
                usuario.getRol(), usuario.getFotoPerfil(), true, usuario.getFechaRegistro()
        );
        return usuarioRepository.save(actualizado);
    }

    @Override
    public Usuario editar(Long id, String nombre, String email, String rol) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + id));
        String nuevoNombre = nombre != null ? nombre : usuario.getNombre();
        String nuevoEmail = email != null ? email : usuario.getEmail();
        Rol nuevoRol = rol != null ? Rol.valueOf(rol.toUpperCase()) : usuario.getRol();
        Usuario actualizado = new Usuario(
                id, nuevoNombre, nuevoEmail, usuario.getPassword(),
                nuevoRol, usuario.getFotoPerfil(), usuario.isActivo(), usuario.getFechaRegistro()
        );
        return usuarioRepository.save(actualizado);
    }

    @Override
    public long contarTotal() {
        return usuarioRepository.countAll();
    }
}
