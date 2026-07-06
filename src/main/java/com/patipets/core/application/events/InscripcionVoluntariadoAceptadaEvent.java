package com.patipets.core.application.events;

public class InscripcionVoluntariadoAceptadaEvent {
    private final Long inscripcionId;
    private final Long voluntarioId;
    private final Long alertaId;
    private final String alertaTitulo;

    public InscripcionVoluntariadoAceptadaEvent(Long inscripcionId, Long voluntarioId,
                                                Long alertaId, String alertaTitulo) {
        this.inscripcionId = inscripcionId;
        this.voluntarioId = voluntarioId;
        this.alertaId = alertaId;
        this.alertaTitulo = alertaTitulo;
    }

    public Long getInscripcionId() { return inscripcionId; }
    public Long getVoluntarioId() { return voluntarioId; }
    public Long getAlertaId() { return alertaId; }
    public String getAlertaTitulo() { return alertaTitulo; }
}
