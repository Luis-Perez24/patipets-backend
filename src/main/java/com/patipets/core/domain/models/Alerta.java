package com.patipets.core.domain.models;

import com.patipets.core.domain.enums.NivelUrgencia;
import java.time.LocalDateTime;

public class Alerta {
    private final Long id;
    private final String titulo;
    private final String descripcion;
    private final NivelUrgencia nivelUrgencia;
    private final Long refugioId;
    private final Long creadoPor;
    private final boolean activa;
    private final LocalDateTime createdAt;

    public Alerta(Long id, String titulo, String descripcion, NivelUrgencia nivelUrgencia,
                  Long refugioId, Long creadoPor, boolean activa, LocalDateTime createdAt) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.nivelUrgencia = nivelUrgencia;
        this.refugioId = refugioId;
        this.creadoPor = creadoPor;
        this.activa = activa;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
    public NivelUrgencia getNivelUrgencia() { return nivelUrgencia; }
    public Long getRefugioId() { return refugioId; }
    public Long getCreadoPor() { return creadoPor; }
    public boolean isActiva() { return activa; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
