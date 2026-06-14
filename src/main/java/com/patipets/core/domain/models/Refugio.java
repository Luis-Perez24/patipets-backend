package com.patipets.core.domain.models;

import com.patipets.core.domain.enums.EstadoRefugio;

public class Refugio {
    private final Long id;
    private final String nombre;
    private final String direccion;
    private final String region;
    private final Double latitud;
    private final Double longitud;
    private final Integer capacidad;
    private final EstadoRefugio estado;

    public Refugio(Long id, String nombre, String direccion, String region,
                   Double latitud, Double longitud, Integer capacidad, EstadoRefugio estado) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.region = region;
        this.latitud = latitud;
        this.longitud = longitud;
        this.capacidad = capacidad;
        this.estado = estado;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDireccion() { return direccion; }
    public String getRegion() { return region; }
    public Double getLatitud() { return latitud; }
    public Double getLongitud() { return longitud; }
    public Integer getCapacidad() { return capacidad; }
    public EstadoRefugio getEstado() { return estado; }
}
