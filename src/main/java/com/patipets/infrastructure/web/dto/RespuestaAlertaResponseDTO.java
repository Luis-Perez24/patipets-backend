package com.patipets.infrastructure.web.dto;

import com.patipets.core.domain.models.RespuestaAlerta;

public class RespuestaAlertaResponseDTO {

    private Long id;
    private Long alertaId;
    private Long usuarioId;
    private String tipoAyuda;
    private String mensaje;
    private String createdAt;

    public static RespuestaAlertaResponseDTO fromDomain(RespuestaAlerta respuesta) {
        RespuestaAlertaResponseDTO dto = new RespuestaAlertaResponseDTO();
        dto.id = respuesta.getId();
        dto.alertaId = respuesta.getAlertaId();
        dto.usuarioId = respuesta.getUsuarioId();
        dto.tipoAyuda = respuesta.getTipoAyuda().name();
        dto.mensaje = respuesta.getMensaje();
        dto.createdAt = respuesta.getCreatedAt() != null ? respuesta.getCreatedAt().toString() : null;
        return dto;
    }

    public Long getId() { return id; }
    public Long getAlertaId() { return alertaId; }
    public Long getUsuarioId() { return usuarioId; }
    public String getTipoAyuda() { return tipoAyuda; }
    public String getMensaje() { return mensaje; }
    public String getCreatedAt() { return createdAt; }
}
