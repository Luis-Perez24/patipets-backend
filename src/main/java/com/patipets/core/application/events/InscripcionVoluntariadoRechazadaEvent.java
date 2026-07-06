package com.patipets.core.application.events;

public class InscripcionVoluntariadoRechazadaEvent {
    private final Long inscripcionId;
    private final Long voluntarioId;
    private final Long alertaId;
    private final String alertaTitulo;
    private final String motivo;

    public InscripcionVoluntariadoRechazadaEvent(Long inscripcionId, Long voluntarioId,
                                                  Long alertaId, String alertaTitulo,
                                                  String motivo) {
        this.inscripcionId = inscripcionId;
        this.voluntarioId = voluntarioId;
        this.alertaId = alertaId;
        this.alertaTitulo = alertaTitulo;
        this.motivo = motivo;
    }

    public Long getInscripcionId() { return inscripcionId; }
    public Long getVoluntarioId() { return voluntarioId; }
    public Long getAlertaId() { return alertaId; }
    public String getAlertaTitulo() { return alertaTitulo; }
    public String getMotivo() { return motivo; }
}
