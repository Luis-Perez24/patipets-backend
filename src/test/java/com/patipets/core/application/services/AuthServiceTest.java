package com.patipets.core.application.services;

import com.patipets.core.application.ports.output.ImageStoragePort;
import com.patipets.core.application.ports.output.PasswordEncoderPort;
import com.patipets.core.application.ports.output.RefugioRepositoryPort;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioRepositoryPort usuarioRepository;
    @Mock
    private PasswordEncoderPort passwordEncoder;
    @Mock
    private ImageStoragePort imageStoragePort;
    @Mock
    private RefugioRepositoryPort refugioRepository;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(usuarioRepository, passwordEncoder, imageStoragePort, refugioRepository);
    }

    private Usuario usuarioBase(Rol rol, boolean activo) {
        return new Usuario(1L, "Luis", "luis@patipets.cl", "hashed",
                rol, null, "+56911111111", null,
                "Araucanía", "Temuco", "Calle 123", null, activo, LocalDateTime.now());
    }

    @Test
    void registrar_creaUsuarioConPasswordEncriptadaYRolCiudadano() {
        when(usuarioRepository.existsByEmail("nuevo@patipets.cl")).thenReturn(false);
        when(passwordEncoder.encode("clave123")).thenReturn("hash-clave123");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        Usuario resultado = authService.registrar("Nuevo", "nuevo@patipets.cl", "clave123",
                "+56922222222", "Araucanía", "Temuco", "Calle 456");

        assertThat(resultado.getRol()).isEqualTo(Rol.CIUDADANO);
        assertThat(resultado.getPassword()).isEqualTo("hash-clave123");
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void registrar_rechazaEmailYaRegistrado() {
        when(usuarioRepository.existsByEmail("existente@patipets.cl")).thenReturn(true);

        assertThatThrownBy(() -> authService.registrar("X", "existente@patipets.cl", "clave123",
                "+56900000000", "Araucanía", "Temuco", "Calle 1"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ya está registrado");

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void autenticar_devuelveUsuarioConCredencialesValidas() {
        Usuario usuario = usuarioBase(Rol.CIUDADANO, true);
        when(usuarioRepository.findByEmail("luis@patipets.cl")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("clave123", "hashed")).thenReturn(true);
        when(refugioRepository.findByUsuario(1L)).thenReturn(Collections.emptyList());

        Usuario resultado = authService.autenticar("luis@patipets.cl", "clave123");

        assertThat(resultado.getEmail()).isEqualTo("luis@patipets.cl");
    }

    @Test
    void autenticar_rechazaPasswordIncorrecta() {
        Usuario usuario = usuarioBase(Rol.CIUDADANO, true);
        when(usuarioRepository.findByEmail("luis@patipets.cl")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("mala-clave", "hashed")).thenReturn(false);

        assertThatThrownBy(() -> authService.autenticar("luis@patipets.cl", "mala-clave"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Credenciales inválidas");
    }

    @Test
    void autenticar_rechazaCuentaDesactivada() {
        Usuario usuario = usuarioBase(Rol.CIUDADANO, false);
        when(usuarioRepository.findByEmail("luis@patipets.cl")).thenReturn(Optional.of(usuario));

        assertThatThrownBy(() -> authService.autenticar("luis@patipets.cl", "clave123"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("desactivada");

        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    void activarRol_rechazaAutoAsignacionDeAdmin() {
        Usuario usuario = usuarioBase(Rol.CIUDADANO, true);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        assertThatThrownBy(() -> authService.activarRol(1L, "ADMIN"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ADMIN");

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void activarRol_rechazaRolRefugioSinRefugioAprobado() {
        Usuario usuario = usuarioBase(Rol.CIUDADANO, true);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(refugioRepository.findByUsuario(1L)).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> authService.activarRol(1L, "REFUGIO"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("refugio aprobado");
    }

    @Test
    void activarRol_permiteRolRefugioConRefugioAprobado() {
        Usuario usuario = usuarioBase(Rol.CIUDADANO, true);
        Refugio refugioAprobado = new Refugio(10L, "Refugio Sur", "desc", "dir", "Araucanía",
                "Temuco", -38.7, -72.6, 20, "r@patipets.cl", "+56933333333", EstadoRefugio.APROBADO, null);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(refugioRepository.findByUsuario(1L)).thenReturn(List.of(refugioAprobado));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        Usuario resultado = authService.activarRol(1L, "refugio");

        assertThat(resultado.getRol()).isEqualTo(Rol.REFUGIO);
    }

    @Test
    void activarRol_rechazaRolInexistente() {
        Usuario usuario = usuarioBase(Rol.CIUDADANO, true);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        assertThatThrownBy(() -> authService.activarRol(1L, "SUPERUSUARIO"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Rol inválido");
    }

    @Test
    void restablecerPassword_rechazaTokenExpirado() {
        Usuario usuario = usuarioBase(Rol.CIUDADANO, true);
        usuario.setResetToken("token-abc");
        usuario.setResetTokenExpiry(LocalDateTime.now().minusMinutes(1));
        when(usuarioRepository.findByResetToken("token-abc")).thenReturn(Optional.of(usuario));

        assertThatThrownBy(() -> authService.restablecerPassword("token-abc", "nuevaClave123"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("expirado");
    }

    @Test
    void restablecerPassword_actualizaPasswordYLimpiaToken() {
        Usuario usuario = usuarioBase(Rol.CIUDADANO, true);
        usuario.setResetToken("token-abc");
        usuario.setResetTokenExpiry(LocalDateTime.now().plusMinutes(30));
        when(usuarioRepository.findByResetToken("token-abc")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.encode("nuevaClave123")).thenReturn("hash-nueva");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        authService.restablecerPassword("token-abc", "nuevaClave123");

        verify(usuarioRepository).save(argThat(u ->
                u.getPassword().equals("hash-nueva")
                        && u.getResetToken() == null
                        && u.getResetTokenExpiry() == null));
    }

    @Test
    void solicitarRecuperacionPassword_rechazaEmailInexistente() {
        when(usuarioRepository.findByEmail("no-existe@patipets.cl")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.solicitarRecuperacionPassword("no-existe@patipets.cl"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void eliminarCuenta_rechazaUsuarioInexistente() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.eliminarCuenta(99L))
                .isInstanceOf(IllegalArgumentException.class);

        verify(usuarioRepository, never()).deleteById(any());
    }
}
