package com.patipets.infrastructure.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class RefugioHistorialResponseDTO {

    private Long refugioId;
    private String refugioNombre;

    @JsonProperty("total_animales")
    private long totalAnimales;

    @JsonProperty("total_adopciones")
    private long totalAdopciones;

    @JsonProperty("animales_adoptados")
    private List<AnimalResponseDTO> animalesAdoptados;

    public RefugioHistorialResponseDTO() {}

    public static RefugioHistorialResponseDTO of(Long refugioId, String refugioNombre,
                                                 long totalAnimales, long totalAdopciones,
                                                 List<AnimalResponseDTO> animalesAdoptados) {
        RefugioHistorialResponseDTO dto = new RefugioHistorialResponseDTO();
        dto.refugioId = refugioId;
        dto.refugioNombre = refugioNombre;
        dto.totalAnimales = totalAnimales;
        dto.totalAdopciones = totalAdopciones;
        dto.animalesAdoptados = animalesAdoptados;
        return dto;
    }

    public Long getRefugioId() { return refugioId; }
    public String getRefugioNombre() { return refugioNombre; }
    public long getTotalAnimales() { return totalAnimales; }
    public long getTotalAdopciones() { return totalAdopciones; }
    public List<AnimalResponseDTO> getAnimalesAdoptados() { return animalesAdoptados; }
}
