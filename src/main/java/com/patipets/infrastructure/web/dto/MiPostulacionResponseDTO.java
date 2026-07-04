package com.patipets.infrastructure.web.dto;

import com.patipets.core.domain.models.Animal;
import com.patipets.core.domain.models.Refugio;
import com.patipets.core.domain.models.SolicitudAdopcion;

public class MiPostulacionResponseDTO {

    private Long id;
    private String estado;
    private String fechaCreacion;
    private String fechaActualizacion;
    private Long animalId;
    private String animalNombre;
    private String animalFoto;
    private Long refugioId;
    private String refugioNombre;
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

    public static MiPostulacionResponseDTO fromDomain(SolicitudAdopcion s, Animal animal, Refugio refugio) {
        MiPostulacionResponseDTO dto = new MiPostulacionResponseDTO();
        dto.id = s.getId();
        dto.estado = s.getEstado().name();
        dto.fechaCreacion = s.getFechaCreacion() != null ? s.getFechaCreacion().toString() : null;
        dto.fechaActualizacion = s.getFechaActualizacion() != null ? s.getFechaActualizacion().toString() : null;
        dto.animalId = s.getAnimalId();
        dto.animalNombre = animal != null ? animal.getNombre() : null;
        dto.animalFoto = (animal != null && animal.getFotos() != null && !animal.getFotos().isEmpty())
                ? animal.getFotos().get(0) : null;
        dto.refugioId = s.getRefugioId();
        dto.refugioNombre = refugio != null ? refugio.getNombre() : null;
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
    public String getEstado() { return estado; }
    public String getFechaCreacion() { return fechaCreacion; }
    public String getFechaActualizacion() { return fechaActualizacion; }
    public Long getAnimalId() { return animalId; }
    public String getAnimalNombre() { return animalNombre; }
    public String getAnimalFoto() { return animalFoto; }
    public Long getRefugioId() { return refugioId; }
    public String getRefugioNombre() { return refugioNombre; }
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
