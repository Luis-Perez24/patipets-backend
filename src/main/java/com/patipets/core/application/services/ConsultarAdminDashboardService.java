package com.patipets.core.application.services;

import com.patipets.core.application.ports.output.EstadisticaRepositoryPort;
import com.patipets.core.application.useCase.ConsultarAdminDashboardUseCase;
import com.patipets.core.domain.models.AdminDashboardStats;

public class ConsultarAdminDashboardService implements ConsultarAdminDashboardUseCase {

    private final EstadisticaRepositoryPort estadisticaRepository;

    public ConsultarAdminDashboardService(EstadisticaRepositoryPort estadisticaRepository) {
        this.estadisticaRepository = estadisticaRepository;
    }

    @Override
    public AdminDashboardStats obtenerEstadisticasAdmin() {
        return estadisticaRepository.obtenerAdminStats();
    }
}
