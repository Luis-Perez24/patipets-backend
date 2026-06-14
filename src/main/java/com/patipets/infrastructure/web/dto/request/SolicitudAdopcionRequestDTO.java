package com.patipets.infrastructure.web.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SolicitudAdopcionRequestDTO {

    @NotNull(message = "es obligatorio")
    private Long animalId;

    @NotBlank(message = "es obligatorio")
    private String nombreCompleto;

    @NotBlank(message = "es obligatorio")
    private String numeroContacto;

    @NotBlank(message = "es obligatorio")
    private String direccion;

    @NotBlank(message = "es obligatorio")
    private String nivelActividad;

    @NotNull(message = "es obligatorio")
    @Min(value = 0, message = "no puede ser negativo")
    private Integer horasSolo;

    @NotBlank(message = "es obligatorio")
    private String cuidadoVacaciones;

    @NotBlank(message = "es obligatorio")
    private String tipoVivienda;

    private String descripcionEspacio;

    @NotNull(message = "es obligatorio")
    private Boolean tieneNinos;

    @NotNull(message = "es obligatorio")
    private Boolean tieneOtrasMascotas;

    public SolicitudAdopcionRequestDTO() {}

    public Long getAnimalId() { return animalId; }
    public void setAnimalId(Long animalId) { this.animalId = animalId; }
    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    public String getNumeroContacto() { return numeroContacto; }
    public void setNumeroContacto(String numeroContacto) { this.numeroContacto = numeroContacto; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getNivelActividad() { return nivelActividad; }
    public void setNivelActividad(String nivelActividad) { this.nivelActividad = nivelActividad; }
    public Integer getHorasSolo() { return horasSolo; }
    public void setHorasSolo(Integer horasSolo) { this.horasSolo = horasSolo; }
    public String getCuidadoVacaciones() { return cuidadoVacaciones; }
    public void setCuidadoVacaciones(String cuidadoVacaciones) { this.cuidadoVacaciones = cuidadoVacaciones; }
    public String getTipoVivienda() { return tipoVivienda; }
    public void setTipoVivienda(String tipoVivienda) { this.tipoVivienda = tipoVivienda; }
    public String getDescripcionEspacio() { return descripcionEspacio; }
    public void setDescripcionEspacio(String descripcionEspacio) { this.descripcionEspacio = descripcionEspacio; }
    public Boolean getTieneNinos() { return tieneNinos; }
    public void setTieneNinos(Boolean tieneNinos) { this.tieneNinos = tieneNinos; }
    public Boolean getTieneOtrasMascotas() { return tieneOtrasMascotas; }
    public void setTieneOtrasMascotas(Boolean tieneOtrasMascotas) { this.tieneOtrasMascotas = tieneOtrasMascotas; }
}
