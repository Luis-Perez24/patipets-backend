package com.patipets.infrastructure.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.patipets.core.domain.models.RespuestaAlerta;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RespuestaAlertaResponseDTO {

    private Long id;
    private Long alertaId;
    private String alertaTitulo;
    private Long refugioId;
    private String refugioNombre;
    private Long usuarioId;
    private String usuarioNombre;
    private String tipoAyuda;
    private String mensaje;
    private String disponibilidad;
    private String createdAt;
    private String estado;
    private String fechaCancelacion;
    private String motivoCancelacion;

    public static RespuestaAlertaResponseDTO fromDomain(RespuestaAlerta respuesta) {
        RespuestaAlertaResponseDTO dto = new RespuestaAlertaResponseDTO();
        dto.id = respuesta.getId();
        dto.alertaId = respuesta.getAlertaId();
        dto.usuarioId = respuesta.getUsuarioId();
        dto.tipoAyuda = respuesta.getTipoAyuda() != null ? respuesta.getTipoAyuda().name() : null;
        dto.mensaje = respuesta.getMensaje();
        dto.disponibilidad = respuesta.getDisponibilidad();
        dto.createdAt = respuesta.getCreatedAt() != null ? respuesta.getCreatedAt().toString() : null;
        dto.estado = respuesta.getEstado();
        dto.fechaCancelacion = respuesta.getFechaCancelacion() != null
                ? respuesta.getFechaCancelacion().toString() : null;
        dto.motivoCancelacion = respuesta.getMotivoCancelacion();
        return dto;
    }

    public Long getId() { return id; }
    public Long getAlertaId() { return alertaId; }
    public String getAlertaTitulo() { return alertaTitulo; }
    public void setAlertaTitulo(String alertaTitulo) { this.alertaTitulo = alertaTitulo; }
    public Long getRefugioId() { return refugioId; }
    public void setRefugioId(Long refugioId) { this.refugioId = refugioId; }
    public String getRefugioNombre() { return refugioNombre; }
    public void setRefugioNombre(String refugioNombre) { this.refugioNombre = refugioNombre; }
    public Long getUsuarioId() { return usuarioId; }
    public String getUsuarioNombre() { return usuarioNombre; }
    public void setUsuarioNombre(String usuarioNombre) { this.usuarioNombre = usuarioNombre; }
    public String getTipoAyuda() { return tipoAyuda; }
    public String getMensaje() { return mensaje; }
    public String getDisponibilidad() { return disponibilidad; }
    public String getCreatedAt() { return createdAt; }
    public String getEstado() { return estado; }
    public String getFechaCancelacion() { return fechaCancelacion; }
    public String getMotivoCancelacion() { return motivoCancelacion; }
}
