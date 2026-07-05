package com.patipets.infrastructure.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.patipets.core.domain.models.Refugio;

public class RefugioUbicacionDTO {

    private Long id;
    private String nombre;
    private String direccion;
    private String region;

    private Double latitud;
    private Double longitud;

    private String telefono;
    private String email;

    public static RefugioUbicacionDTO fromDomain(Refugio refugio) {
        RefugioUbicacionDTO dto = new RefugioUbicacionDTO();
        dto.id = refugio.getId();
        dto.nombre = refugio.getNombre();
        dto.direccion = refugio.getDireccion();
        dto.region = refugio.getRegion();
        dto.latitud = refugio.getLatitud();
        dto.longitud = refugio.getLongitud();
        dto.telefono = refugio.getNumeroContacto();
        dto.email = refugio.getEmail();
        return dto;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDireccion() { return direccion; }
    public String getRegion() { return region; }
    public Double getLatitud() { return latitud; }
    public Double getLongitud() { return longitud; }
    public String getTelefono() { return telefono; }
    public String getEmail() { return email; }
}
