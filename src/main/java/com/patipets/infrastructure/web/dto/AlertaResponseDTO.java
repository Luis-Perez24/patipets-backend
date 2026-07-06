package com.patipets.infrastructure.web.dto;

import com.patipets.core.domain.models.Alerta;

public class AlertaResponseDTO {

    private Long id;
    private String titulo;
    private String descripcion;
    private String nivelUrgencia;
    private Long refugioId;
    private String refugioNombre;
    private Long creadoPor;
    private boolean activa;
    private String createdAt;
    private String tipoAyuda;
    private String fecha;
    private String perfilRequerido;
    private String refugioUbicacion;

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
        dto.tipoAyuda = alerta.getTipoAyuda() != null ? alerta.getTipoAyuda().name() : null;
        dto.fecha = alerta.getFecha() != null ? alerta.getFecha().toString() : null;
        dto.perfilRequerido = alerta.getPerfilRequerido();
        return dto;
    }

    public Long getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
    public String getNivelUrgencia() { return nivelUrgencia; }
    public Long getRefugioId() { return refugioId; }
    public String getRefugioNombre() { return refugioNombre; }
    public void setRefugioNombre(String refugioNombre) { this.refugioNombre = refugioNombre; }
    public String getRefugioUbicacion() { return refugioUbicacion; }
    public void setRefugioUbicacion(String refugioUbicacion) { this.refugioUbicacion = refugioUbicacion; }
    public Long getCreadoPor() { return creadoPor; }
    public boolean isActiva() { return activa; }
    public String getCreatedAt() { return createdAt; }
    public String getTipoAyuda() { return tipoAyuda; }
    public String getFecha() { return fecha; }
    public String getPerfilRequerido() { return perfilRequerido; }
}
