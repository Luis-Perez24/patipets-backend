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
    private final String estado;
    private final LocalDateTime fechaCancelacion;
    private final String motivoCancelacion;

    public RespuestaAlerta(Long id, Long alertaId, Long usuarioId,
                            TipoAyudaVoluntariado tipoAyuda,
                            String mensaje, LocalDateTime createdAt) {
        this(id, alertaId, usuarioId, tipoAyuda, mensaje, null, createdAt,
                "ACTIVA", null, null);
    }

    public RespuestaAlerta(Long id, Long alertaId, Long usuarioId,
                            TipoAyudaVoluntariado tipoAyuda,
                            String mensaje, String disponibilidad,
                            LocalDateTime createdAt) {
        this(id, alertaId, usuarioId, tipoAyuda, mensaje, disponibilidad, createdAt,
                "ACTIVA", null, null);
    }

    public RespuestaAlerta(Long id, Long alertaId, Long usuarioId,
                            TipoAyudaVoluntariado tipoAyuda,
                            String mensaje, String disponibilidad,
                            LocalDateTime createdAt,
                            String estado, LocalDateTime fechaCancelacion,
                            String motivoCancelacion) {
        this.id = id;
        this.alertaId = alertaId;
        this.usuarioId = usuarioId;
        this.tipoAyuda = tipoAyuda;
        this.mensaje = mensaje;
        this.disponibilidad = disponibilidad;
        this.createdAt = createdAt;
        this.estado = estado;
        this.fechaCancelacion = fechaCancelacion;
        this.motivoCancelacion = motivoCancelacion;
    }

    public Long getId() { return id; }
    public Long getAlertaId() { return alertaId; }
    public Long getUsuarioId() { return usuarioId; }
    public TipoAyudaVoluntariado getTipoAyuda() { return tipoAyuda; }
    public String getMensaje() { return mensaje; }
    public String getDisponibilidad() { return disponibilidad; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getEstado() { return estado; }
    public LocalDateTime getFechaCancelacion() { return fechaCancelacion; }
    public String getMotivoCancelacion() { return motivoCancelacion; }
}
