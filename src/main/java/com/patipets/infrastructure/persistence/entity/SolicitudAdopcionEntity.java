package com.patipets.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "solicitudes_adopcion")
public class SolicitudAdopcionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "animal_id", nullable = false)
    private Long animalId;

    @Column(name = "adoptante_id", nullable = false)
    private Long adoptanteId;

    @Column(name = "refugio_id", nullable = false)
    private Long refugioId;

    @Column(nullable = false)
    private String estado;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @Column(name = "nombre_completo", nullable = false)
    private String nombreCompleto;

    @Column(name = "numero_contacto", nullable = false)
    private String numeroContacto;

    @Column(nullable = false)
    private String direccion;

    @Column(name = "nivel_actividad", nullable = false)
    private String nivelActividad;

    @Column(name = "horas_solo", nullable = false)
    private Integer horasSolo;

    @Column(name = "cuidado_vacaciones", nullable = false)
    private String cuidadoVacaciones;

    @Column(name = "tipo_vivienda", nullable = false)
    private String tipoVivienda;

    @Column(name = "descripcion_espacio", columnDefinition = "TEXT")
    private String descripcionEspacio;

    @Column(name = "tiene_ninos", nullable = false)
    private Boolean tieneNinos;

    @Column(name = "tiene_otras_mascotas", nullable = false)
    private Boolean tieneOtrasMascotas;

    @Column(name = "detalle_mascotas", columnDefinition = "TEXT")
    private String detalleMascotas;

    public SolicitudAdopcionEntity() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getAnimalId() { return animalId; }
    public void setAnimalId(Long animalId) { this.animalId = animalId; }
    public Long getAdoptanteId() { return adoptanteId; }
    public void setAdoptanteId(Long adoptanteId) { this.adoptanteId = adoptanteId; }
    public Long getRefugioId() { return refugioId; }
    public void setRefugioId(Long refugioId) { this.refugioId = refugioId; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    public String getNumeroContacto() { return numeroContacto; }
    public void setNumeroContacto(String numeroContacto) { this.numeroContacto = numeroContacto; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getNivelActividad() { return nivelActividad; }
    public void setNivelActividad(String nivelActividad) { this.nivelActividad = nivelActividad; }
    public Integer getHorasSolo() { return horasSolo; }
    public void setHorasSolo(Integer horasSolo) { this.horasSolo = horasSolo; }
    public String getCuidadoVacaciones() { return cuidadoVacaciones; }
    public void setCuidadoVacaciones(String cuidadoVacaciones) { this.cuidadoVacaciones = cuidadoVacaciones; }
    public String getTipoVivienda() { return tipoVivienda; }
    public void setTipoVivienda(String tipoVivienda) { this.tipoVivienda = tipoVivienda; }
    public String getDescripcionEspacio() { return descripcionEspacio; }
    public void setDescripcionEspacio(String descripcionEspacio) { this.descripcionEspacio = descripcionEspacio; }
    public Boolean getTieneNinos() { return tieneNinos; }
    public void setTieneNinos(Boolean tieneNinos) { this.tieneNinos = tieneNinos; }
    public Boolean getTieneOtrasMascotas() { return tieneOtrasMascotas; }
    public void setTieneOtrasMascotas(Boolean tieneOtrasMascotas) { this.tieneOtrasMascotas = tieneOtrasMascotas; }
    public String getDetalleMascotas() { return detalleMascotas; }
    public void setDetalleMascotas(String detalleMascotas) { this.detalleMascotas = detalleMascotas; }
}
