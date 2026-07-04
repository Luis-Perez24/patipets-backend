package com.patipets.core.domain.models;

import com.patipets.core.domain.enums.EstadoRefugio;

public class Refugio {
    private final Long id;
    private final String nombre;
    private final String direccion;
    private final String region;
    private final String comuna;
    private final Double latitud;
    private final Double longitud;
    private final Integer capacidad;
    private final String email;
    private final String numeroContacto;
    private final EstadoRefugio estado;
    private final String foto;

    public Refugio(Long id, String nombre, String direccion, String region,
                   String comuna, Double latitud, Double longitud, Integer capacidad,
                   String email, String numeroContacto, EstadoRefugio estado, String foto) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.region = region;
        this.comuna = comuna;
        this.latitud = latitud;
        this.longitud = longitud;
        this.capacidad = capacidad;
        this.email = email;
        this.numeroContacto = numeroContacto;
        this.estado = estado;
        this.foto = foto;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDireccion() { return direccion; }
    public String getRegion() { return region; }
    public String getComuna() { return comuna; }
    public Double getLatitud() { return latitud; }
    public Double getLongitud() { return longitud; }
    public Integer getCapacidad() { return capacidad; }
    public String getEmail() { return email; }
    public String getNumeroContacto() { return numeroContacto; }
    public EstadoRefugio getEstado() { return estado; }
    public String getFoto() { return foto; }
}
