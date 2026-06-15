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

    private String personalidad;

    @JsonProperty("estado_salud")
    private String estadoSalud;

    private String historia;

    @JsonProperty("estado_adopcion")
    private String estadoAdopcion;

    @JsonProperty("refugio_id")
    private Long refugioId;

    private List<String> fotos;

    @JsonProperty("fecha_registro")
    private LocalDateTime fechaRegistro;

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
        return dto;
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
    public String getEstadoAdopcion() { return estadoAdopcion; }
    public Long getRefugioId() { return refugioId; }
    public List<String> getFotos() { return fotos; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
}
