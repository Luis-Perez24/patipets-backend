package com.patipets.infrastructure.web.dto;

import com.patipets.core.domain.models.Apadrinamiento;

public class ApadrinamientoResponseDTO {

    private Long id;
    private Long padrinoId;
    private Long animalId;
    private Long refugioId;
    private String tipoApoyo;
    private String compromiso;
    private String fechaInicio;
    private String animalNombre;
    private String refugioNombre;
    private boolean activo;

    public static ApadrinamientoResponseDTO fromDomain(Apadrinamiento a) {
        ApadrinamientoResponseDTO dto = new ApadrinamientoResponseDTO();
        dto.id = a.getId();
        dto.padrinoId = a.getPadrinoId();
        dto.animalId = a.getAnimalId();
        dto.refugioId = a.getRefugioId();
        dto.tipoApoyo = a.getTipoApoyo().name();
        dto.compromiso = a.getCompromiso();
        dto.fechaInicio = a.getFechaInicio() != null ? a.getFechaInicio().toString() : null;
        dto.activo = a.isActivo();
        return dto;
    }

    public Long getId() { return id; }
    public Long getPadrinoId() { return padrinoId; }
    public Long getAnimalId() { return animalId; }
    public Long getRefugioId() { return refugioId; }
    public String getTipoApoyo() { return tipoApoyo; }
    public String getCompromiso() { return compromiso; }
    public String getFechaInicio() { return fechaInicio; }
    public String getAnimalNombre() { return animalNombre; }
    public void setAnimalNombre(String animalNombre) { this.animalNombre = animalNombre; }
    public String getRefugioNombre() { return refugioNombre; }
    public void setRefugioNombre(String refugioNombre) { this.refugioNombre = refugioNombre; }
    public boolean isActivo() { return activo; }
}
