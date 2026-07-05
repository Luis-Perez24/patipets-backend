package com.patipets.infrastructure.web.dto;

import com.patipets.core.domain.models.Apadrinamiento;

public class ApadrinamientoResponseDTO {

    private Long id;
    private Long padrinoId;
    private Long animalId;
    private Long refugioId;
    private String tipoApoyo;
    private String fechaInicio;
    private boolean activo;

    public static ApadrinamientoResponseDTO fromDomain(Apadrinamiento a) {
        ApadrinamientoResponseDTO dto = new ApadrinamientoResponseDTO();
        dto.id = a.getId();
        dto.padrinoId = a.getPadrinoId();
        dto.animalId = a.getAnimalId();
        dto.refugioId = a.getRefugioId();
        dto.tipoApoyo = a.getTipoApoyo().name();
        dto.fechaInicio = a.getFechaInicio() != null ? a.getFechaInicio().toString() : null;
        dto.activo = a.isActivo();
        return dto;
    }

    public Long getId() { return id; }
    public Long getPadrinoId() { return padrinoId; }
    public Long getAnimalId() { return animalId; }
    public Long getRefugioId() { return refugioId; }
    public String getTipoApoyo() { return tipoApoyo; }
    public String getFechaInicio() { return fechaInicio; }
    public boolean isActivo() { return activo; }
}
