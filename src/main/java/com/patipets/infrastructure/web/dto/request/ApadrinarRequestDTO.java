package com.patipets.infrastructure.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class ApadrinarRequestDTO {

    @NotNull(message = "es obligatorio")
    private Long animalId;

    @NotBlank(message = "es obligatorio")
    @Pattern(
        regexp = "^(VISITAS|INSUMOS|PASEOS|ATENCION|OTRO)$",
        message = "debe ser uno de: VISITAS, INSUMOS, PASEOS, ATENCION, OTRO"
    )
    private String tipoApoyo;

    @NotBlank(message = "es obligatorio")
    private String compromiso;

    public ApadrinarRequestDTO() {}

    public Long getAnimalId() { return animalId; }
    public void setAnimalId(Long animalId) { this.animalId = animalId; }
    public String getTipoApoyo() { return tipoApoyo; }
    public void setTipoApoyo(String tipoApoyo) { this.tipoApoyo = tipoApoyo; }
    public String getCompromiso() { return compromiso; }
    public void setCompromiso(String compromiso) { this.compromiso = compromiso; }
}
