package com.patipets.infrastructure.web.dto;

import com.patipets.core.domain.models.Notificacion;

public class NotificacionResponseDTO {

    private Long id;
    private String tipo;
    private String titulo;
    private String mensaje;
    private Long entidadRelacionadaId;
    private boolean leida;
    private String fechaCreacion;

    public static NotificacionResponseDTO fromDomain(Notificacion notificacion) {
        NotificacionResponseDTO dto = new NotificacionResponseDTO();
        dto.id = notificacion.getId();
        dto.tipo = notificacion.getTipo().name();
        dto.titulo = notificacion.getTitulo();
        dto.mensaje = notificacion.getMensaje();
        dto.entidadRelacionadaId = notificacion.getEntidadRelacionadaId();
        dto.leida = notificacion.isLeida();
        dto.fechaCreacion = notificacion.getFechaCreacion() != null ? notificacion.getFechaCreacion().toString() : null;
        return dto;
    }

    public Long getId() { return id; }
    public String getTipo() { return tipo; }
    public String getTitulo() { return titulo; }
    public String getMensaje() { return mensaje; }
    public Long getEntidadRelacionadaId() { return entidadRelacionadaId; }
    public boolean isLeida() { return leida; }
    public String getFechaCreacion() { return fechaCreacion; }
}
