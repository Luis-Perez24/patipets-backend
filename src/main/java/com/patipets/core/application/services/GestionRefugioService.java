package com.patipets.core.application.services;

import com.patipets.core.application.ports.output.RefugioRepositoryPort;
import com.patipets.core.application.useCase.GestionRefugioUseCase;
import com.patipets.core.domain.enums.EstadoRefugio;
import com.patipets.core.domain.models.Refugio;
import java.util.List;
import java.util.Optional;

public class GestionRefugioService implements GestionRefugioUseCase {

    private final RefugioRepositoryPort refugioRepository;

    public GestionRefugioService(RefugioRepositoryPort refugioRepository) {
        this.refugioRepository = refugioRepository;
    }

    @Override
    public Refugio solicitar(String nombre, String direccion, String region, String comuna,
                              Double latitud, Double longitud, Integer capacidad,
                              String email, String numeroContacto, Long usuarioId) {
        Refugio nuevo = new Refugio(
                null, nombre, direccion, region, comuna,
                latitud, longitud, capacidad, email, numeroContacto, EstadoRefugio.PENDIENTE
        );
        Refugio guardado = refugioRepository.save(nuevo);
        refugioRepository.vincularUsuario(usuarioId, guardado.getId());
        return guardado;
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
                refugio.getRegion(), refugio.getComuna(), refugio.getLatitud(),
                refugio.getLongitud(), refugio.getCapacidad(),
                refugio.getEmail(), refugio.getNumeroContacto(), EstadoRefugio.APROBADO
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
                refugio.getRegion(), refugio.getComuna(), refugio.getLatitud(),
                refugio.getLongitud(), refugio.getCapacidad(),
                refugio.getEmail(), refugio.getNumeroContacto(), EstadoRefugio.RECHAZADO
        );
        return refugioRepository.save(actualizado);
    }

    @Override
    public List<Refugio> listarPendientes() {
        return refugioRepository.findByEstado(EstadoRefugio.PENDIENTE);
    }

    @Override
    public boolean perteneceAUsuario(Long refugioId, Long usuarioId) {
        return refugioRepository.perteneceAUsuario(refugioId, usuarioId);
    }

    @Override
    public Optional<Refugio> obtenerPorId(Long id) {
        return refugioRepository.findById(id);
    }
}
