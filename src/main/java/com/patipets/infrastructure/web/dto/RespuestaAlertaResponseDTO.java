package com.patipets.infrastructure.web.dto;

import com.patipets.core.domain.models.RespuestaAlerta;

public class RespuestaAlertaResponseDTO {

    private Long id;
    private Long alertaId;
    private String alertaTitulo;
    private Long usuarioId;
    private String usuarioNombre;
    private String tipoAyuda;
    private String mensaje;
    private String disponibilidad;
    private String createdAt;

    public static RespuestaAlertaResponseDTO fromDomain(RespuestaAlerta respuesta) {
        RespuestaAlertaResponseDTO dto = new RespuestaAlertaResponseDTO();
        dto.id = respuesta.getId();
        dto.alertaId = respuesta.getAlertaId();
        dto.usuarioId = respuesta.getUsuarioId();
        dto.tipoAyuda = respuesta.getTipoAyuda().name();
        dto.mensaje = respuesta.getMensaje();
        dto.disponibilidad = respuesta.getDisponibilidad();
        dto.createdAt = respuesta.getCreatedAt() != null ? respuesta.getCreatedAt().toString() : null;
        return dto;
    }

    public Long getId() { return id; }
    public Long getAlertaId() { return alertaId; }
    public String getAlertaTitulo() { return alertaTitulo; }
    public void setAlertaTitulo(String alertaTitulo) { this.alertaTitulo = alertaTitulo; }
    public Long getUsuarioId() { return usuarioId; }
    public String getUsuarioNombre() { return usuarioNombre; }
    public void setUsuarioNombre(String usuarioNombre) { this.usuarioNombre = usuarioNombre; }
    public String getTipoAyuda() { return tipoAyuda; }
    public String getMensaje() { return mensaje; }
    public String getDisponibilidad() { return disponibilidad; }
    public String getCreatedAt() { return createdAt; }
}
