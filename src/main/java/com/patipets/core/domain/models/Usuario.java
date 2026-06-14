package com.patipets.core.domain.models;

import com.patipets.core.domain.enums.Rol;
import java.time.LocalDateTime;

public class Usuario {
    private final Long id;
    private final String nombre;
    private final String email;
    private final String password;
    private final Rol rol;
    private final String fotoPerfil;
    private final boolean activo;
    private final LocalDateTime fechaRegistro;

    public Usuario(Long id, String nombre, String email, String password,
                   Rol rol, String fotoPerfil, boolean activo, LocalDateTime fechaRegistro) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.rol = rol;
        this.fotoPerfil = fotoPerfil;
        this.activo = activo;
        this.fechaRegistro = fechaRegistro;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public Rol getRol() { return rol; }
    public String getFotoPerfil() { return fotoPerfil; }
    public boolean isActivo() { return activo; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
}
