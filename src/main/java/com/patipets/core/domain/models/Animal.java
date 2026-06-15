package com.patipets.core.domain.models;

import com.patipets.core.domain.enums.EstadoAnimal;
import java.time.LocalDateTime;
import java.util.List;

public class Animal {
    private final Long id;
    private final String nombre;
    private final String especie;
    private final String raza;
    private final Integer edad;
    private final String tamano;
    private final String personalidad;
    private final String estadoSalud;
    private final String historia;
    private final EstadoAnimal estadoAdopcion;
    private final Long refugioId;
    private final String nombreRefugio;
    private final String regionRefugio;
    private final List<String> fotos;
    private final LocalDateTime fechaRegistro;

    public Animal(Long id, String nombre, String especie, String raza, Integer edad,
                  String tamano, String personalidad, String estadoSalud, String historia,
                  EstadoAnimal estadoAdopcion, Long refugioId, String nombreRefugio,
                  String regionRefugio, List<String> fotos, LocalDateTime fechaRegistro) {
        this.id = id;
        this.nombre = nombre;
        this.especie = especie;
        this.raza = raza;
        this.edad = edad;
        this.tamano = tamano;
        this.personalidad = personalidad;
        this.estadoSalud = estadoSalud;
        this.historia = historia;
        this.estadoAdopcion = estadoAdopcion;
        this.refugioId = refugioId;
        this.nombreRefugio = nombreRefugio;
        this.regionRefugio = regionRefugio;
        this.fotos = fotos;
        this.fechaRegistro = fechaRegistro;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEspecie() { return especie; }
    public String getRaza() { return raza; }
    public Integer getEdad() { return edad; }
    public String getTamano() { return tamano; }
    public String getPersonalidad() { return personalidad; }
    public String getEstadoSalud() { return estadoSalud; }
    public String getHistoria() { return historia; }
    public EstadoAnimal getEstadoAdopcion() { return estadoAdopcion; }
    public Long getRefugioId() { return refugioId; }
    public String getNombreRefugio() { return nombreRefugio; }
    public String getRegionRefugio() { return regionRefugio; }
    public List<String> getFotos() { return fotos; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
}
