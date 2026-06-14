package com.patipets.core.application.ports.output;

import com.patipets.core.domain.models.AdminDashboardStats;
import com.patipets.core.domain.models.EstadisticaDashboard;

public interface EstadisticaRepositoryPort {
    EstadisticaDashboard obtenerEstadisticas();
    AdminDashboardStats obtenerAdminStats();
}
