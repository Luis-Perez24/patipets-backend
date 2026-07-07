package com.patipets.infrastructure.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.patipets.core.domain.models.Animal;
import java.time.LocalDateTime;
import java.util.List;

public class AnimalResponseDTO {

    private Long id;
    private String nombre;
    private String especie;
    private String raza;
    private Integer edad;
    private String tamano;

    private List<String> personalidad;

    @JsonProperty("estado_salud")
    private List<String> estadoSalud;

    private String historia;

    @JsonProperty("estado_adopcion")
    private String estadoAdopcion;

    @JsonProperty("refugio_id")
    private Long refugioId;

    @JsonProperty("refugio_nombre")
    private String refugioNombre;

    @JsonProperty("refugio_telefono")
    private String refugioTelefono;

    @JsonProperty("refugio_email")
    private String refugioEmail;

    @JsonProperty("refugio_direccion")
    private String refugioDireccion;

    @JsonProperty("refugio_region")
    private String refugioRegion;

    @JsonProperty("refugio_comuna")
    private String refugioComuna;

    private List<String> fotos;

    @JsonProperty("fecha_registro")
    private LocalDateTime fechaRegistro;

    private String sexo;
    private Double peso;

    public static AnimalResponseDTO fromDomain(Animal animal) {
        AnimalResponseDTO dto = new AnimalResponseDTO();
        dto.id = animal.getId();
        dto.nombre = animal.getNombre();
        dto.especie = animal.getEspecie();
        dto.raza = animal.getRaza();
        dto.edad = animal.getEdad();
        dto.tamano = animal.getTamano();
        dto.personalidad = animal.getPersonalidad();
        dto.estadoSalud = animal.getEstadoSalud();
        dto.historia = animal.getHistoria();
        dto.estadoAdopcion = animal.getEstadoAdopcion().name();
        dto.refugioId = animal.getRefugioId();
        dto.fotos = animal.getFotos();
        dto.fechaRegistro = animal.getFechaRegistro();
        dto.sexo = animal.getSexo();
        dto.peso = animal.getPeso();
        return dto;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEspecie() { return especie; }
    public String getRaza() { return raza; }
    public Integer getEdad() { return edad; }
    public String getTamano() { return tamano; }
    public List<String> getPersonalidad() { return personalidad; }
    public List<String> getEstadoSalud() { return estadoSalud; }
    public String getHistoria() { return historia; }
    public String getEstadoAdopcion() { return estadoAdopcion; }
    public Long getRefugioId() { return refugioId; }
    public String getRefugioNombre() { return refugioNombre; }
    public String getRefugioTelefono() { return refugioTelefono; }
    public String getRefugioEmail() { return refugioEmail; }
    public String getRefugioDireccion() { return refugioDireccion; }
    public String getRefugioRegion() { return refugioRegion; }
    public String getRefugioComuna() { return refugioComuna; }
    public List<String> getFotos() { return fotos; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }

    public void setRefugioNombre(String refugioNombre) { this.refugioNombre = refugioNombre; }
    public void setRefugioTelefono(String refugioTelefono) { this.refugioTelefono = refugioTelefono; }
    public void setRefugioEmail(String refugioEmail) { this.refugioEmail = refugioEmail; }
    public void setRefugioDireccion(String refugioDireccion) { this.refugioDireccion = refugioDireccion; }
    public void setRefugioRegion(String refugioRegion) { this.refugioRegion = refugioRegion; }
    public void setRefugioComuna(String refugioComuna) { this.refugioComuna = refugioComuna; }
    public String getSexo() { return sexo; }
    public Double getPeso() { return peso; }
}
