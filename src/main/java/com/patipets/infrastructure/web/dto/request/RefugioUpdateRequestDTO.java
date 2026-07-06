package com.patipets.infrastructure.web.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public class RefugioUpdateRequestDTO {

    @Size(min = 3, max = 255, message = "debe tener entre 3 y 255 caracteres")
    private String nombre;

    @Size(max = 2000, message = "no puede superar los 2000 caracteres")
    private String descripcion;

    @Size(max = 500, message = "no puede superar los 500 caracteres")
    private String direccion;

    private String region;

    private String comuna;

    @Min(value = 1, message = "debe ser al menos 1")
    private Integer capacidad;

    @Email(message = "debe ser un correo electrónico válido")
    private String email;

    @Size(max = 50, message = "no puede superar los 50 caracteres")
    private String numeroContacto;

    private Double latitud;
    private Double longitud;

    public RefugioUpdateRequestDTO() {}

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public String getComuna() { return comuna; }
    public void setComuna(String comuna) { this.comuna = comuna; }

    public Integer getCapacidad() { return capacidad; }
    public void setCapacidad(Integer capacidad) { this.capacidad = capacidad; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNumeroContacto() { return numeroContacto; }
    public void setNumeroContacto(String numeroContacto) { this.numeroContacto = numeroContacto; }

    public Double getLatitud() { return latitud; }
    public void setLatitud(Double latitud) { this.latitud = latitud; }

    public Double getLongitud() { return longitud; }
    public void setLongitud(Double longitud) { this.longitud = longitud; }
}
