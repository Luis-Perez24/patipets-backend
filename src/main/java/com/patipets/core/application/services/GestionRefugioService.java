package com.patipets.core.application.services;

import com.patipets.core.application.ports.output.RefugioRepositoryPort;
import com.patipets.core.application.useCase.GestionRefugioUseCase;
import com.patipets.core.domain.enums.EstadoRefugio;
import com.patipets.core.domain.models.Refugio;
import java.util.List;

public class GestionRefugioService implements GestionRefugioUseCase {

    private final RefugioRepositoryPort refugioRepository;

    public GestionRefugioService(RefugioRepositoryPort refugioRepository) {
        this.refugioRepository = refugioRepository;
    }

    @Override
    public Refugio solicitar(String nombre, String direccion, String region,
                              Double latitud, Double longitud, Integer capacidad) {
        Refugio nuevo = new Refugio(
                null, nombre, direccion, region,
                latitud, longitud, capacidad, EstadoRefugio.PENDIENTE
        );
        return refugioRepository.save(nuevo);
    }

    @Override
    public Refugio aprobar(Long id) {
        Refugio refugio = refugioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Refugio no encontrado: " + id));
        if (refugio.getEstado() != EstadoRefugio.PENDIENTE) {
            throw new IllegalStateException("El refugio no está en estado PENDIENTE");
        }
        Refugio actualizado = new Refugio(
                refugio.getId(), refugio.getNombre(), refugio.getDireccion(),
                refugio.getRegion(), refugio.getLatitud(), refugio.getLongitud(),
                refugio.getCapacidad(), EstadoRefugio.APROBADO
        );
        return refugioRepository.save(actualizado);
    }

    @Override
    public Refugio rechazar(Long id) {
        Refugio refugio = refugioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Refugio no encontrado: " + id));
        if (refugio.getEstado() != EstadoRefugio.PENDIENTE) {
            throw new IllegalStateException("El refugio no está en estado PENDIENTE");
        }
        Refugio actualizado = new Refugio(
                refugio.getId(), refugio.getNombre(), refugio.getDireccion(),
                refugio.getRegion(), refugio.getLatitud(), refugio.getLongitud(),
                refugio.getCapacidad(), EstadoRefugio.RECHAZADO
        );
        return refugioRepository.save(actualizado);
    }

    @Override
    public List<Refugio> listarPendientes() {
        return refugioRepository.findByEstado(EstadoRefugio.PENDIENTE);
    }
}
