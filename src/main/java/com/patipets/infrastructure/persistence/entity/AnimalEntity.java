package com.patipets.infrastructure.persistence.entity;

import com.patipets.core.domain.enums.EstadoAnimal;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "animales")
public class AnimalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String especie;

    private String raza;

    private Integer edad;

    private String tamano;

    private String personalidad;

    @Column(name = "estado_salud")
    private String estadoSalud;

    @Column(columnDefinition = "TEXT")
    private String historia;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_adopcion", nullable = false)
    private EstadoAnimal estadoAdopcion;

    @Column(name = "refugio_id", nullable = false)
    private Long refugioId;

    @ElementCollection
    @CollectionTable(name = "animal_fotos", joinColumns = @JoinColumn(name = "animal_id"))
    @Column(name = "url_foto")
    private List<String> fotos;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;

    public AnimalEntity() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEspecie() { return especie; }
    public void setEspecie(String especie) { this.especie = especie; }
    public String getRaza() { return raza; }
    public void setRaza(String raza) { this.raza = raza; }
    public Integer getEdad() { return edad; }
    public void setEdad(Integer edad) { this.edad = edad; }
    public String getTamano() { return tamano; }
    public void setTamano(String tamano) { this.tamano = tamano; }
    public String getPersonalidad() { return personalidad; }
    public void setPersonalidad(String personalidad) { this.personalidad = personalidad; }
    public String getEstadoSalud() { return estadoSalud; }
    public void setEstadoSalud(String estadoSalud) { this.estadoSalud = estadoSalud; }
    public String getHistoria() { return historia; }
    public void setHistoria(String historia) { this.historia = historia; }
    public EstadoAnimal getEstadoAdopcion() { return estadoAdopcion; }
    public void setEstadoAdopcion(EstadoAnimal estadoAdopcion) { this.estadoAdopcion = estadoAdopcion; }
    public Long getRefugioId() { return refugioId; }
    public void setRefugioId(Long refugioId) { this.refugioId = refugioId; }
    public List<String> getFotos() { return fotos; }
    public void setFotos(List<String> fotos) { this.fotos = fotos; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}
