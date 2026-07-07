package com.patipets.core.application.services;

import com.patipets.core.application.ports.output.AlertaRepositoryPort;
import com.patipets.core.application.ports.output.EventPublisherPort;
import com.patipets.core.application.ports.output.RespuestaAlertaRepositoryPort;
import com.patipets.core.domain.enums.NivelUrgencia;
import com.patipets.core.domain.enums.TipoAyudaVoluntariado;
import com.patipets.core.domain.models.Alerta;
import com.patipets.core.domain.models.RespuestaAlerta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GestionAlertaServiceTest {

    @Mock
    private AlertaRepositoryPort alertaRepository;
    @Mock
    private EventPublisherPort eventPublisher;
    @Mock
    private RespuestaAlertaRepositoryPort respuestaRepository;

    private GestionAlertaService service;

    @BeforeEach
    void setUp() {
        service = new GestionAlertaService(alertaRepository, eventPublisher, respuestaRepository);
    }

    private Alerta alerta(Long id, boolean activa, Long refugioId) {
        return new Alerta(id, "Necesitamos alimento", "desc", NivelUrgencia.URGENTE,
                refugioId, 1L, activa, LocalDateTime.now());
    }

    private RespuestaAlerta respuesta(Long id, Long alertaId, Long usuarioId, String estado) {
        return new RespuestaAlerta(id, alertaId, usuarioId, TipoAyudaVoluntariado.PASEOS,
                "Puedo ayudar", "Fines de semana", LocalDateTime.now(), estado, null, null);
    }

    @Test
    void crear_rechazaTituloVacio() {
        assertThatThrownBy(() -> service.crear("", "desc", "URGENTE", 5L, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("título");
    }

    @Test
    void crear_rechazaNivelUrgenciaInvalido() {
        assertThatThrownBy(() -> service.crear("Título", "desc", "CRITICO", 5L, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Nivel de urgencia inválido");
    }

    @Test
    void crear_publicaEventoAlCrearAlerta() {
        when(alertaRepository.save(any(Alerta.class))).thenAnswer(inv -> {
            Alerta a = inv.getArgument(0);
            return new Alerta(1L, a.getTitulo(), a.getDescripcion(), a.getNivelUrgencia(),
                    a.getRefugioId(), a.getCreadoPor(), a.isActiva(), a.getCreatedAt());
        });

        Alerta resultado = service.crear("Necesitamos alimento", "desc", "URGENTE", 5L, 1L);

        assertThat(resultado.getId()).isEqualTo(1L);
        verify(eventPublisher).publicar(any());
    }

    @Test
    void marcarResuelta_rechazaSiYaEstaResuelta() {
        Alerta resuelta = alerta(1L, false, 5L);
        when(alertaRepository.findById(1L)).thenReturn(Optional.of(resuelta));

        assertThatThrownBy(() -> service.marcarResuelta(1L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("ya está resuelta");
    }

    @Test
    void responder_rechazaRespuestaDuplicadaDelMismoUsuario() {
        Alerta activa = alerta(1L, true, 5L);
        when(alertaRepository.findById(1L)).thenReturn(Optional.of(activa));
        when(respuestaRepository.existsByAlertaIdAndUsuarioId(1L, 20L)).thenReturn(true);

        assertThatThrownBy(() -> service.responder(1L, 20L, "PASEOS", "mensaje", "fin de semana"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Ya has respondido");
    }

    @Test
    void responder_rechazaSiAlertaEstaResuelta() {
        Alerta resuelta = alerta(1L, false, 5L);
        when(alertaRepository.findById(1L)).thenReturn(Optional.of(resuelta));

        assertThatThrownBy(() -> service.responder(1L, 20L, "PASEOS", "mensaje", null))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("resuelta");
    }

    @Test
    void aceptarRespuesta_rechazaSiInscripcionNoPerteneceAlRefugio() {
        RespuestaAlerta pendiente = respuesta(1L, 10L, 20L, "PENDIENTE");
        Alerta deOtroRefugio = alerta(10L, true, 99L);
        when(respuestaRepository.findById(1L)).thenReturn(Optional.of(pendiente));
        when(alertaRepository.findById(10L)).thenReturn(Optional.of(deOtroRefugio));

        assertThatThrownBy(() -> service.aceptarRespuesta(1L, 5L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("no pertenece a este refugio");
    }

    @Test
    void aceptarRespuesta_rechazaSiYaFueAceptada() {
        RespuestaAlerta yaAceptada = respuesta(1L, 10L, 20L, "ACEPTADA");
        Alerta delRefugio = alerta(10L, true, 5L);
        when(respuestaRepository.findById(1L)).thenReturn(Optional.of(yaAceptada));
        when(alertaRepository.findById(10L)).thenReturn(Optional.of(delRefugio));

        assertThatThrownBy(() -> service.aceptarRespuesta(1L, 5L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("ya fue aceptada");
    }

    @Test
    void cancelarRespuestaPorVoluntario_rechazaSiNoLePertenece() {
        RespuestaAlerta deOtroUsuario = respuesta(1L, 10L, 99L, "PENDIENTE");
        when(respuestaRepository.findById(1L)).thenReturn(Optional.of(deOtroUsuario));

        assertThatThrownBy(() -> service.cancelarRespuestaPorVoluntario(1L, 20L, "motivo"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("no te pertenece");
    }

    @Test
    void cancelarRespuestaPorVoluntario_marcaComoCanceladaYPublicaEvento() {
        RespuestaAlerta pendiente = respuesta(1L, 10L, 20L, "PENDIENTE");
        Alerta delRefugio = alerta(10L, true, 5L);
        when(respuestaRepository.findById(1L)).thenReturn(Optional.of(pendiente));
        when(alertaRepository.findById(10L)).thenReturn(Optional.of(delRefugio));
        when(respuestaRepository.save(any(RespuestaAlerta.class))).thenAnswer(inv -> inv.getArgument(0));

        RespuestaAlerta resultado = service.cancelarRespuestaPorVoluntario(1L, 20L, "Ya no puedo");

        assertThat(resultado.getEstado()).isEqualTo("CANCELADA");
        verify(eventPublisher).publicar(any());
    }
}
