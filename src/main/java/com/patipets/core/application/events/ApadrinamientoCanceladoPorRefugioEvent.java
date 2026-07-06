package com.patipets.core.application.events;

public class ApadrinamientoCanceladoPorRefugioEvent {
    private final Long apadrinamientoId;
    private final Long padrinoId;
    private final Long animalId;
    private final String animalNombre;
    private final String motivo;

    public ApadrinamientoCanceladoPorRefugioEvent(Long apadrinamientoId, Long padrinoId,
                                                   Long animalId, String animalNombre,
                                                   String motivo) {
        this.apadrinamientoId = apadrinamientoId;
        this.padrinoId = padrinoId;
        this.animalId = animalId;
        this.animalNombre = animalNombre;
        this.motivo = motivo;
    }

    public Long getApadrinamientoId() { return apadrinamientoId; }
    public Long getPadrinoId() { return padrinoId; }
    public Long getAnimalId() { return animalId; }
    public String getAnimalNombre() { return animalNombre; }
    public String getMotivo() { return motivo; }
}
