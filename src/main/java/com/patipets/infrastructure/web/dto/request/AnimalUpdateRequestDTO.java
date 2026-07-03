package com.patipets.infrastructure.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

public class AnimalUpdateRequestDTO {

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

    private String personalidad;

    @NotBlank(message = "es obligatorio")
    private String estadoSalud;

    private String historia;

    @NotBlank(message = "es obligatorio")
    private String estadoAdopcion;

    private List<String> fotosAMantener;

    public AnimalUpdateRequestDTO() {}

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
    public String getEstadoAdopcion() { return estadoAdopcion; }
    public void setEstadoAdopcion(String estadoAdopcion) { this.estadoAdopcion = estadoAdopcion; }
    public List<String> getFotosAMantener() { return fotosAMantener; }
    public void setFotosAMantener(List<String> fotosAMantener) { this.fotosAMantener = fotosAMantener; }
}
