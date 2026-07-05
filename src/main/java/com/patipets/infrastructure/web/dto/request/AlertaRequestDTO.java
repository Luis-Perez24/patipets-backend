package com.patipets.infrastructure.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AlertaRequestDTO {

    @NotBlank(message = "es obligatorio")
    private String titulo;

    private String descripcion;

    @NotBlank(message = "es obligatorio")
    private String nivelUrgencia;

    @NotNull(message = "es obligatorio")
    private Long refugioId;

    private String tipoAyuda;

    private String fecha;

    private String perfilRequerido;

    public AlertaRequestDTO() {}

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getNivelUrgencia() { return nivelUrgencia; }
    public void setNivelUrgencia(String nivelUrgencia) { this.nivelUrgencia = nivelUrgencia; }
    public Long getRefugioId() { return refugioId; }
    public void setRefugioId(Long refugioId) { this.refugioId = refugioId; }
    public String getTipoAyuda() { return tipoAyuda; }
    public void setTipoAyuda(String tipoAyuda) { this.tipoAyuda = tipoAyuda; }
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public String getPerfilRequerido() { return perfilRequerido; }
    public void setPerfilRequerido(String perfilRequerido) { this.perfilRequerido = perfilRequerido; }
}
