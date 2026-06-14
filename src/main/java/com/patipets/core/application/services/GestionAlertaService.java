package com.patipets.core.application.services;

import com.patipets.core.application.ports.output.AlertaRepositoryPort;
import com.patipets.core.application.useCase.GestionAlertaUseCase;
import com.patipets.core.domain.enums.NivelUrgencia;
import com.patipets.core.domain.models.Alerta;
import java.time.LocalDateTime;
import java.util.List;

public class GestionAlertaService implements GestionAlertaUseCase {

    private final AlertaRepositoryPort alertaRepository;

    public GestionAlertaService(AlertaRepositoryPort alertaRepository) {
        this.alertaRepository = alertaRepository;
    }

    @Override
    public Alerta crear(String titulo, String descripcion, String nivelUrgencia,
                        Long refugioId, Long creadoPor) {
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("El título es obligatorio");
        }
        NivelUrgencia nivel;
        try {
            nivel = NivelUrgencia.valueOf(nivelUrgencia.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Nivel de urgencia inválido: " + nivelUrgencia);
        }
        Alerta alerta = new Alerta(
                null, titulo, descripcion, nivel, refugioId, creadoPor, true, LocalDateTime.now()
        );
        return alertaRepository.save(alerta);
    }

    @Override
    public List<Alerta> listarPorRefugio(Long refugioId) {
        return alertaRepository.findByRefugioId(refugioId);
    }

    @Override
    public List<Alerta> listarActivas(int page, int size) {
        return alertaRepository.findActivas(page, size);
    }

    @Override
    public Alerta marcarResuelta(Long id) {
        Alerta alerta = alertaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Alerta no encontrada: " + id));
        if (!alerta.isActiva()) {
            throw new IllegalStateException("La alerta ya está resuelta");
        }
        Alerta resuelta = new Alerta(
                id, alerta.getTitulo(), alerta.getDescripcion(), alerta.getNivelUrgencia(),
                alerta.getRefugioId(), alerta.getCreadoPor(), false, alerta.getCreatedAt()
        );
        return alertaRepository.save(resuelta);
    }

    @Override
    public void eliminar(Long id) {
        if (alertaRepository.findById(id).isEmpty()) {
            throw new IllegalArgumentException("Alerta no encontrada: " + id);
        }
        alertaRepository.deleteById(id);
    }
}
