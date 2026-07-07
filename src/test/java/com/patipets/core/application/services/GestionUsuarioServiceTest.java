package com.patipets.core.application.services;

import com.patipets.core.application.ports.output.UsuarioRepositoryPort;
import com.patipets.core.domain.enums.Rol;
import com.patipets.core.domain.models.Usuario;
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
class GestionUsuarioServiceTest {

    @Mock
    private UsuarioRepositoryPort usuarioRepository;

    private GestionUsuarioService service;

    @BeforeEach
    void setUp() {
        service = new GestionUsuarioService(usuarioRepository);
    }

    private Usuario usuario(Long id, boolean activo) {
        return new Usuario(id, "Luis", "luis@patipets.cl", "hash", Rol.CIUDADANO,
                null, "+56911111111", null, "Araucanía", "Temuco", "Calle 1", null, activo, LocalDateTime.now());
    }

    @Test
    void bloquear_desactivaUsuarioActivo() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario(1L, true)));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        Usuario resultado = service.bloquear(1L);

        assertThat(resultado.isActivo()).isFalse();
    }

    @Test
    void bloquear_rechazaUsuarioYaBloqueado() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario(1L, false)));

        assertThatThrownBy(() -> service.bloquear(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ya está bloqueado");
    }

    @Test
    void desbloquear_activaUsuarioBloqueado() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario(1L, false)));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        Usuario resultado = service.desbloquear(1L);

        assertThat(resultado.isActivo()).isTrue();
    }

    @Test
    void desbloquear_rechazaUsuarioYaActivo() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario(1L, true)));

        assertThatThrownBy(() -> service.desbloquear(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ya está activo");
    }

    @Test
    void editar_mantieneValoresOriginalesCuandoNoSeProvistos() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario(1L, true)));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        Usuario resultado = service.editar(1L, null, null, null, "+56922222222", null, null);

        assertThat(resultado.getNombre()).isEqualTo("Luis");
        assertThat(resultado.getNumeroContacto()).isEqualTo("+56922222222");
    }

    @Test
    void editar_cambiaRolCuandoSeProvisto() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario(1L, true)));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        Usuario resultado = service.editar(1L, null, null, "ADMIN", null, null, null);

        assertThat(resultado.getRol()).isEqualTo(Rol.ADMIN);
    }

    @Test
    void obtener_rechazaUsuarioInexistente() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.obtener(99L))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
