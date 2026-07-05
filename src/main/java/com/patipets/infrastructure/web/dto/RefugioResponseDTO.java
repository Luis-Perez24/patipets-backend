package com.patipets.infrastructure.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.patipets.core.domain.models.Refugio;
import java.time.LocalDateTime;

public class RefugioResponseDTO {

    private Long id;
    private String nombre;
    private String direccion;
    private String region;
    private String comuna;
    private Double latitud;
    private Double longitud;
    private Integer capacidad;
    private String email;

    @JsonProperty("numero_contacto")
    private String numeroContacto;

    private String estado;
    private String foto;

    @JsonProperty("usuario_id")
    private Long usuarioId;

    @JsonProperty("usuario_nombre")
    private String usuarioNombre;

    @JsonProperty("fecha_creacion")
    private LocalDateTime fechaCreacion;

    public static RefugioResponseDTO fromDomain(Refugio refugio) {
        RefugioResponseDTO dto = new RefugioResponseDTO();
        dto.id = refugio.getId();
        dto.nombre = refugio.getNombre();
        dto.direccion = refugio.getDireccion();
        dto.region = refugio.getRegion();
        dto.comuna = refugio.getComuna();
        dto.latitud = refugio.getLatitud();
        dto.longitud = refugio.getLongitud();
        dto.capacidad = refugio.getCapacidad();
        dto.email = refugio.getEmail();
        dto.numeroContacto = refugio.getNumeroContacto();
        dto.estado = refugio.getEstado().name();
        dto.foto = refugio.getFoto();
        dto.usuarioId = refugio.getUsuarioId();
        dto.usuarioNombre = refugio.getUsuarioNombre();
        dto.fechaCreacion = refugio.getFechaCreacion();
        return dto;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDireccion() { return direccion; }
    public String getRegion() { return region; }
    public String getComuna() { return comuna; }
    public Double getLatitud() { return latitud; }
    public Double getLongitud() { return longitud; }
    public Integer getCapacidad() { return capacidad; }
    public String getEmail() { return email; }
    public String getNumeroContacto() { return numeroContacto; }
    public String getEstado() { return estado; }
    public String getFoto() { return foto; }
    public Long getUsuarioId() { return usuarioId; }
    public String getUsuarioNombre() { return usuarioNombre; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
}
