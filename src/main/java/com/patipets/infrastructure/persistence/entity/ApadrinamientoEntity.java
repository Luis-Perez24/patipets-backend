package com.patipets.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "apadrinamientos")
public class ApadrinamientoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "padrino_id", nullable = false)
    private Long padrinoId;

    @Column(name = "animal_id", nullable = false)
    private Long animalId;

    @Column(name = "refugio_id", nullable = false)
    private Long refugioId;

    @Column(name = "tipo_apoyo", nullable = false)
    private String tipoApoyo;

    @Column(columnDefinition = "TEXT")
    private String compromiso;

    @Column(name = "fecha_inicio")
    private LocalDateTime fechaInicio;

    @Column(nullable = false)
    private boolean activo = true;

    public ApadrinamientoEntity() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPadrinoId() { return padrinoId; }
    public void setPadrinoId(Long padrinoId) { this.padrinoId = padrinoId; }
    public Long getAnimalId() { return animalId; }
    public void setAnimalId(Long animalId) { this.animalId = animalId; }
    public Long getRefugioId() { return refugioId; }
    public void setRefugioId(Long refugioId) { this.refugioId = refugioId; }
    public String getTipoApoyo() { return tipoApoyo; }
    public void setTipoApoyo(String tipoApoyo) { this.tipoApoyo = tipoApoyo; }
    public String getCompromiso() { return compromiso; }
    public void setCompromiso(String compromiso) { this.compromiso = compromiso; }
    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDateTime fechaInicio) { this.fechaInicio = fechaInicio; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}
