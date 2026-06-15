package com.patipets.infrastructure.persistence.adapter;

import com.patipets.core.application.ports.output.AnimalRepositoryPort;
import com.patipets.core.domain.enums.EstadoAnimal;
import com.patipets.core.domain.models.Animal;
import com.patipets.infrastructure.persistence.entity.AnimalEntity;
import com.patipets.infrastructure.persistence.repository.AnimalJpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AnimalRepositoryAdapter implements AnimalRepositoryPort {

    private final AnimalJpaRepository jpaRepository;

    public AnimalRepositoryAdapter(AnimalJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<Animal> findAllDisponibles(String especie, String raza, Integer edadMin,
                                            Integer edadMax, String tamano, String region) {
        List<AnimalEntity> entities = jpaRepository.buscarDisponiblesConFiltros(
                especie, raza, edadMin, edadMax, tamano);
        return entities.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Animal> findById(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Animal> findByRefugioId(Long refugioId) {
        return jpaRepository.findByRefugioId(refugioId)
                .stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public Animal save(Animal animal) {
        AnimalEntity entity = toEntity(animal);
        AnimalEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public long countDisponibles() {
        return jpaRepository.countByEstadoAdopcion(EstadoAnimal.DISPONIBLE);
    }

    @Override
    public long countByRefugioId(Long refugioId) {
        return jpaRepository.countByRefugioId(refugioId);
    }

    private Animal toDomain(AnimalEntity entity) {
        return new Animal(
                entity.getId(),
                entity.getNombre(),
                entity.getEspecie(),
                entity.getRaza(),
                entity.getEdad(),
                entity.getTamano(),
                entity.getPersonalidad(),
                entity.getEstadoSalud(),
                entity.getHistoria(),
                entity.getEstadoAdopcion(),
                entity.getRefugioId(),
                null,
                null,
                entity.getFotos(),
                entity.getFechaRegistro()
        );
    }

    private AnimalEntity toEntity(Animal animal) {
        AnimalEntity entity = new AnimalEntity();
        entity.setId(animal.getId());
        entity.setNombre(animal.getNombre());
        entity.setEspecie(animal.getEspecie());
        entity.setRaza(animal.getRaza());
        entity.setEdad(animal.getEdad());
        entity.setTamano(animal.getTamano());
        entity.setPersonalidad(animal.getPersonalidad());
        entity.setEstadoSalud(animal.getEstadoSalud());
        entity.setHistoria(animal.getHistoria());
        entity.setEstadoAdopcion(animal.getEstadoAdopcion());
        entity.setRefugioId(animal.getRefugioId());
        entity.setFotos(animal.getFotos());
        entity.setFechaRegistro(animal.getFechaRegistro());
        return entity;
    }
}
