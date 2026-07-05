package com.patipets.infrastructure.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class ResponderAlertaRequestDTO {

    @NotBlank(message = "es obligatorio")
    @Pattern(
        regexp = "^(PASEOS|LIMPIEZAS|TRANSPORTES|ATENCIONES|OTROS)$",
        message = "debe ser uno de: PASEOS, LIMPIEZAS, TRANSPORTES, ATENCIONES, OTROS"
    )
    private String tipoAyuda;

    private String mensaje;

    private String disponibilidad;

    public ResponderAlertaRequestDTO() {}

    public String getTipoAyuda() { return tipoAyuda; }
    public void setTipoAyuda(String tipoAyuda) { this.tipoAyuda = tipoAyuda; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    public String getDisponibilidad() { return disponibilidad; }
    public void setDisponibilidad(String disponibilidad) { this.disponibilidad = disponibilidad; }
}
