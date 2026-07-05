package com.patipets.infrastructure.web.dto.request;

import jakarta.validation.constraints.NotBlank;

public class UsuarioUpdateRequestDTO {

    private String nombre;
    private String email;
    private String rol;

    private String numeroContacto;

    private String ubicacion;

    private String biografia;

    public UsuarioUpdateRequestDTO() {}

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    public String getNumeroContacto() { return numeroContacto; }
    public void setNumeroContacto(String numeroContacto) { this.numeroContacto = numeroContacto; }
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    public String getBiografia() { return biografia; }
    public void setBiografia(String biografia) { this.biografia = biografia; }
}
