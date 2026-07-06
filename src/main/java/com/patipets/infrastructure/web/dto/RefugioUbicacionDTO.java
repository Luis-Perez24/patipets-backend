package com.patipets.infrastructure.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.patipets.core.domain.models.Refugio;
import java.time.LocalDateTime;

public class RefugioUbicacionDTO {

    private Long id;
    private String nombre;
    private String descripcion;
    private String direccion;
    private String region;

    private Double latitud;
    private Double longitud;

    private String telefono;
    private String email;

    private String foto;

    private Integer capacidad;

    private String comuna;

    @JsonProperty("fecha_creacion")
    private LocalDateTime fechaCreacion;

    public static RefugioUbicacionDTO fromDomain(Refugio refugio) {
        RefugioUbicacionDTO dto = new RefugioUbicacionDTO();
        dto.id = refugio.getId();
        dto.nombre = refugio.getNombre();
        dto.descripcion = refugio.getDescripcion();
        dto.direccion = refugio.getDireccion();
        dto.region = refugio.getRegion();
        dto.latitud = refugio.getLatitud();
        dto.longitud = refugio.getLongitud();
        dto.telefono = refugio.getNumeroContacto();
        dto.email = refugio.getEmail();
        dto.foto = refugio.getFoto();
        dto.capacidad = refugio.getCapacidad();
        dto.comuna = refugio.getComuna();
        dto.fechaCreacion = refugio.getFechaCreacion();
        return dto;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public String getDireccion() { return direccion; }
    public String getRegion() { return region; }
    public Double getLatitud() { return latitud; }
    public Double getLongitud() { return longitud; }
    public String getTelefono() { return telefono; }
    public String getEmail() { return email; }
    public String getFoto() { return foto; }
    public Integer getCapacidad() { return capacidad; }
    public String getComuna() { return comuna; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
}
