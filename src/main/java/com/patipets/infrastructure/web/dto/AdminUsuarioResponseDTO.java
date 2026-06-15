package com.patipets.infrastructure.web.dto;

import com.patipets.core.domain.models.Usuario;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AdminUsuarioResponseDTO {

    private final Long id;
    private final String nombre;
    private final String email;
    private final String rol;
    private final String fotoPerfil;

    @JsonProperty("numero_contacto")
    private final String numeroContacto;

    private final String ubicacion;
    private final String biografia;
    private final boolean activo;
    private final LocalDateTime fechaRegistro;

    public AdminUsuarioResponseDTO(Long id, String nombre, String email, String rol,
                                   String fotoPerfil, String numeroContacto,
                                   String ubicacion, String biografia,
                                   boolean activo, LocalDateTime fechaRegistro) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.rol = rol;
        this.fotoPerfil = fotoPerfil;
        this.numeroContacto = numeroContacto;
        this.ubicacion = ubicacion;
        this.biografia = biografia;
        this.activo = activo;
        this.fechaRegistro = fechaRegistro;
    }

    public static AdminUsuarioResponseDTO fromDomain(Usuario usuario) {
        return new AdminUsuarioResponseDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRol().name(),
                usuario.getFotoPerfil(),
                usuario.getNumeroContacto(),
                usuario.getUbicacion(),
                usuario.getBiografia(),
                usuario.isActivo(),
                usuario.getFechaRegistro()
        );
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String getRol() { return rol; }
    public String getFotoPerfil() { return fotoPerfil; }
    public String getNumeroContacto() { return numeroContacto; }
    public String getUbicacion() { return ubicacion; }
    public String getBiografia() { return biografia; }
    public boolean isActivo() { return activo; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
}
