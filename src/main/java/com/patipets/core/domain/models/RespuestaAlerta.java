package com.patipets.core.domain.models;

import com.patipets.core.domain.enums.TipoAyudaVoluntariado;
import java.time.LocalDateTime;

public class RespuestaAlerta {
    private final Long id;
    private final Long alertaId;
    private final Long usuarioId;
    private final TipoAyudaVoluntariado tipoAyuda;
    private final String mensaje;
    private final String disponibilidad;
    private final LocalDateTime createdAt;

    public RespuestaAlerta(Long id, Long alertaId, Long usuarioId,
                            TipoAyudaVoluntariado tipoAyuda,
                            String mensaje, LocalDateTime createdAt) {
        this(id, alertaId, usuarioId, tipoAyuda, mensaje, null, createdAt);
    }

    public RespuestaAlerta(Long id, Long alertaId, Long usuarioId,
                            TipoAyudaVoluntariado tipoAyuda,
                            String mensaje, String disponibilidad,
                            LocalDateTime createdAt) {
        this.id = id;
        this.alertaId = alertaId;
        this.usuarioId = usuarioId;
        this.tipoAyuda = tipoAyuda;
        this.mensaje = mensaje;
        this.disponibilidad = disponibilidad;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public Long getAlertaId() { return alertaId; }
    public Long getUsuarioId() { return usuarioId; }
    public TipoAyudaVoluntariado getTipoAyuda() { return tipoAyuda; }
    public String getMensaje() { return mensaje; }
    public String getDisponibilidad() { return disponibilidad; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
