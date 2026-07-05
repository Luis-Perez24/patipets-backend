package com.patipets.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "respuestas_alerta")
public class RespuestaAlertaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "alerta_id", nullable = false)
    private Long alertaId;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(name = "tipo_ayuda", nullable = false)
    private String tipoAyuda;

    @Column(columnDefinition = "TEXT")
    private String mensaje;

    @Column(columnDefinition = "TEXT")
    private String disponibilidad;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public RespuestaAlertaEntity() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getAlertaId() { return alertaId; }
    public void setAlertaId(Long alertaId) { this.alertaId = alertaId; }
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public String getTipoAyuda() { return tipoAyuda; }
    public void setTipoAyuda(String tipoAyuda) { this.tipoAyuda = tipoAyuda; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    public String getDisponibilidad() { return disponibilidad; }
    public void setDisponibilidad(String disponibilidad) { this.disponibilidad = disponibilidad; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
