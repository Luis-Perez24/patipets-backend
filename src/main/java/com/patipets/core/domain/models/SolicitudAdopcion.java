package com.patipets.core.domain.models;

import com.patipets.core.domain.enums.CuidadoVacaciones;
import com.patipets.core.domain.enums.EstadoPostulacion;
import com.patipets.core.domain.enums.NivelActividad;
import com.patipets.core.domain.enums.TipoVivienda;
import java.time.LocalDateTime;

public class SolicitudAdopcion {
    private final Long id;
    private final Long animalId;
    private final Long adoptanteId;
    private final Long refugioId;
    private final EstadoPostulacion estado;
    private final LocalDateTime fechaCreacion;
    private final LocalDateTime fechaActualizacion;

    private final String nombreCompleto;
    private final String numeroContacto;
    private final String direccion;
    private final NivelActividad nivelActividad;
    private final Integer horasSolo;
    private final CuidadoVacaciones cuidadoVacaciones;
    private final TipoVivienda tipoVivienda;
    private final String descripcionEspacio;
    private final Boolean tieneNinos;
    private final Boolean tieneOtrasMascotas;

    public SolicitudAdopcion(Long id, Long animalId, Long adoptanteId, Long refugioId,
                              EstadoPostulacion estado, LocalDateTime fechaCreacion,
                              LocalDateTime fechaActualizacion, String nombreCompleto,
                              String numeroContacto, String direccion, NivelActividad nivelActividad,
                              Integer horasSolo, CuidadoVacaciones cuidadoVacaciones,
                              TipoVivienda tipoVivienda, String descripcionEspacio,
                              Boolean tieneNinos, Boolean tieneOtrasMascotas) {
        this.id = id;
        this.animalId = animalId;
        this.adoptanteId = adoptanteId;
        this.refugioId = refugioId;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
        this.nombreCompleto = nombreCompleto;
        this.numeroContacto = numeroContacto;
        this.direccion = direccion;
        this.nivelActividad = nivelActividad;
        this.horasSolo = horasSolo;
        this.cuidadoVacaciones = cuidadoVacaciones;
        this.tipoVivienda = tipoVivienda;
        this.descripcionEspacio = descripcionEspacio;
        this.tieneNinos = tieneNinos;
        this.tieneOtrasMascotas = tieneOtrasMascotas;
    }

    public Long getId() { return id; }
    public Long getAnimalId() { return animalId; }
    public Long getAdoptanteId() { return adoptanteId; }
    public Long getRefugioId() { return refugioId; }
    public EstadoPostulacion getEstado() { return estado; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public String getNombreCompleto() { return nombreCompleto; }
    public String getNumeroContacto() { return numeroContacto; }
    public String getDireccion() { return direccion; }
    public NivelActividad getNivelActividad() { return nivelActividad; }
    public Integer getHorasSolo() { return horasSolo; }
    public CuidadoVacaciones getCuidadoVacaciones() { return cuidadoVacaciones; }
    public TipoVivienda getTipoVivienda() { return tipoVivienda; }
    public String getDescripcionEspacio() { return descripcionEspacio; }
    public Boolean getTieneNinos() { return tieneNinos; }
    public Boolean getTieneOtrasMascotas() { return tieneOtrasMascotas; }
}
