package com.patipets.infrastructure.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.patipets.core.domain.models.EstadisticaDashboard;

public class EstadisticaDashboardResponseDTO {

    @JsonProperty("animales_disponibles")
    private long animalesDisponibles;

    @JsonProperty("adopciones_concretadas")
    private long adopcionesConcretadas;

    @JsonProperty("voluntarios_activos")
    private long voluntariosActivos;

    @JsonProperty("refugios_registrados")
    private long refugiosRegistrados;

    public static EstadisticaDashboardResponseDTO fromDomain(EstadisticaDashboard stats) {
        EstadisticaDashboardResponseDTO dto = new EstadisticaDashboardResponseDTO();
        dto.animalesDisponibles = stats.getTotalAnimalesDisponibles();
        dto.adopcionesConcretadas = stats.getTotalAdopcionesConcretadas();
        dto.voluntariosActivos = stats.getTotalVoluntariosActivos();
        dto.refugiosRegistrados = stats.getTotalRefugiosRegistrados();
        return dto;
    }

    public long getAnimalesDisponibles() { return animalesDisponibles; }
    public long getAdopcionesConcretadas() { return adopcionesConcretadas; }
    public long getVoluntariosActivos() { return voluntariosActivos; }
    public long getRefugiosRegistrados() { return refugiosRegistrados; }
}
