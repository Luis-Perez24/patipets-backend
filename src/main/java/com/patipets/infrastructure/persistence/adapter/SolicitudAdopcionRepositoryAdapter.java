package com.patipets.infrastructure.persistence.adapter;

import com.patipets.core.application.ports.output.SolicitudAdopcionRepositoryPort;
import com.patipets.core.domain.enums.CuidadoVacaciones;
import com.patipets.core.domain.enums.EstadoPostulacion;
import com.patipets.core.domain.enums.NivelActividad;
import com.patipets.core.domain.enums.TipoVivienda;
import com.patipets.core.domain.models.SolicitudAdopcion;
import com.patipets.infrastructure.persistence.entity.SolicitudAdopcionEntity;
import com.patipets.infrastructure.persistence.repository.SolicitudAdopcionJpaRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SolicitudAdopcionRepositoryAdapter implements SolicitudAdopcionRepositoryPort {

    private final SolicitudAdopcionJpaRepository jpaRepository;

    public SolicitudAdopcionRepositoryAdapter(SolicitudAdopcionJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public SolicitudAdopcion save(SolicitudAdopcion solicitud) {
        SolicitudAdopcionEntity entity = toEntity(solicitud);
        SolicitudAdopcionEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<SolicitudAdopcion> findById(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<SolicitudAdopcion> findByAdoptanteId(Long adoptanteId) {
        return jpaRepository.findByAdoptanteIdOrderByFechaCreacionDesc(adoptanteId)
                .stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<SolicitudAdopcion> findByRefugioId(Long refugioId) {
        return jpaRepository.findByRefugioIdOrderByFechaCreacionDesc(refugioId)
                .stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<SolicitudAdopcion> findByAnimalId(Long animalId) {
        return jpaRepository.findByAnimalId(animalId)
                .stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<SolicitudAdopcion> findByAnimalIdAndEstado(Long animalId, String estado) {
        return jpaRepository.findByAnimalIdAndEstado(animalId, estado)
                .stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<SolicitudAdopcion> findByEstado(String estado, int page, int size) {
        return jpaRepository.findByEstado(estado,
                        PageRequest.of(page, size, Sort.by("fechaCreacion").descending()))
                .stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public long countByRefugioIdAndEstado(Long refugioId, String estado) {
        return jpaRepository.countByRefugioIdAndEstado(refugioId, estado);
    }

    @Override
    public long countByEstado(String estado) {
        return jpaRepository.countByEstado(estado);
    }

    private SolicitudAdopcion toDomain(SolicitudAdopcionEntity entity) {
        return new SolicitudAdopcion(
                entity.getId(), entity.getAnimalId(), entity.getAdoptanteId(),
                entity.getRefugioId(), EstadoPostulacion.valueOf(entity.getEstado()),
                entity.getFechaCreacion(), entity.getFechaActualizacion(),
                entity.getNombreCompleto(), entity.getNumeroContacto(),
                entity.getDireccion(), NivelActividad.valueOf(entity.getNivelActividad()),
                entity.getHorasSolo(), CuidadoVacaciones.valueOf(entity.getCuidadoVacaciones()),
                TipoVivienda.valueOf(entity.getTipoVivienda()), entity.getDescripcionEspacio(),
                entity.getTieneNinos(), entity.getTieneOtrasMascotas(), entity.getDetalleMascotas()
        );
    }

    private SolicitudAdopcionEntity toEntity(SolicitudAdopcion solicitud) {
        SolicitudAdopcionEntity entity = new SolicitudAdopcionEntity();
        entity.setId(solicitud.getId());
        entity.setAnimalId(solicitud.getAnimalId());
        entity.setAdoptanteId(solicitud.getAdoptanteId());
        entity.setRefugioId(solicitud.getRefugioId());
        entity.setEstado(solicitud.getEstado().name());
        entity.setFechaCreacion(solicitud.getFechaCreacion());
        entity.setFechaActualizacion(solicitud.getFechaActualizacion());
        entity.setNombreCompleto(solicitud.getNombreCompleto());
        entity.setNumeroContacto(solicitud.getNumeroContacto());
        entity.setDireccion(solicitud.getDireccion());
        entity.setNivelActividad(solicitud.getNivelActividad().name());
        entity.setHorasSolo(solicitud.getHorasSolo());
        entity.setCuidadoVacaciones(solicitud.getCuidadoVacaciones().name());
        entity.setTipoVivienda(solicitud.getTipoVivienda().name());
        entity.setDescripcionEspacio(solicitud.getDescripcionEspacio());
        entity.setTieneNinos(solicitud.getTieneNinos());
        entity.setTieneOtrasMascotas(solicitud.getTieneOtrasMascotas());
        entity.setDetalleMascotas(solicitud.getDetalleMascotas());
        return entity;
    }
}
