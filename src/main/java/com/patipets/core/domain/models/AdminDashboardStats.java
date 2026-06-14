package com.patipets.core.domain.models;

public class AdminDashboardStats {
    private final long animalesDisponibles;
    private final long refugiosPendientes;
    private final long refugiosAprobados;
    private final long refugiosRechazados;
    private final long solicitudesPendientes;
    private final long solicitudesAprobadas;
    private final long usuariosTotales;

    public AdminDashboardStats(long animalesDisponibles, long refugiosPendientes,
                                long refugiosAprobados, long refugiosRechazados,
                                long solicitudesPendientes, long solicitudesAprobadas,
                                long usuariosTotales) {
        this.animalesDisponibles = animalesDisponibles;
        this.refugiosPendientes = refugiosPendientes;
        this.refugiosAprobados = refugiosAprobados;
        this.refugiosRechazados = refugiosRechazados;
        this.solicitudesPendientes = solicitudesPendientes;
        this.solicitudesAprobadas = solicitudesAprobadas;
        this.usuariosTotales = usuariosTotales;
    }

    public long getAnimalesDisponibles() { return animalesDisponibles; }
    public long getRefugiosPendientes() { return refugiosPendientes; }
    public long getRefugiosAprobados() { return refugiosAprobados; }
    public long getRefugiosRechazados() { return refugiosRechazados; }
    public long getSolicitudesPendientes() { return solicitudesPendientes; }
    public long getSolicitudesAprobadas() { return solicitudesAprobadas; }
    public long getUsuariosTotales() { return usuariosTotales; }
}
