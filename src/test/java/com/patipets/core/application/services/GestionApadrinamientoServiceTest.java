package com.patipets.core.application.services;

import com.patipets.core.application.ports.output.AnimalRepositoryPort;
import com.patipets.core.application.ports.output.ApadrinamientoRepositoryPort;
import com.patipets.core.application.ports.output.EventPublisherPort;
import com.patipets.core.application.ports.output.RefugioRepositoryPort;
import com.patipets.core.domain.enums.EstadoAnimal;
import com.patipets.core.domain.enums.EstadoRefugio;
import com.patipets.core.domain.enums.TipoApoyo;
import com.patipets.core.domain.models.Animal;
import com.patipets.core.domain.models.Apadrinamiento;
import com.patipets.core.domain.models.Refugio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GestionApadrinamientoServiceTest {

    @Mock
    private ApadrinamientoRepositoryPort apadrinamientoRepository;
    @Mock
    private AnimalRepositoryPort animalRepository;
    @Mock
    private RefugioRepositoryPort refugioRepository;
    @Mock
    private EventPublisherPort eventPublisher;

    private GestionApadrinamientoService service;

    @BeforeEach
    void setUp() {
        service = new GestionApadrinamientoService(apadrinamientoRepository, animalRepository,
                refugioRepository, eventPublisher);
    }

    private Animal animal(Long id, EstadoAnimal estado, Long refugioId) {
        return new Animal(id, "Firulais", "Perro", "Mestizo", 3, "MEDIANO",
                List.of(), List.of(), "Historia", estado, refugioId,
                null, null, List.of(), LocalDateTime.now(), "MACHO", 12.5);
    }

    private Apadrinamiento apadrinamiento(Long id, Long padrinoId, Long animalId, Long refugioId, boolean activo) {
        return new Apadrinamiento(id, padrinoId, animalId, refugioId, TipoApoyo.VISITAS,
                "Visita semanal", LocalDateTime.now(), activo);
    }

    @Test
    void apadrinar_creaApadrinamientoActivo() {
        Animal disponible = animal(1L, EstadoAnimal.DISPONIBLE, 5L);
        Refugio refugio = new Refugio(5L, "Refugio", "desc", "dir", "Araucanía", "Temuco",
                -38.7, -72.6, 20, "r@patipets.cl", "+56900000000", EstadoRefugio.APROBADO, null);
        when(animalRepository.findById(1L)).thenReturn(Optional.of(disponible));
        when(apadrinamientoRepository.existsByPadrinoIdAndAnimalIdAndActivoTrue(10L, 1L)).thenReturn(false);
        when(refugioRepository.perteneceAUsuario(5L, 10L)).thenReturn(false);
        when(refugioRepository.findById(5L)).thenReturn(Optional.of(refugio));
        when(apadrinamientoRepository.save(any(Apadrinamiento.class))).thenAnswer(inv -> inv.getArgument(0));

        Apadrinamiento resultado = service.apadrinar(10L, 1L, "VISITAS", "Visita semanal");

        assertThat(resultado.isActivo()).isTrue();
        assertThat(resultado.getTipoApoyo()).isEqualTo(TipoApoyo.VISITAS);
    }

    @Test
    void apadrinar_rechazaAnimalNoDisponible() {
        Animal enProceso = animal(1L, EstadoAnimal.EN_PROCESO, 5L);
        when(animalRepository.findById(1L)).thenReturn(Optional.of(enProceso));

        assertThatThrownBy(() -> service.apadrinar(10L, 1L, "VISITAS", "Compromiso"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("no está disponible");
    }

    @Test
    void apadrinar_rechazaDuplicado() {
        Animal disponible = animal(1L, EstadoAnimal.DISPONIBLE, 5L);
        when(animalRepository.findById(1L)).thenReturn(Optional.of(disponible));
        when(apadrinamientoRepository.existsByPadrinoIdAndAnimalIdAndActivoTrue(10L, 1L)).thenReturn(true);

        assertThatThrownBy(() -> service.apadrinar(10L, 1L, "VISITAS", "Compromiso"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Ya estás apadrinando");
    }

    @Test
    void apadrinar_rechazaAutoApadrinamientoDelPropioRefugio() {
        Animal disponible = animal(1L, EstadoAnimal.DISPONIBLE, 5L);
        when(animalRepository.findById(1L)).thenReturn(Optional.of(disponible));
        when(apadrinamientoRepository.existsByPadrinoIdAndAnimalIdAndActivoTrue(10L, 1L)).thenReturn(false);
        when(refugioRepository.perteneceAUsuario(5L, 10L)).thenReturn(true);

        assertThatThrownBy(() -> service.apadrinar(10L, 1L, "VISITAS", "Compromiso"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("propio refugio");
    }

    @Test
    void apadrinar_rechazaTipoApoyoInvalido() {
        Animal disponible = animal(1L, EstadoAnimal.DISPONIBLE, 5L);
        Refugio refugio = new Refugio(5L, "Refugio", "desc", "dir", "Araucanía", "Temuco",
                -38.7, -72.6, 20, "r@patipets.cl", "+56900000000", EstadoRefugio.APROBADO, null);
        when(animalRepository.findById(1L)).thenReturn(Optional.of(disponible));
        when(apadrinamientoRepository.existsByPadrinoIdAndAnimalIdAndActivoTrue(10L, 1L)).thenReturn(false);
        when(refugioRepository.perteneceAUsuario(5L, 10L)).thenReturn(false);
        when(refugioRepository.findById(5L)).thenReturn(Optional.of(refugio));

        assertThatThrownBy(() -> service.apadrinar(10L, 1L, "DINERO", "Compromiso"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Tipo de apoyo inválido");
    }

    @Test
    void cancelarPorPadrino_rechazaSiNoLePertenece() {
        Apadrinamiento deOtroPadrino = apadrinamiento(1L, 99L, 5L, 20L, true);
        when(apadrinamientoRepository.findById(1L)).thenReturn(Optional.of(deOtroPadrino));

        assertThatThrownBy(() -> service.cancelarPorPadrino(1L, 10L, "motivo"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("no te pertenece");
    }

    @Test
    void cancelarPorPadrino_rechazaSiYaEstaCancelado() {
        Apadrinamiento cancelado = apadrinamiento(1L, 10L, 5L, 20L, false);
        when(apadrinamientoRepository.findById(1L)).thenReturn(Optional.of(cancelado));

        assertThatThrownBy(() -> service.cancelarPorPadrino(1L, 10L, "motivo"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("ya está cancelado");
    }

    @Test
    void cancelarPorPadrino_marcaInactivoYPublicaEvento() {
        Apadrinamiento activo = apadrinamiento(1L, 10L, 5L, 20L, true);
        Animal elAnimal = animal(5L, EstadoAnimal.DISPONIBLE, 20L);
        when(apadrinamientoRepository.findById(1L)).thenReturn(Optional.of(activo));
        when(animalRepository.findById(5L)).thenReturn(Optional.of(elAnimal));
        when(apadrinamientoRepository.save(any(Apadrinamiento.class))).thenAnswer(inv -> inv.getArgument(0));

        Apadrinamiento resultado = service.cancelarPorPadrino(1L, 10L, "Ya no puedo");

        assertThat(resultado.isActivo()).isFalse();
        verify(eventPublisher).publicar(any());
    }

    @Test
    void cancelarPorRefugio_rechazaSiNoPerteneceAlRefugio() {
        Apadrinamiento deOtroRefugio = apadrinamiento(1L, 10L, 5L, 99L, true);
        when(apadrinamientoRepository.findById(1L)).thenReturn(Optional.of(deOtroRefugio));

        assertThatThrownBy(() -> service.cancelarPorRefugio(1L, 20L, "motivo"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("no pertenece a este refugio");
    }
}
