package com.patipets.core.application.events;

public class EstadoSolicitudCambiadoEvent {
    private final Long solicitudId;
    private final Long adoptanteId;
    private final Long animalId;
    private final String nuevoEstado;

    public EstadoSolicitudCambiadoEvent(Long solicitudId, Long adoptanteId, Long animalId, String nuevoEstado) {
        this.solicitudId = solicitudId;
        this.adoptanteId = adoptanteId;
        this.animalId = animalId;
        this.nuevoEstado = nuevoEstado;
    }

    public Long getSolicitudId() { return solicitudId; }
    public Long getAdoptanteId() { return adoptanteId; }
    public Long getAnimalId() { return animalId; }
    public String getNuevoEstado() { return nuevoEstado; }
}
