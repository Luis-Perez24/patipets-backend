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

    public AlertaRequestDTO() {}

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getNivelUrgencia() { return nivelUrgencia; }
    public void setNivelUrgencia(String nivelUrgencia) { this.nivelUrgencia = nivelUrgencia; }
    public Long getRefugioId() { return refugioId; }
    public void setRefugioId(Long refugioId) { this.refugioId = refugioId; }
}
