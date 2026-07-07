package com.patipets.infrastructure.persistence.adapter;

import com.patipets.core.application.ports.output.AnimalRepositoryPort;
import com.patipets.core.domain.enums.EstadoAnimal;
import com.patipets.core.domain.models.Animal;
import com.patipets.core.domain.models.PaginatedResult;
import com.patipets.infrastructure.persistence.entity.AnimalEntity;
import com.patipets.infrastructure.persistence.repository.AnimalJpaRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
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
    public List<Animal> findByRefugioIdAndEstado(Long refugioId, String estado) {
        return jpaRepository.findByRefugioIdAndEstadoAdopcion(refugioId, EstadoAnimal.valueOf(estado))
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

    @Override
    public PaginatedResult<Animal> findAll(String estado, Long refugioId, String especie,
                                            String busqueda, int page, int size) {
        Specification<AnimalEntity> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (estado != null && !estado.isEmpty()) {
                predicates.add(cb.equal(root.get("estadoAdopcion"), EstadoAnimal.valueOf(estado)));
            }
            if (refugioId != null) {
                predicates.add(cb.equal(root.get("refugioId"), refugioId));
            }
            if (especie != null && !especie.isEmpty()) {
                predicates.add(cb.equal(root.get("especie"), especie));
            }
            if (busqueda != null && !busqueda.isEmpty()) {
                String pattern = "%" + busqueda.toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("nombre")), pattern),
                        cb.like(cb.lower(root.get("raza")), pattern)
                ));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        Page<AnimalEntity> pageResult = jpaRepository.findAll(spec,
                PageRequest.of(page, size, Sort.by("fechaRegistro").descending()));
        List<Animal> items = pageResult.getContent().stream()
                .map(this::toDomain)
                .toList();
        return new PaginatedResult<>(items, pageResult.getTotalPages(),
                pageResult.getTotalElements(), pageResult.getNumber(), pageResult.getSize());
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
                entity.getFechaRegistro(),
                entity.getSexo(),
                entity.getPeso()
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
        entity.setSexo(animal.getSexo());
        entity.setPeso(animal.getPeso());
        return entity;
    }
}
