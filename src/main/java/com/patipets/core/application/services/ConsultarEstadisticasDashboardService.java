package com.patipets.core.application.services;

import com.patipets.core.application.ports.output.EstadisticaRepositoryPort;
import com.patipets.core.application.useCase.ConsultarEstadisticasDashboardUseCase;
import com.patipets.core.domain.models.EstadisticaDashboard;

public class ConsultarEstadisticasDashboardService implements ConsultarEstadisticasDashboardUseCase {

    private final EstadisticaRepositoryPort estadisticaRepository;

    public ConsultarEstadisticasDashboardService(EstadisticaRepositoryPort estadisticaRepository) {
        this.estadisticaRepository = estadisticaRepository;
    }

    @Override
    public EstadisticaDashboard obtenerEstadisticas() {
        return estadisticaRepository.obtenerEstadisticas();
    }
}
