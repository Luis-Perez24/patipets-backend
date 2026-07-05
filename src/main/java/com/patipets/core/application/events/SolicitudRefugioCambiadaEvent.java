package com.patipets.core.application.events;

public class SolicitudRefugioCambiadaEvent {
    private final Long refugioId;
    private final Long usuarioId;
    private final String nombreRefugio;
    private final String nuevoEstado;

    public SolicitudRefugioCambiadaEvent(Long refugioId, Long usuarioId, String nombreRefugio, String nuevoEstado) {
        this.refugioId = refugioId;
        this.usuarioId = usuarioId;
        this.nombreRefugio = nombreRefugio;
        this.nuevoEstado = nuevoEstado;
    }

    public Long getRefugioId() { return refugioId; }
    public Long getUsuarioId() { return usuarioId; }
    public String getNombreRefugio() { return nombreRefugio; }
    public String getNuevoEstado() { return nuevoEstado; }
}
