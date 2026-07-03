package com.patipets.core.domain.models;

import com.patipets.core.domain.enums.TipoNotificacion;
import java.time.LocalDateTime;

public class Notificacion {
    private final Long id;
    private final Long usuarioId;
    private final TipoNotificacion tipo;
    private final String titulo;
    private final String mensaje;
    private final Long entidadRelacionadaId;
    private final boolean leida;
    private final LocalDateTime fechaCreacion;

    public Notificacion(Long id, Long usuarioId, TipoNotificacion tipo, String titulo, String mensaje,
                         Long entidadRelacionadaId, boolean leida, LocalDateTime fechaCreacion) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.tipo = tipo;
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.entidadRelacionadaId = entidadRelacionadaId;
        this.leida = leida;
        this.fechaCreacion = fechaCreacion;
    }

    public Long getId() { return id; }
    public Long getUsuarioId() { return usuarioId; }
    public TipoNotificacion getTipo() { return tipo; }
    public String getTitulo() { return titulo; }
    public String getMensaje() { return mensaje; }
    public Long getEntidadRelacionadaId() { return entidadRelacionadaId; }
    public boolean isLeida() { return leida; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
}
