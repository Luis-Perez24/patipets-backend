package com.patipets.infrastructure.web.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RefugioSolicitudRequestDTO {

    @NotBlank(message = "es obligatorio")
    private String nombre;

    private String descripcion;

    @NotBlank(message = "es obligatorio")
    private String direccion;

    @NotBlank(message = "es obligatorio")
    private String region;

    @NotBlank(message = "es obligatorio")
    private String comuna;

    @NotNull(message = "es obligatorio")
    @DecimalMin(value = "-90", message = "debe estar entre -90 y 90")
    @DecimalMax(value = "90", message = "debe estar entre -90 y 90")
    private Double latitud;

    @NotNull(message = "es obligatorio")
    @DecimalMin(value = "-180", message = "debe estar entre -180 y 180")
    @DecimalMax(value = "180", message = "debe estar entre -180 y 180")
    private Double longitud;

    @NotNull(message = "es obligatorio")
    @Min(value = 1, message = "debe ser al menos 1")
    private Integer capacidad;

    @NotBlank(message = "es obligatorio")
    @Email(message = "debe ser un correo electrónico válido")
    private String email;

    @NotBlank(message = "es obligatorio")
    private String numeroContacto;

    public RefugioSolicitudRequestDTO() {}

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
    public Double getLatitud() { return latitud; }
    public void setLatitud(Double latitud) { this.latitud = latitud; }
    public Double getLongitud() { return longitud; }
    public void setLongitud(Double longitud) { this.longitud = longitud; }
    public Integer getCapacidad() { return capacidad; }
    public void setCapacidad(Integer capacidad) { this.capacidad = capacidad; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getNumeroContacto() { return numeroContacto; }
    public void setNumeroContacto(String numeroContacto) { this.numeroContacto = numeroContacto; }
}
