package com.patipets.infrastructure.web.dto.request;

public class UsuarioUpdateRequestDTO {

    private String nombre;
    private String email;
    private String rol;

    public UsuarioUpdateRequestDTO() {}

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
}
