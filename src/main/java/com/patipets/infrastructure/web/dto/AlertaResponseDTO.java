package com.patipets.infrastructure.web.dto;

import com.patipets.core.domain.models.Alerta;

public class AlertaResponseDTO {

    private Long id;
    private String titulo;
    private String descripcion;
    private String nivelUrgencia;
    private Long refugioId;
    private Long creadoPor;
    private boolean activa;
    private String createdAt;

    public static AlertaResponseDTO fromDomain(Alerta alerta) {
        AlertaResponseDTO dto = new AlertaResponseDTO();
        dto.id = alerta.getId();
        dto.titulo = alerta.getTitulo();
        dto.descripcion = alerta.getDescripcion();
        dto.nivelUrgencia = alerta.getNivelUrgencia().name();
        dto.refugioId = alerta.getRefugioId();
        dto.creadoPor = alerta.getCreadoPor();
        dto.activa = alerta.isActiva();
        dto.createdAt = alerta.getCreatedAt() != null ? alerta.getCreatedAt().toString() : null;
        return dto;
    }

    public Long getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
    public String getNivelUrgencia() { return nivelUrgencia; }
    public Long getRefugioId() { return refugioId; }
    public Long getCreadoPor() { return creadoPor; }
    public boolean isActiva() { return activa; }
    public String getCreatedAt() { return createdAt; }
}
