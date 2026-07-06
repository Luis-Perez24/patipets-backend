package com.patipets.core.application.services;

import com.patipets.core.application.ports.output.ImageStoragePort;
import com.patipets.core.application.ports.output.PasswordEncoderPort;
import com.patipets.core.application.ports.output.RefugioRepositoryPort;
import com.patipets.core.application.ports.output.UsuarioRepositoryPort;
import com.patipets.core.application.useCase.AuthUseCase;
import com.patipets.core.domain.enums.EstadoRefugio;
import com.patipets.core.domain.enums.Rol;
import com.patipets.core.domain.models.Refugio;
import com.patipets.core.domain.models.Usuario;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class AuthService implements AuthUseCase {

    private final UsuarioRepositoryPort usuarioRepository;
    private final PasswordEncoderPort passwordEncoder;
    private final ImageStoragePort imageStoragePort;
    private final RefugioRepositoryPort refugioRepository;

    public AuthService(UsuarioRepositoryPort usuarioRepository,
                         PasswordEncoderPort passwordEncoder,
                         ImageStoragePort imageStoragePort,
                         RefugioRepositoryPort refugioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.imageStoragePort = imageStoragePort;
        this.refugioRepository = refugioRepository;
    }

    private void enriquecerConRefugio(Usuario usuario) {
        if (refugioRepository == null || usuario == null || usuario.getId() == null) {
            return;
        }
        List<Refugio> refugios = refugioRepository.findByUsuario(usuario.getId());
        if (refugios == null || refugios.isEmpty()) {
            return;
        }
        Refugio seleccionado = refugios.stream()
                .filter(r -> r.getEstado() == EstadoRefugio.APROBADO)
                .findFirst()
                .orElse(refugios.get(0));
        usuario.setRefugioId(seleccionado.getId());
    }



    @Override
    public Usuario registrar(String nombre, String email, String password, String numeroContacto,
                              String region, String comuna, String direccion) {
        if (usuarioRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("El email ya está registrado");
        }
        String hashedPassword = passwordEncoder.encode(password);
        Usuario nuevo = new Usuario(
                null, nombre, email, hashedPassword,
                Rol.CIUDADANO, null, numeroContacto, null,
                region, comuna, direccion, null, true, LocalDateTime.now()
        );
        Usuario guardado = usuarioRepository.save(nuevo);
        enriquecerConRefugio(guardado);
        return guardado;
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
        enriquecerConRefugio(usuario);
        return usuario;
    }

    @Override
    public Usuario activarRol(Long usuarioId, String nuevoRol) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        Rol rol;
        try {
            rol = Rol.valueOf(nuevoRol.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Rol inválido: " + nuevoRol);
        }

        if (rol == Rol.ADMIN) {
            throw new IllegalArgumentException("No puedes auto-asignarte el rol ADMIN");
        }

        if (rol == Rol.REFUGIO) {
            List<Refugio> refugios = refugioRepository.findByUsuario(usuarioId);
            boolean tieneRefugioAprobado = refugios.stream()
                    .anyMatch(r -> r.getEstado() == EstadoRefugio.APROBADO);
            if (!tieneRefugioAprobado) {
                throw new IllegalArgumentException("No tienes un refugio aprobado para administrar.");
            }
        }

        Usuario actualizado = new Usuario(
                usuario.getId(), usuario.getNombre(), usuario.getEmail(),
                usuario.getPassword(), rol, usuario.getFotoPerfil(),
                usuario.getNumeroContacto(), usuario.getUbicacion(),
                usuario.getRegion(), usuario.getComuna(), usuario.getDireccion(), usuario.getBiografia(),
                usuario.isActivo(), usuario.getFechaRegistro()
        );
        Usuario guardado = usuarioRepository.save(actualizado);
        enriquecerConRefugio(guardado);
        return guardado;
    }

    @Override
    public Usuario actualizarPerfil(Long usuarioId, String nombre, String numeroContacto,
                                     String region, String comuna, String direccion,
                                     String biografia, String fotoPerfil) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        String nuevoNombre = nombre != null ? nombre : usuario.getNombre();
        String nuevaFoto = fotoPerfil != null ? fotoPerfil : usuario.getFotoPerfil();
        String nuevaBiografia = biografia != null ? biografia : usuario.getBiografia();
        Usuario actualizado = new Usuario(
                usuario.getId(), nuevoNombre, usuario.getEmail(),
                usuario.getPassword(), usuario.getRol(), nuevaFoto,
                numeroContacto, usuario.getUbicacion(), region, comuna, direccion, nuevaBiografia,
                usuario.isActivo(), usuario.getFechaRegistro()
        );
        Usuario guardado = usuarioRepository.save(actualizado);
        enriquecerConRefugio(guardado);
        return guardado;
    }

    @Override
    public Usuario actualizarFotoPerfil(Long usuarioId, MultipartFile foto) throws IOException {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        String fotoAnterior = usuario.getFotoPerfil();
        String nuevaUrl = imageStoragePort.upload(foto);
        Usuario actualizado = new Usuario(
                usuario.getId(), usuario.getNombre(), usuario.getEmail(),
                usuario.getPassword(), usuario.getRol(), nuevaUrl,
                usuario.getNumeroContacto(), usuario.getUbicacion(),
                usuario.getRegion(), usuario.getComuna(), usuario.getDireccion(),
                usuario.getBiografia(), usuario.isActivo(), usuario.getFechaRegistro()
        );
        Usuario guardado = usuarioRepository.save(actualizado);
        if (fotoAnterior != null) {
            imageStoragePort.delete(fotoAnterior);
        }
        enriquecerConRefugio(guardado);
        return guardado;
    }

    @Override
    public Optional<Usuario> obtenerPorId(Long id) {
        return usuarioRepository.findById(id).map(usuario -> {
            enriquecerConRefugio(usuario);
            return usuario;
        });
    }
}
