package com.patipets.infrastructure.web.dto;

import com.patipets.core.domain.models.SolicitudAdopcion;

public class SolicitudAdopcionResponseDTO {

    private Long id;
    private Long animalId;
    private Long adoptanteId;
    private Long refugioId;
    private String estado;
    private String fechaCreacion;
    private String fechaActualizacion;
    private String nombreCompleto;
    private String numeroContacto;
    private String direccion;
    private String nivelActividad;
    private Integer horasSolo;
    private String cuidadoVacaciones;
    private String tipoVivienda;
    private String descripcionEspacio;
    private Boolean tieneNinos;
    private Boolean tieneOtrasMascotas;

    public static SolicitudAdopcionResponseDTO fromDomain(SolicitudAdopcion s) {
        SolicitudAdopcionResponseDTO dto = new SolicitudAdopcionResponseDTO();
        dto.id = s.getId();
        dto.animalId = s.getAnimalId();
        dto.adoptanteId = s.getAdoptanteId();
        dto.refugioId = s.getRefugioId();
        dto.estado = s.getEstado().name();
        dto.fechaCreacion = s.getFechaCreacion() != null ? s.getFechaCreacion().toString() : null;
        dto.fechaActualizacion = s.getFechaActualizacion() != null ? s.getFechaActualizacion().toString() : null;
        dto.nombreCompleto = s.getNombreCompleto();
        dto.numeroContacto = s.getNumeroContacto();
        dto.direccion = s.getDireccion();
        dto.nivelActividad = s.getNivelActividad().name();
        dto.horasSolo = s.getHorasSolo();
        dto.cuidadoVacaciones = s.getCuidadoVacaciones().name();
        dto.tipoVivienda = s.getTipoVivienda().name();
        dto.descripcionEspacio = s.getDescripcionEspacio();
        dto.tieneNinos = s.getTieneNinos();
        dto.tieneOtrasMascotas = s.getTieneOtrasMascotas();
        return dto;
    }

    public Long getId() { return id; }
    public Long getAnimalId() { return animalId; }
    public Long getAdoptanteId() { return adoptanteId; }
    public Long getRefugioId() { return refugioId; }
    public String getEstado() { return estado; }
    public String getFechaCreacion() { return fechaCreacion; }
    public String getFechaActualizacion() { return fechaActualizacion; }
    public String getNombreCompleto() { return nombreCompleto; }
    public String getNumeroContacto() { return numeroContacto; }
    public String getDireccion() { return direccion; }
    public String getNivelActividad() { return nivelActividad; }
    public Integer getHorasSolo() { return horasSolo; }
    public String getCuidadoVacaciones() { return cuidadoVacaciones; }
    public String getTipoVivienda() { return tipoVivienda; }
    public String getDescripcionEspacio() { return descripcionEspacio; }
    public Boolean getTieneNinos() { return tieneNinos; }
    public Boolean getTieneOtrasMascotas() { return tieneOtrasMascotas; }
}
