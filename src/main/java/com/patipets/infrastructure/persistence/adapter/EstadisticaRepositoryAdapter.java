package com.patipets.infrastructure.persistence.adapter;

import com.patipets.core.application.ports.output.AnimalRepositoryPort;
import com.patipets.core.application.ports.output.EstadisticaRepositoryPort;
import com.patipets.core.application.ports.output.RefugioRepositoryPort;
import com.patipets.core.application.ports.output.UsuarioRepositoryPort;
import com.patipets.core.domain.enums.EstadoRefugio;
import com.patipets.core.domain.models.AdminDashboardStats;
import com.patipets.core.domain.models.EstadisticaDashboard;
import com.patipets.infrastructure.persistence.repository.AnimalJpaRepository;
import com.patipets.infrastructure.persistence.repository.RefugioJpaRepository;
import com.patipets.infrastructure.persistence.repository.SolicitudAdopcionJpaRepository;
import com.patipets.infrastructure.persistence.repository.UsuarioJpaRepository;

public class EstadisticaRepositoryAdapter implements EstadisticaRepositoryPort {

    private final AnimalJpaRepository animalJpaRepository;
    private final RefugioJpaRepository refugioJpaRepository;
    private final SolicitudAdopcionJpaRepository solicitudJpaRepository;
    private final UsuarioJpaRepository usuarioJpaRepository;

    public EstadisticaRepositoryAdapter(AnimalJpaRepository animalJpaRepository,
                                         RefugioJpaRepository refugioJpaRepository,
                                         SolicitudAdopcionJpaRepository solicitudJpaRepository,
                                         UsuarioJpaRepository usuarioJpaRepository) {
        this.animalJpaRepository = animalJpaRepository;
        this.refugioJpaRepository = refugioJpaRepository;
        this.solicitudJpaRepository = solicitudJpaRepository;
        this.usuarioJpaRepository = usuarioJpaRepository;
    }

    @Override
    public EstadisticaDashboard obtenerEstadisticas() {
        long disponibles = animalJpaRepository.countByEstadoAdopcion(
                com.patipets.core.domain.enums.EstadoAnimal.DISPONIBLE);
        long refugios = refugioJpaRepository.countByEstado(EstadoRefugio.APROBADO);
        long adopciones = solicitudJpaRepository.countByEstado("APROBADA");
        long voluntarios = usuarioJpaRepository.countByRol(
                com.patipets.core.domain.enums.Rol.VOLUNTARIO);
        return new EstadisticaDashboard(disponibles, adopciones, voluntarios, refugios);
    }

    @Override
    public AdminDashboardStats obtenerAdminStats() {
        long disponibles = animalJpaRepository.countByEstadoAdopcion(
                com.patipets.core.domain.enums.EstadoAnimal.DISPONIBLE);
        long refugiosPendientes = refugioJpaRepository.countByEstado(EstadoRefugio.PENDIENTE);
        long refugiosAprobados = refugioJpaRepository.countByEstado(EstadoRefugio.APROBADO);
        long refugiosRechazados = refugioJpaRepository.countByEstado(EstadoRefugio.RECHAZADO);
        long solicitudesPendientes = solicitudJpaRepository.countByEstado("PENDIENTE");
        long solicitudesAprobadas = solicitudJpaRepository.countByEstado("APROBADA");
        long usuariosTotales = usuarioJpaRepository.count();
        return new AdminDashboardStats(disponibles, refugiosPendientes, refugiosAprobados,
                refugiosRechazados, solicitudesPendientes, solicitudesAprobadas, usuariosTotales);
    }
}
