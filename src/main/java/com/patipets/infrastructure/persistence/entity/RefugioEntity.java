package com.patipets.infrastructure.persistence.entity;

import com.patipets.core.domain.enums.EstadoRefugio;
import jakarta.persistence.*;

@Entity
@Table(name = "refugios")
public class RefugioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    private String direccion;

    @Column(nullable = false)
    private String region;

    private String comuna;

    private Double latitud;

    private Double longitud;

    private Integer capacidad;

    private String email;

    @Column(name = "numero_contacto")
    private String numeroContacto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoRefugio estado;

    private String foto;

    public RefugioEntity() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    public String getComuna() { return comuna; }
    public void setComuna(String comuna) { this.comuna = comuna; }
    public Double getLatitud() { return latitud; }
    public void setLatitud(Double latitud) { this.latitud = latitud; }
    public Double getLongitud() { return longitud; }
    public void setLongitud(Double longitud) { this.longitud = longitud; }
    public Integer getCapacidad() { return capacidad; }
    public void setCapacidad(Integer capacidad) { this.capacidad = capacidad; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getNumeroContacto() { return numeroContacto; }
    public void setNumeroContacto(String numeroContacto) { this.numeroContacto = numeroContacto; }
    public EstadoRefugio getEstado() { return estado; }
    public void setEstado(EstadoRefugio estado) { this.estado = estado; }
    public String getFoto() { return foto; }
    public void setFoto(String foto) { this.foto = foto; }
}
