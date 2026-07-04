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
    private final String numeroContacto;
    private final String ubicacion;
    private final String region;
    private final String comuna;
    private final String direccion;
    private final String biografia;
    private final boolean activo;
    private final LocalDateTime fechaRegistro;

    public Usuario(Long id, String nombre, String email, String password,
                   Rol rol, String fotoPerfil, String numeroContacto,
                   String ubicacion, String region, String comuna, String direccion,
                   String biografia, boolean activo, LocalDateTime fechaRegistro) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.rol = rol;
        this.fotoPerfil = fotoPerfil;
        this.numeroContacto = numeroContacto;
        this.ubicacion = ubicacion;
        this.region = region;
        this.comuna = comuna;
        this.direccion = direccion;
        this.biografia = biografia;
        this.activo = activo;
        this.fechaRegistro = fechaRegistro;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public Rol getRol() { return rol; }
    public String getFotoPerfil() { return fotoPerfil; }
    public String getNumeroContacto() { return numeroContacto; }
    public String getUbicacion() { return ubicacion; }
    public String getRegion() { return region; }
    public String getComuna() { return comuna; }
    public String getDireccion() { return direccion; }
    public String getBiografia() { return biografia; }
    public boolean isActivo() { return activo; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
}
