package com.patipets.infrastructure.web.dto;

import com.patipets.core.domain.models.Usuario;
import java.time.LocalDateTime;

public class AdminUsuarioResponseDTO {

    private final Long id;
    private final String nombre;
    private final String email;
    private final String rol;
    private final boolean activo;
    private final LocalDateTime fechaRegistro;

    public AdminUsuarioResponseDTO(Long id, String nombre, String email, String rol,
                                   boolean activo, LocalDateTime fechaRegistro) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.rol = rol;
        this.activo = activo;
        this.fechaRegistro = fechaRegistro;
    }

    public static AdminUsuarioResponseDTO fromDomain(Usuario usuario) {
        return new AdminUsuarioResponseDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRol().name(),
                usuario.isActivo(),
                usuario.getFechaRegistro()
        );
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String getRol() { return rol; }
    public boolean isActivo() { return activo; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
}
