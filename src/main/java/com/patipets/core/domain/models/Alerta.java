package com.patipets.core.domain.models;

import com.patipets.core.domain.enums.NivelUrgencia;
import com.patipets.core.domain.enums.TipoAyudaVoluntariado;
import java.time.LocalDate;
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
    private final TipoAyudaVoluntariado tipoAyuda;
    private final LocalDate fecha;
    private final String perfilRequerido;

    public Alerta(Long id, String titulo, String descripcion, NivelUrgencia nivelUrgencia,
                  Long refugioId, Long creadoPor, boolean activa, LocalDateTime createdAt) {
        this(id, titulo, descripcion, nivelUrgencia, refugioId, creadoPor, activa, createdAt, null, null, null);
    }

    public Alerta(Long id, String titulo, String descripcion, NivelUrgencia nivelUrgencia,
                  Long refugioId, Long creadoPor, boolean activa, LocalDateTime createdAt,
                  TipoAyudaVoluntariado tipoAyuda, LocalDate fecha, String perfilRequerido) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.nivelUrgencia = nivelUrgencia;
        this.refugioId = refugioId;
        this.creadoPor = creadoPor;
        this.activa = activa;
        this.createdAt = createdAt;
        this.tipoAyuda = tipoAyuda;
        this.fecha = fecha;
        this.perfilRequerido = perfilRequerido;
    }

    public Long getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
    public NivelUrgencia getNivelUrgencia() { return nivelUrgencia; }
    public Long getRefugioId() { return refugioId; }
    public Long getCreadoPor() { return creadoPor; }
    public boolean isActiva() { return activa; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public TipoAyudaVoluntariado getTipoAyuda() { return tipoAyuda; }
    public LocalDate getFecha() { return fecha; }
    public String getPerfilRequerido() { return perfilRequerido; }
}
