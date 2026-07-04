package com.patipets.core.application.services;

import com.patipets.core.application.ports.output.ImageStoragePort;
import com.patipets.core.application.ports.output.RefugioRepositoryPort;
import com.patipets.core.application.useCase.GestionRefugioUseCase;
import com.patipets.core.domain.enums.EstadoRefugio;
import com.patipets.core.domain.models.Refugio;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

public class GestionRefugioService implements GestionRefugioUseCase {

    private final RefugioRepositoryPort refugioRepository;
    private final ImageStoragePort imageStoragePort;

    public GestionRefugioService(RefugioRepositoryPort refugioRepository, ImageStoragePort imageStoragePort) {
        this.refugioRepository = refugioRepository;
        this.imageStoragePort = imageStoragePort;
    }

    @Override
    public Refugio solicitar(String nombre, String direccion, String region, String comuna,
                              Double latitud, Double longitud, Integer capacidad,
                              String email, String numeroContacto, Long usuarioId,
                              MultipartFile foto) throws IOException {
        String fotoUrl = (foto != null && !foto.isEmpty()) ? imageStoragePort.upload(foto) : null;
        Refugio nuevo = new Refugio(
                null, nombre, direccion, region, comuna,
                latitud, longitud, capacidad, email, numeroContacto, EstadoRefugio.PENDIENTE, fotoUrl
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
                refugio.getEmail(), refugio.getNumeroContacto(), EstadoRefugio.APROBADO,
                refugio.getFoto()
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
                refugio.getEmail(), refugio.getNumeroContacto(), EstadoRefugio.RECHAZADO,
                refugio.getFoto()
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
    public List<Refugio> listarMisRefugios(Long usuarioId) {
        return refugioRepository.findByUsuario(usuarioId);
    }
}
