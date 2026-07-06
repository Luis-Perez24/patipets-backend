package com.patipets.core.application.services;

import com.patipets.core.application.events.ApadrinamientoCanceladoPorPadrinoEvent;
import com.patipets.core.application.events.ApadrinamientoCanceladoPorRefugioEvent;
import com.patipets.core.application.ports.output.AnimalRepositoryPort;
import com.patipets.core.application.ports.output.ApadrinamientoRepositoryPort;
import com.patipets.core.application.ports.output.EventPublisherPort;
import com.patipets.core.application.ports.output.RefugioRepositoryPort;
import com.patipets.core.application.useCase.GestionApadrinamientoUseCase;
import com.patipets.core.domain.enums.EstadoAnimal;
import com.patipets.core.domain.enums.TipoApoyo;
import com.patipets.core.domain.models.Animal;
import com.patipets.core.domain.models.Apadrinamiento;
import com.patipets.core.domain.models.Refugio;
import java.time.LocalDateTime;
import java.util.List;

public class GestionApadrinamientoService implements GestionApadrinamientoUseCase {

    private final ApadrinamientoRepositoryPort apadrinamientoRepository;
    private final AnimalRepositoryPort animalRepository;
    private final RefugioRepositoryPort refugioRepository;
    private final EventPublisherPort eventPublisher;

    public GestionApadrinamientoService(ApadrinamientoRepositoryPort apadrinamientoRepository,
                                         AnimalRepositoryPort animalRepository,
                                         RefugioRepositoryPort refugioRepository,
                                         EventPublisherPort eventPublisher) {
        this.apadrinamientoRepository = apadrinamientoRepository;
        this.animalRepository = animalRepository;
        this.refugioRepository = refugioRepository;
        this.eventPublisher = eventPublisher;
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
        if (refugioRepository.perteneceAUsuario(animal.getRefugioId(), padrinoId)) {
            throw new IllegalArgumentException("No puedes apadrinar un animal de tu propio refugio");
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
        return cancelarPorPadrino(id, usuarioId, null);
    }

    @Override
    public Apadrinamiento cancelarPorPadrino(Long id, Long usuarioId, String motivo) {
        Apadrinamiento apadrinamiento = apadrinamientoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Apadrinamiento no encontrado: " + id));
        if (!apadrinamiento.isActivo()) {
            throw new IllegalStateException("El apadrinamiento ya está cancelado");
        }
        if (!apadrinamiento.getPadrinoId().equals(usuarioId)) {
            throw new IllegalArgumentException("No puedes cancelar un apadrinamiento que no te pertenece");
        }
        String nombreAnimal = animalRepository.findById(apadrinamiento.getAnimalId())
                .map(Animal::getNombre).orElse("el animal");
        Apadrinamiento cancelado = new Apadrinamiento(
                id, apadrinamiento.getPadrinoId(), apadrinamiento.getAnimalId(),
                apadrinamiento.getRefugioId(), apadrinamiento.getTipoApoyo(),
                apadrinamiento.getCompromiso(),
                apadrinamiento.getFechaInicio(), false
        );
        Apadrinamiento guardado = apadrinamientoRepository.save(cancelado);
        eventPublisher.publicar(new ApadrinamientoCanceladoPorPadrinoEvent(
                guardado.getId(), guardado.getAnimalId(), guardado.getRefugioId(),
                guardado.getPadrinoId(), nombreAnimal, motivo));
        return guardado;
    }

    @Override
    public Apadrinamiento cancelarPorRefugio(Long id, Long refugioId, String motivo) {
        Apadrinamiento apadrinamiento = apadrinamientoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Apadrinamiento no encontrado: " + id));
        if (!apadrinamiento.getRefugioId().equals(refugioId)) {
            throw new IllegalArgumentException("El apadrinamiento no pertenece a este refugio");
        }
        if (!apadrinamiento.isActivo()) {
            throw new IllegalStateException("El apadrinamiento ya está cancelado");
        }
        Apadrinamiento cancelado = new Apadrinamiento(
                id, apadrinamiento.getPadrinoId(), apadrinamiento.getAnimalId(),
                apadrinamiento.getRefugioId(), apadrinamiento.getTipoApoyo(),
                apadrinamiento.getCompromiso(),
                apadrinamiento.getFechaInicio(), false
        );
        Apadrinamiento guardado = apadrinamientoRepository.save(cancelado);
        String nombreAnimal = animalRepository.findById(guardado.getAnimalId())
                .map(Animal::getNombre)
                .orElse("el animal");
        eventPublisher.publicar(new ApadrinamientoCanceladoPorRefugioEvent(
                guardado.getId(), guardado.getPadrinoId(), guardado.getAnimalId(),
                nombreAnimal, motivo));
        return guardado;
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
