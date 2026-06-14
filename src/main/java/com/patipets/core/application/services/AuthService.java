package com.patipets.core.application.services;

import com.patipets.core.application.ports.output.PasswordEncoderPort;
import com.patipets.core.application.ports.output.UsuarioRepositoryPort;
import com.patipets.core.application.useCase.AuthUseCase;
import com.patipets.core.domain.enums.Rol;
import com.patipets.core.domain.models.Usuario;
import java.time.LocalDateTime;
import java.util.Optional;

public class AuthService implements AuthUseCase {

    private final UsuarioRepositoryPort usuarioRepository;
    private final PasswordEncoderPort passwordEncoder;

    public AuthService(UsuarioRepositoryPort usuarioRepository,
                         PasswordEncoderPort passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }



    @Override
    public Usuario registrar(String nombre, String email, String password) {
        if (usuarioRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("El email ya está registrado");
        }
        String hashedPassword = passwordEncoder.encode(password);
        Usuario nuevo = new Usuario(
                null, nombre, email, hashedPassword,
                Rol.CIUDADANO, null, true, LocalDateTime.now()
        );
        return usuarioRepository.save(nuevo);
    }

    @Override
    public Usuario autenticar(String email, String password) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Credenciales inválidas"));
        if (!usuario.isActivo()) {
            throw new IllegalStateException("La cuenta está desactivada");
        }
        if (!passwordEncoder.matches(password, usuario.getPassword())) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }
        return usuario;
    }

    @Override
    public Usuario activarRol(Long usuarioId, String nuevoRol) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        try {
            Rol rol = Rol.valueOf(nuevoRol.toUpperCase());
            if (rol == Rol.ADMIN) {
                throw new IllegalArgumentException("No puedes auto-asignarte el rol ADMIN");
            }
            Usuario actualizado = new Usuario(
                    usuario.getId(), usuario.getNombre(), usuario.getEmail(),
                    usuario.getPassword(), rol, usuario.getFotoPerfil(),
                    usuario.isActivo(), usuario.getFechaRegistro()
            );
            return usuarioRepository.save(actualizado);
        } catch (IllegalArgumentException e) {
            if (e.getMessage() != null && e.getMessage().contains("No puedes")) throw e;
            throw new IllegalArgumentException("Rol inválido: " + nuevoRol);
        }
    }

    @Override
    public Optional<Usuario> obtenerPorId(Long id) {
        return usuarioRepository.findById(id);
    }
}
