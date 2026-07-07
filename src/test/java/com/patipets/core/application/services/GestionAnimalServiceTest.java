package com.patipets.core.application.services;

import com.patipets.core.application.ports.output.AnimalRepositoryPort;
import com.patipets.core.application.ports.output.EventPublisherPort;
import com.patipets.core.application.ports.output.ImageStoragePort;
import com.patipets.core.application.ports.output.RefugioRepositoryPort;
import com.patipets.core.application.ports.output.SolicitudAdopcionRepositoryPort;
import com.patipets.core.domain.enums.EstadoAnimal;
import com.patipets.core.domain.enums.EstadoPostulacion;
import com.patipets.core.domain.models.Animal;
import com.patipets.core.domain.models.SolicitudAdopcion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GestionAnimalServiceTest {

    @Mock
    private AnimalRepositoryPort animalRepository;
    @Mock
    private SolicitudAdopcionRepositoryPort solicitudRepository;
    @Mock
    private RefugioRepositoryPort refugioRepository;
    @Mock
    private ImageStoragePort imageStoragePort;
    @Mock
    private EventPublisherPort eventPublisher;

    private GestionAnimalService service;

    @BeforeEach
    void setUp() {
        service = new GestionAnimalService(animalRepository, solicitudRepository, refugioRepository,
                imageStoragePort, eventPublisher);
    }

    private Animal animal(Long id, EstadoAnimal estado, Long refugioId) {
        return new Animal(id, "Firulais", "Perro", "Mestizo", 3, "MEDIANO",
                List.of("Juguetón"), List.of("Sano"), "Historia", estado, refugioId,
                null, null, List.of(), LocalDateTime.now(), "MACHO", 12.5);
    }

    private SolicitudAdopcion solicitud(Long id, Long animalId, Long adoptanteId, Long refugioId,
                                         EstadoPostulacion estado) {
        return new SolicitudAdopcion(id, animalId, adoptanteId, refugioId, estado,
                LocalDateTime.now(), LocalDateTime.now(), "Adoptante Uno", "+56911111111", "Calle 1",
                com.patipets.core.domain.enums.NivelActividad.MODERADO, 4,
                com.patipets.core.domain.enums.CuidadoVacaciones.FAMILIAR,
                com.patipets.core.domain.enums.TipoVivienda.CASA_CON_PATIO, "Amplio", false, false, null);
    }

    @Test
    void solicitar_creaSolicitudCuandoAnimalDisponible() {
        Animal disponible = animal(1L, EstadoAnimal.DISPONIBLE, 5L);
        when(animalRepository.findById(1L)).thenReturn(java.util.Optional.of(disponible));
        when(refugioRepository.perteneceAUsuario(5L, 20L)).thenReturn(false);
        when(solicitudRepository.findByAnimalId(1L)).thenReturn(List.of());
        when(solicitudRepository.save(any(SolicitudAdopcion.class))).thenAnswer(inv -> inv.getArgument(0));

        SolicitudAdopcion resultado = service.solicitar(1L, 20L, "Adoptante Uno", "+56911111111",
                "Calle 1", "MODERADO", 4, "FAMILIAR", "CASA_CON_PATIO", "Amplio", false, false, null);

        assertThat(resultado.getEstado()).isEqualTo(EstadoPostulacion.PENDIENTE);
        assertThat(resultado.getAnimalId()).isEqualTo(1L);
    }

    @Test
    void solicitar_rechazaAnimalNoDisponible() {
        Animal enProceso = animal(1L, EstadoAnimal.EN_PROCESO, 5L);
        when(animalRepository.findById(1L)).thenReturn(java.util.Optional.of(enProceso));

        assertThatThrownBy(() -> service.solicitar(1L, 20L, "Adoptante Uno", "+56911111111",
                "Calle 1", "MODERADO", 4, "FAMILIAR", "CASA_CON_PATIO", "Amplio", false, false, null))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("no está disponible");
    }

    @Test
    void solicitar_rechazaAutoAdopcionDeAnimalDelPropioRefugio() {
        Animal disponible = animal(1L, EstadoAnimal.DISPONIBLE, 5L);
        when(animalRepository.findById(1L)).thenReturn(java.util.Optional.of(disponible));
        when(refugioRepository.perteneceAUsuario(5L, 20L)).thenReturn(true);

        assertThatThrownBy(() -> service.solicitar(1L, 20L, "Adoptante Uno", "+56911111111",
                "Calle 1", "MODERADO", 4, "FAMILIAR", "CASA_CON_PATIO", "Amplio", false, false, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("propio refugio");
    }

    @Test
    void solicitar_rechazaSolicitudDuplicadaDelMismoAdoptante() {
        Animal disponible = animal(1L, EstadoAnimal.DISPONIBLE, 5L);
        SolicitudAdopcion existente = solicitud(99L, 1L, 20L, 5L, EstadoPostulacion.PENDIENTE);
        when(animalRepository.findById(1L)).thenReturn(java.util.Optional.of(disponible));
        when(refugioRepository.perteneceAUsuario(5L, 20L)).thenReturn(false);
        when(solicitudRepository.findByAnimalId(1L)).thenReturn(List.of(existente));

        assertThatThrownBy(() -> service.solicitar(1L, 20L, "Adoptante Uno", "+56911111111",
                "Calle 1", "MODERADO", 4, "FAMILIAR", "CASA_CON_PATIO", "Amplio", false, false, null))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Ya tienes una postulación");
    }

    @Test
    void aprobarSolicitud_marcaAnimalEnProcesoYRechazaOtrasSolicitudesPendientes() {
        SolicitudAdopcion pendiente = solicitud(1L, 10L, 20L, 5L, EstadoPostulacion.PENDIENTE);
        SolicitudAdopcion otraPendiente = solicitud(2L, 10L, 21L, 5L, EstadoPostulacion.PENDIENTE);
        Animal disponible = animal(10L, EstadoAnimal.DISPONIBLE, 5L);

        when(solicitudRepository.findById(1L)).thenReturn(java.util.Optional.of(pendiente));
        when(animalRepository.findById(10L)).thenReturn(java.util.Optional.of(disponible));
        when(solicitudRepository.findByAnimalIdAndEstado(10L, "PENDIENTE")).thenReturn(List.of(pendiente, otraPendiente));
        when(solicitudRepository.save(any(SolicitudAdopcion.class))).thenAnswer(inv -> inv.getArgument(0));

        SolicitudAdopcion resultado = service.aprobarSolicitud(1L);

        assertThat(resultado.getEstado()).isEqualTo(EstadoPostulacion.APROBADA);
        verify(animalRepository).save(argThat(a -> a.getEstadoAdopcion() == EstadoAnimal.EN_PROCESO));
        verify(solicitudRepository).save(argThat(s -> s.getId().equals(2L) && s.getEstado() == EstadoPostulacion.RECHAZADA));
        verify(eventPublisher, times(2)).publicar(any());
    }

    @Test
    void aprobarSolicitud_rechazaSiNoEstaPendiente() {
        SolicitudAdopcion yaAprobada = solicitud(1L, 10L, 20L, 5L, EstadoPostulacion.APROBADA);
        when(solicitudRepository.findById(1L)).thenReturn(java.util.Optional.of(yaAprobada));

        assertThatThrownBy(() -> service.aprobarSolicitud(1L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("PENDIENTE");
    }

    @Test
    void confirmarAdopcion_marcaAnimalAdoptadoYSolicitudCompletada() {
        SolicitudAdopcion aprobada = solicitud(1L, 10L, 20L, 5L, EstadoPostulacion.APROBADA);
        Animal enProceso = animal(10L, EstadoAnimal.EN_PROCESO, 5L);
        when(solicitudRepository.findById(1L)).thenReturn(java.util.Optional.of(aprobada));
        when(animalRepository.findById(10L)).thenReturn(java.util.Optional.of(enProceso));
        when(solicitudRepository.save(any(SolicitudAdopcion.class))).thenAnswer(inv -> inv.getArgument(0));

        SolicitudAdopcion resultado = service.confirmarAdopcion(1L);

        assertThat(resultado.getEstado()).isEqualTo(EstadoPostulacion.COMPLETADA);
        verify(animalRepository).save(argThat(a -> a.getEstadoAdopcion() == EstadoAnimal.ADOPTADO));
    }

    @Test
    void confirmarAdopcion_rechazaSiSolicitudNoAprobada() {
        SolicitudAdopcion pendiente = solicitud(1L, 10L, 20L, 5L, EstadoPostulacion.PENDIENTE);
        when(solicitudRepository.findById(1L)).thenReturn(java.util.Optional.of(pendiente));

        assertThatThrownBy(() -> service.confirmarAdopcion(1L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("APROBADA");
    }

    @Test
    void rechazarSolicitud_devuelveAnimalADisponible() {
        SolicitudAdopcion pendiente = solicitud(1L, 10L, 20L, 5L, EstadoPostulacion.PENDIENTE);
        Animal enProceso = animal(10L, EstadoAnimal.EN_PROCESO, 5L);
        when(solicitudRepository.findById(1L)).thenReturn(java.util.Optional.of(pendiente));
        when(animalRepository.findById(10L)).thenReturn(java.util.Optional.of(enProceso));
        when(solicitudRepository.save(any(SolicitudAdopcion.class))).thenAnswer(inv -> inv.getArgument(0));

        SolicitudAdopcion resultado = service.rechazarSolicitud(1L);

        assertThat(resultado.getEstado()).isEqualTo(EstadoPostulacion.RECHAZADA);
        verify(animalRepository).save(argThat(a -> a.getEstadoAdopcion() == EstadoAnimal.DISPONIBLE));
    }

    @Test
    void crearAnimal_rechazaSiRefugioNoAprobado() {
        com.patipets.core.domain.models.Refugio refugioPendiente = new com.patipets.core.domain.models.Refugio(
                5L, "Refugio", "desc", "dir", "Araucanía", "Temuco", -38.7, -72.6, 20,
                "r@patipets.cl", "+56900000000", com.patipets.core.domain.enums.EstadoRefugio.PENDIENTE, null);
        when(refugioRepository.findById(5L)).thenReturn(java.util.Optional.of(refugioPendiente));

        assertThatThrownBy(() -> service.crearAnimal("Firulais", "Perro", "Mestizo", 3, "MEDIANO",
                List.of(), List.of(), "Historia", 5L, List.of(), "MACHO", 12.5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("no está aprobado");
    }

    @Test
    void eliminarAnimal_rechazaSiAnimalNoPerteneceAlRefugio() {
        Animal deOtroRefugio = animal(1L, EstadoAnimal.DISPONIBLE, 99L);
        when(animalRepository.findById(1L)).thenReturn(java.util.Optional.of(deOtroRefugio));

        assertThatThrownBy(() -> service.eliminarAnimal(1L, 5L))
                .isInstanceOf(IllegalArgumentException.class);

        verify(animalRepository, never()).deleteById(any());
    }
}
