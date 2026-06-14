package com.patipets.core.domain.models;

public class EstadisticaDashboard {
    private final long totalAnimalesDisponibles;
    private final long totalAdopcionesConcretadas;
    private final long totalVoluntariosActivos;
    private final long totalRefugiosRegistrados;

    public EstadisticaDashboard(long totalAnimalesDisponibles, long totalAdopcionesConcretadas,
                                 long totalVoluntariosActivos, long totalRefugiosRegistrados) {
        this.totalAnimalesDisponibles = totalAnimalesDisponibles;
        this.totalAdopcionesConcretadas = totalAdopcionesConcretadas;
        this.totalVoluntariosActivos = totalVoluntariosActivos;
        this.totalRefugiosRegistrados = totalRefugiosRegistrados;
    }

    public long getTotalAnimalesDisponibles() { return totalAnimalesDisponibles; }
    public long getTotalAdopcionesConcretadas() { return totalAdopcionesConcretadas; }
    public long getTotalVoluntariosActivos() { return totalVoluntariosActivos; }
    public long getTotalRefugiosRegistrados() { return totalRefugiosRegistrados; }
}
