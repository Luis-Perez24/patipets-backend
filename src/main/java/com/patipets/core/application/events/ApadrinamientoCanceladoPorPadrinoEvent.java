package com.patipets.core.application.events;

public class ApadrinamientoCanceladoPorPadrinoEvent {
    private final Long apadrinamientoId;
    private final Long animalId;
    private final Long refugioId;
    private final Long padrinoId;
    private final String animalNombre;
    private final String motivo;

    public ApadrinamientoCanceladoPorPadrinoEvent(Long apadrinamientoId, Long animalId, Long refugioId,
                                                   Long padrinoId, String animalNombre, String motivo) {
        this.apadrinamientoId = apadrinamientoId;
        this.animalId = animalId;
        this.refugioId = refugioId;
        this.padrinoId = padrinoId;
        this.animalNombre = animalNombre;
        this.motivo = motivo;
    }

    public Long getApadrinamientoId() { return apadrinamientoId; }
    public Long getAnimalId() { return animalId; }
    public Long getRefugioId() { return refugioId; }
    public Long getPadrinoId() { return padrinoId; }
    public String getAnimalNombre() { return animalNombre; }
    public String getMotivo() { return motivo; }
}
