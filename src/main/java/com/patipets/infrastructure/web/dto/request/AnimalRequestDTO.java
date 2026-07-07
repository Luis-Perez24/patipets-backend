package com.patipets.infrastructure.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

public class AnimalRequestDTO {

    @NotBlank(message = "es obligatorio")
    private String nombre;

    @NotBlank(message = "es obligatorio")
    private String especie;

    @NotBlank(message = "es obligatorio")
    private String raza;

    @NotNull(message = "es obligatorio")
    @Positive(message = "debe ser un número positivo")
    private Integer edad;

    @NotBlank(message = "es obligatorio")
    private String tamano;

    private List<String> personalidad;

    private List<String> estadoSalud;

    private String historia;

    @NotNull(message = "es obligatorio")
    @Positive(message = "debe ser un número positivo")
    private Long refugioId;

    private String sexo;

    @Positive(message = "debe ser un número positivo")
    private Double peso;

    public AnimalRequestDTO() {}

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
    public List<String> getPersonalidad() { return personalidad; }
    public void setPersonalidad(List<String> personalidad) { this.personalidad = personalidad; }
    public List<String> getEstadoSalud() { return estadoSalud; }
    public void setEstadoSalud(List<String> estadoSalud) { this.estadoSalud = estadoSalud; }
    public String getHistoria() { return historia; }
    public void setHistoria(String historia) { this.historia = historia; }
    public Long getRefugioId() { return refugioId; }
    public void setRefugioId(Long refugioId) { this.refugioId = refugioId; }
    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }
    public Double getPeso() { return peso; }
    public void setPeso(Double peso) { this.peso = peso; }
}
