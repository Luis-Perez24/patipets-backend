package com.patipets.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "alertas")
public class AlertaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "nivel_urgencia", nullable = false)
    private String nivelUrgencia;

    @Column(name = "refugio_id", nullable = false)
    private Long refugioId;

    @Column(name = "creado_por", nullable = false)
    private Long creadoPor;

    @Column(nullable = false)
    private boolean activa = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "tipo_ayuda")
    private String tipoAyuda;

    private LocalDate fecha;

    @Column(name = "perfil_requerido")
    private String perfilRequerido;

    public AlertaEntity() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getNivelUrgencia() { return nivelUrgencia; }
    public void setNivelUrgencia(String nivelUrgencia) { this.nivelUrgencia = nivelUrgencia; }
    public Long getRefugioId() { return refugioId; }
    public void setRefugioId(Long refugioId) { this.refugioId = refugioId; }
    public Long getCreadoPor() { return creadoPor; }
    public void setCreadoPor(Long creadoPor) { this.creadoPor = creadoPor; }
    public boolean isActiva() { return activa; }
    public void setActiva(boolean activa) { this.activa = activa; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public String getTipoAyuda() { return tipoAyuda; }
    public void setTipoAyuda(String tipoAyuda) { this.tipoAyuda = tipoAyuda; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public String getPerfilRequerido() { return perfilRequerido; }
    public void setPerfilRequerido(String perfilRequerido) { this.perfilRequerido = perfilRequerido; }
}
