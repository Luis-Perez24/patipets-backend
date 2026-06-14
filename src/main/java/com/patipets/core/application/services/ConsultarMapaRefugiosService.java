package com.patipets.core.application.services;

import com.patipets.core.application.ports.output.RefugioRepositoryPort;
import com.patipets.core.application.useCase.ConsultarMapaRefugiosUseCase;
import com.patipets.core.domain.models.Refugio;
import java.util.List;

public class ConsultarMapaRefugiosService implements ConsultarMapaRefugiosUseCase {

    private final RefugioRepositoryPort refugioRepository;

    public ConsultarMapaRefugiosService(RefugioRepositoryPort refugioRepository) {
        this.refugioRepository = refugioRepository;
    }

    @Override
    public List<Refugio> obtenerRefugiosAprobados() {
        return refugioRepository.findAllAprobados();
    }
}
