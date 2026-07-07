package com.patipets.infrastructure.web.controller;

import com.patipets.core.application.useCase.AuthUseCase;
import com.patipets.core.domain.models.Usuario;
import com.patipets.infrastructure.security.JwtTokenProvider;
import com.patipets.infrastructure.web.dto.ApiResponseDTO;
import com.patipets.infrastructure.web.dto.request.ActivarRolRequestDTO;
import com.patipets.infrastructure.web.dto.request.ActualizarPerfilRequestDTO;
import com.patipets.infrastructure.web.dto.request.LoginRequestDTO;
import com.patipets.infrastructure.web.dto.request.RegisterRequestDTO;
import com.patipets.infrastructure.web.dto.response.AuthResponseDTO;
import com.patipets.infrastructure.web.dto.response.UsuarioResponseDTO;
import com.patipets.infrastructure.web.dto.request.RecuperarRequestDTO;
import com.patipets.infrastructure.web.dto.request.RestablecerRequestDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthUseCase authUseCase;
    private final JwtTokenProvider tokenProvider;

    public AuthController(AuthUseCase authUseCase, JwtTokenProvider tokenProvider) {
        this.authUseCase = authUseCase;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponseDTO<AuthResponseDTO>> register(
            @Valid @RequestBody RegisterRequestDTO request) {
        try {
            Usuario usuario = authUseCase.registrar(
                    request.getNombre(), request.getEmail(), request.getPassword(),
                    request.getNumeroContacto(), request.getRegion(),
                    request.getComuna(), request.getDireccion());
            String token = tokenProvider.generateToken(usuario);
            AuthResponseDTO response = new AuthResponseDTO(token,
                    UsuarioResponseDTO.fromDomain(usuario));
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponseDTO.ok("Registro exitoso", response));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO<AuthResponseDTO>> login(
            @Valid @RequestBody LoginRequestDTO request) {
        try {
            Usuario usuario = authUseCase.autenticar(
                    request.getEmail(), request.getPassword());
            String token = tokenProvider.generateToken(usuario);
            AuthResponseDTO response = new AuthResponseDTO(token,
                    UsuarioResponseDTO.fromDomain(usuario));
            return ResponseEntity.ok(ApiResponseDTO.ok("Inicio de sesión exitoso", response));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @PostMapping("/activar-rol")
    public ResponseEntity<ApiResponseDTO<UsuarioResponseDTO>> activarRol(
            Authentication authentication,
            @Valid @RequestBody ActivarRolRequestDTO request) {
        try {
            Usuario usuarioActual = (Usuario) authentication.getPrincipal();
            Usuario usuario = authUseCase.activarRol(usuarioActual.getId(), request.getRol());
            return ResponseEntity.ok(ApiResponseDTO.ok("Rol actualizado",
                    UsuarioResponseDTO.fromDomain(usuario)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponseDTO<Void>> logout() {
        return ResponseEntity.ok(ApiResponseDTO.ok("Sesión cerrada exitosamente", null));
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponseDTO<UsuarioResponseDTO>> actualizarPerfil(
            Authentication authentication,
            @Valid @RequestBody ActualizarPerfilRequestDTO request) {
        try {
            Usuario usuarioActual = (Usuario) authentication.getPrincipal();
            Usuario usuario = authUseCase.actualizarPerfil(
                    usuarioActual.getId(), request.getNombre(),
                    request.getNumeroContacto(), request.getRegion(),
                    request.getComuna(), request.getDireccion(),
                    request.getBiografia(), request.getFotoPerfil());
            return ResponseEntity.ok(ApiResponseDTO.ok("Perfil actualizado",
                    UsuarioResponseDTO.fromDomain(usuario)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @PostMapping(value = "/me/foto", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponseDTO<UsuarioResponseDTO>> actualizarFotoPerfil(
            Authentication authentication,
            @RequestPart("archivo") MultipartFile archivo) {
        try {
            Usuario usuarioActual = (Usuario) authentication.getPrincipal();
            Usuario usuario = authUseCase.actualizarFotoPerfil(usuarioActual.getId(), archivo);
            return ResponseEntity.ok(ApiResponseDTO.ok("Foto de perfil actualizada",
                    UsuarioResponseDTO.fromDomain(usuario)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error(e.getMessage()));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Error al procesar la imagen: " + e.getMessage()));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponseDTO<UsuarioResponseDTO>> me(
            Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        return ResponseEntity.ok(
                ApiResponseDTO.ok(UsuarioResponseDTO.fromDomain(usuario)));
    }

    @DeleteMapping("/me")
    public ResponseEntity<ApiResponseDTO<Void>> eliminarCuenta(Authentication authentication) {
        try {
            Usuario usuarioActual = (Usuario) authentication.getPrincipal();
            authUseCase.eliminarCuenta(usuarioActual.getId());
            return ResponseEntity.ok(ApiResponseDTO.ok("Cuenta eliminada exitosamente", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @PostMapping("/recuperar")
    public ResponseEntity<ApiResponseDTO<Void>> solicitarRecuperacion(
            @Valid @RequestBody RecuperarRequestDTO request) {
        try {
            authUseCase.solicitarRecuperacionPassword(request.getEmail());
        } catch (IllegalArgumentException | IllegalStateException e) {
            // No revelar si el correo existe o si la cuenta está desactivada (previene user enumeration)
        }
        return ResponseEntity.ok(ApiResponseDTO.ok("Si el correo existe, se enviará un enlace de recuperación.", null));
    }

    @PostMapping("/restablecer")
    public ResponseEntity<ApiResponseDTO<Void>> restablecerPassword(
            @Valid @RequestBody RestablecerRequestDTO request) {
        try {
            authUseCase.restablecerPassword(request.getToken(), request.getPassword());
            return ResponseEntity.ok(ApiResponseDTO.ok("Contraseña restablecida exitosamente", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponseDTO.error(e.getMessage()));
        }
    }
}
