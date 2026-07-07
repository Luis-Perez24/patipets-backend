package com.patipets.core.application.services;

import com.patipets.core.application.ports.output.AnimalRepositoryPort;
import com.patipets.core.application.ports.output.EventPublisherPort;
import com.patipets.core.application.ports.output.ImageStoragePort;
import com.patipets.core.application.ports.output.RefugioRepositoryPort;
import com.patipets.core.application.ports.output.SolicitudAdopcionRepositoryPort;
import com.patipets.core.application.ports.output.UsuarioRepositoryPort;
import com.patipets.core.domain.enums.EstadoRefugio;
import com.patipets.core.domain.enums.Rol;
import com.patipets.core.domain.models.Refugio;
import com.patipets.core.domain.models.Usuario;
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
class GestionRefugioServiceTest {

    @Mock
    private RefugioRepositoryPort refugioRepository;
    @Mock
    private AnimalRepositoryPort animalRepository;
    @Mock
    private SolicitudAdopcionRepositoryPort solicitudRepository;
    @Mock
    private ImageStoragePort imageStoragePort;
    @Mock
    private UsuarioRepositoryPort usuarioRepository;
    @Mock
    private EventPublisherPort eventPublisher;
    @Mock
    private GeocodingService geocodingService;

    private GestionRefugioService service;

    @BeforeEach
    void setUp() {
        service = new GestionRefugioService(refugioRepository, animalRepository, solicitudRepository,
                imageStoragePort, usuarioRepository, eventPublisher, geocodingService);
    }

    private Refugio refugio(Long id, EstadoRefugio estado, Long usuarioId) {
        return new Refugio(id, "Refugio Sur", "desc", "Calle 1", "Araucanía", "Temuco",
                -38.7, -72.6, 20, "r@patipets.cl", "+56900000000", estado, null,
                usuarioId, "Dueño", LocalDateTime.now());
    }

    @Test
    void solicitar_rechazaSiUsuarioYaTieneSolicitudActiva() {
        when(refugioRepository.findSolicitudesByUsuario(1L))
                .thenReturn(List.of(refugio(5L, EstadoRefugio.PENDIENTE, 1L)));

        assertThatThrownBy(() -> service.solicitar("Nuevo Refugio", "desc", "Calle 1", "Araucanía",
                "Temuco", -38.7, -72.6, 20, "r@patipets.cl", "+56900000000", 1L, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Ya tienes una solicitud");
    }

    @Test
    void solicitar_creaRefugioPendienteConCoordenadasProvistas() throws Exception {
        when(refugioRepository.findSolicitudesByUsuario(1L)).thenReturn(List.of());
        when(refugioRepository.save(any(Refugio.class))).thenAnswer(inv -> inv.getArgument(0));

        Refugio resultado = service.solicitar("Nuevo Refugio", "desc", "Calle 1", "Araucanía",
                "Temuco", -38.7, -72.6, 20, "r@patipets.cl", "+56900000000", 1L, null);

        assertThat(resultado.getEstado()).isEqualTo(EstadoRefugio.PENDIENTE);
        verify(geocodingService, never()).geocode(any(), any(), any());
    }

    @Test
    void solicitar_geocodificaCuandoNoHayCoordenadas() throws Exception {
        when(refugioRepository.findSolicitudesByUsuario(1L)).thenReturn(List.of());
        when(geocodingService.geocode("Calle 1", "Temuco", "Araucanía"))
                .thenReturn(Optional.of(new double[]{-38.7, -72.6}));
        when(refugioRepository.save(any(Refugio.class))).thenAnswer(inv -> inv.getArgument(0));

        Refugio resultado = service.solicitar("Nuevo Refugio", "desc", "Calle 1", "Araucanía",
                "Temuco", null, null, 20, "r@patipets.cl", "+56900000000", 1L, null);

        assertThat(resultado.getLatitud()).isEqualTo(-38.7);
        assertThat(resultado.getLongitud()).isEqualTo(-72.6);
    }

    @Test
    void aprobar_cambiaEstadoYPromueveUsuarioARolRefugio() {
        Refugio pendiente = refugio(5L, EstadoRefugio.PENDIENTE, 1L);
        Usuario ciudadano = new Usuario(1L, "Luis", "luis@patipets.cl", "hash", Rol.CIUDADANO,
                null, "+56911111111", null, "Araucanía", "Temuco", "Calle 1", null, true, LocalDateTime.now());
        when(refugioRepository.findById(5L)).thenReturn(Optional.of(pendiente));
        when(refugioRepository.save(any(Refugio.class))).thenAnswer(inv -> inv.getArgument(0));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(ciudadano));

        Refugio resultado = service.aprobar(5L);

        assertThat(resultado.getEstado()).isEqualTo(EstadoRefugio.APROBADO);
        verify(refugioRepository).vincularUsuario(1L, 5L);
        verify(usuarioRepository).save(argThat(u -> u.getRol() == Rol.REFUGIO));
        verify(eventPublisher).publicar(any());
    }

    @Test
    void aprobar_rechazaSiRefugioNoEstaPendiente() {
        Refugio yaAprobado = refugio(5L, EstadoRefugio.APROBADO, 1L);
        when(refugioRepository.findById(5L)).thenReturn(Optional.of(yaAprobado));

        assertThatThrownBy(() -> service.aprobar(5L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("PENDIENTE");
    }

    @Test
    void rechazar_marcaRefugioComoRechazado() {
        Refugio pendiente = refugio(5L, EstadoRefugio.PENDIENTE, 1L);
        when(refugioRepository.findById(5L)).thenReturn(Optional.of(pendiente));
        when(refugioRepository.save(any(Refugio.class))).thenAnswer(inv -> inv.getArgument(0));

        Refugio resultado = service.rechazar(5L);

        assertThat(resultado.getEstado()).isEqualTo(EstadoRefugio.RECHAZADO);
    }

    @Test
    void cancelarPorUsuario_rechazaSiNoPerteneceAlUsuario() {
        Refugio deOtroUsuario = refugio(5L, EstadoRefugio.PENDIENTE, 99L);
        when(refugioRepository.findById(5L)).thenReturn(Optional.of(deOtroUsuario));

        assertThatThrownBy(() -> service.cancelarPorUsuario(5L, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("no te pertenece");
    }

    @Test
    void actualizar_rechazaSiRefugioNoEstaAprobado() {
        Refugio pendiente = refugio(5L, EstadoRefugio.PENDIENTE, 1L);
        when(refugioRepository.findById(5L)).thenReturn(Optional.of(pendiente));

        assertThatThrownBy(() -> service.actualizar(5L, "Nuevo Nombre", null, null, null, null,
                null, null, null, null, null, null))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("aprobado");
    }

    @Test
    void obtenerHistorial_rechazaSiRefugioNoAprobado() {
        Refugio pendiente = refugio(5L, EstadoRefugio.PENDIENTE, 1L);
        when(refugioRepository.findById(5L)).thenReturn(Optional.of(pendiente));

        assertThatThrownBy(() -> service.obtenerHistorial(5L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("no está aprobado");
    }
}
