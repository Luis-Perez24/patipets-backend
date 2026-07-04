package com.patipets.infrastructure.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class ResponderAlertaRequestDTO {

    @NotBlank(message = "es obligatorio")
    @Pattern(
        regexp = "^(PASEO|LIMPIEZA|TRANSPORTE|ATENCION|OTRO)$",
        message = "debe ser uno de: PASEO, LIMPIEZA, TRANSPORTE, ATENCION, OTRO"
    )
    private String tipoAyuda;

    private String mensaje;

    public ResponderAlertaRequestDTO() {}

    public String getTipoAyuda() { return tipoAyuda; }
    public void setTipoAyuda(String tipoAyuda) { this.tipoAyuda = tipoAyuda; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
}
