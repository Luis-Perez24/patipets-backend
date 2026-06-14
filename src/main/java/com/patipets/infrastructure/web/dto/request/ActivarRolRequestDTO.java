package com.patipets.infrastructure.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class ActivarRolRequestDTO {

    @NotBlank(message = "es obligatorio")
    @Pattern(
        regexp = "^(CIUDADANO|REFUGIO|VOLUNTARIO|PADRINO)$",
        message = "debe ser uno de: CIUDADANO, REFUGIO, VOLUNTARIO, PADRINO"
    )
    private String rol;

    public ActivarRolRequestDTO() {}

    public ActivarRolRequestDTO(String rol) {
        this.rol = rol;
    }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
}
