package com.patipets.core.domain.models;

import com.patipets.core.domain.enums.TipoApoyo;
import java.time.LocalDateTime;

public class Apadrinamiento {
    private final Long id;
    private final Long padrinoId;
    private final Long animalId;
    private final Long refugioId;
    private final TipoApoyo tipoApoyo;
    private final String compromiso;
    private final LocalDateTime fechaInicio;
    private final boolean activo;

    public Apadrinamiento(Long id, Long padrinoId, Long animalId, Long refugioId,
                           TipoApoyo tipoApoyo, String compromiso,
                           LocalDateTime fechaInicio, boolean activo) {
        this.id = id;
        this.padrinoId = padrinoId;
        this.animalId = animalId;
        this.refugioId = refugioId;
        this.tipoApoyo = tipoApoyo;
        this.compromiso = compromiso;
        this.fechaInicio = fechaInicio;
        this.activo = activo;
    }

    public Long getId() { return id; }
    public Long getPadrinoId() { return padrinoId; }
    public Long getAnimalId() { return animalId; }
    public Long getRefugioId() { return refugioId; }
    public TipoApoyo getTipoApoyo() { return tipoApoyo; }
    public String getCompromiso() { return compromiso; }
    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public boolean isActivo() { return activo; }
}
