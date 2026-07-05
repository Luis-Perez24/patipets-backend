package com.patipets.core.application.services;

import com.patipets.core.application.ports.output.ApadrinamientoRepositoryPort;
import com.patipets.core.application.ports.output.AnimalRepositoryPort;
import com.patipets.core.application.ports.output.RefugioRepositoryPort;
import com.patipets.core.application.useCase.GestionApadrinamientoUseCase;
import com.patipets.core.domain.enums.EstadoAnimal;
import com.patipets.core.domain.enums.TipoApoyo;
import com.patipets.core.domain.models.Apadrinamiento;
import com.patipets.core.domain.models.Animal;
import com.patipets.core.domain.models.Refugio;
import java.time.LocalDateTime;
import java.util.List;

public class GestionApadrinamientoService implements GestionApadrinamientoUseCase {

    private final ApadrinamientoRepositoryPort apadrinamientoRepository;
    private final AnimalRepositoryPort animalRepository;
    private final RefugioRepositoryPort refugioRepository;

    public GestionApadrinamientoService(ApadrinamientoRepositoryPort apadrinamientoRepository,
                                         AnimalRepositoryPort animalRepository,
                                         RefugioRepositoryPort refugioRepository) {
        this.apadrinamientoRepository = apadrinamientoRepository;
        this.animalRepository = animalRepository;
        this.refugioRepository = refugioRepository;
    }

    @Override
    public Apadrinamiento apadrinar(Long padrinoId, Long animalId, String tipoApoyo, String compromiso) {
        Animal animal = animalRepository.findById(animalId)
                .orElseThrow(() -> new IllegalArgumentException("Animal no encontrado: " + animalId));
        if (animal.getEstadoAdopcion() != EstadoAnimal.DISPONIBLE) {
            throw new IllegalArgumentException("El animal no está disponible para apadrinar");
        }
        if (apadrinamientoRepository.existsByPadrinoIdAndAnimalIdAndActivoTrue(padrinoId, animalId)) {
            throw new IllegalArgumentException("Ya estás apadrinando a este animal");
        }
        Refugio refugio = refugioRepository.findById(animal.getRefugioId())
                .orElseThrow(() -> new IllegalArgumentException("Refugio no encontrado"));
        TipoApoyo tipo;
        try {
            tipo = TipoApoyo.valueOf(tipoApoyo.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Tipo de apoyo inválido: " + tipoApoyo);
        }
        Apadrinamiento apadrinamiento = new Apadrinamiento(
                null, padrinoId, animalId, refugio.getId(), tipo, compromiso, LocalDateTime.now(), true
        );
        return apadrinamientoRepository.save(apadrinamiento);
    }

    @Override
    public Apadrinamiento cancelar(Long id, Long usuarioId) {
        Apadrinamiento apadrinamiento = apadrinamientoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Apadrinamiento no encontrado: " + id));
        if (!apadrinamiento.isActivo()) {
            throw new IllegalStateException("El apadrinamiento ya está cancelado");
        }
        if (!apadrinamiento.getPadrinoId().equals(usuarioId)) {
            throw new IllegalArgumentException("No puedes cancelar un apadrinamiento que no te pertenece");
        }
        Apadrinamiento cancelado = new Apadrinamiento(
                id, apadrinamiento.getPadrinoId(), apadrinamiento.getAnimalId(),
                apadrinamiento.getRefugioId(), apadrinamiento.getTipoApoyo(),
                apadrinamiento.getCompromiso(),
                apadrinamiento.getFechaInicio(), false
        );
        return apadrinamientoRepository.save(cancelado);
    }

    @Override
    public List<Apadrinamiento> listarPorPadrino(Long padrinoId) {
        return apadrinamientoRepository.findByPadrinoId(padrinoId);
    }

    @Override
    public List<Apadrinamiento> listarPorAnimal(Long animalId) {
        return apadrinamientoRepository.findByAnimalId(animalId);
    }

    @Override
    public List<Apadrinamiento> listarPorRefugio(Long refugioId) {
        return apadrinamientoRepository.findByRefugioId(refugioId);
    }
}
