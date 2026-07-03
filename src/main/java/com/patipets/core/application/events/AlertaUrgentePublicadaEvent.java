package com.patipets.core.application.events;

public class AlertaUrgentePublicadaEvent {
    private final Long alertaId;
    private final Long refugioId;
    private final String titulo;
    private final String descripcion;
    private final String nivelUrgencia;

    public AlertaUrgentePublicadaEvent(Long alertaId, Long refugioId, String titulo,
                                        String descripcion, String nivelUrgencia) {
        this.alertaId = alertaId;
        this.refugioId = refugioId;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.nivelUrgencia = nivelUrgencia;
    }

    public Long getAlertaId() { return alertaId; }
    public Long getRefugioId() { return refugioId; }
    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
    public String getNivelUrgencia() { return nivelUrgencia; }
}
