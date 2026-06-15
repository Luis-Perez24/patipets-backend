package com.patipets.infrastructure.web.dto.request;

import jakarta.validation.constraints.NotBlank;

public class ActualizarPerfilRequestDTO {

    private String nombre;

    @NotBlank(message = "es obligatorio")
    private String numeroContacto;

    @NotBlank(message = "es obligatorio")
    private String ubicacion;

    private String biografia;

    private String fotoPerfil;

    public ActualizarPerfilRequestDTO() {}

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getNumeroContacto() { return numeroContacto; }
    public void setNumeroContacto(String numeroContacto) { this.numeroContacto = numeroContacto; }
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    public String getBiografia() { return biografia; }
    public void setBiografia(String biografia) { this.biografia = biografia; }
    public String getFotoPerfil() { return fotoPerfil; }
    public void setFotoPerfil(String fotoPerfil) { this.fotoPerfil = fotoPerfil; }
}
