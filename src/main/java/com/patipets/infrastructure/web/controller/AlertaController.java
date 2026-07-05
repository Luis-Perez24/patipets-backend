package com.patipets.infrastructure.web.controller;

import com.patipets.core.application.ports.output.AlertaRepositoryPort;
import com.patipets.core.application.ports.output.RefugioRepositoryPort;
import com.patipets.core.application.ports.output.UsuarioRepositoryPort;
import com.patipets.core.application.useCase.GestionAlertaUseCase;
import com.patipets.core.domain.models.Alerta;
import com.patipets.core.domain.models.RespuestaAlerta;
import com.patipets.core.domain.models.Usuario;
import com.patipets.infrastructure.security.RefugioAccessGuard;
import com.patipets.infrastructure.web.dto.AlertaResponseDTO;
import com.patipets.infrastructure.web.dto.ApiResponseDTO;
import com.patipets.infrastructure.web.dto.RespuestaAlertaResponseDTO;
import com.patipets.infrastructure.web.dto.request.AlertaRequestDTO;
import com.patipets.infrastructure.web.dto.request.ResponderAlertaRequestDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/alertas")
public class AlertaController {

    private final GestionAlertaUseCase gestionAlertaUseCase;
    private final RefugioAccessGuard refugioAccessGuard;
    private final RefugioRepositoryPort refugioRepository;
    private final AlertaRepositoryPort alertaRepository;
    private final UsuarioRepositoryPort usuarioRepository;

    public AlertaController(GestionAlertaUseCase gestionAlertaUseCase, RefugioAccessGuard refugioAccessGuard,
                            RefugioRepositoryPort refugioRepository,
                            AlertaRepositoryPort alertaRepository,
                            UsuarioRepositoryPort usuarioRepository) {
        this.gestionAlertaUseCase = gestionAlertaUseCase;
        this.refugioAccessGuard = refugioAccessGuard;
        this.refugioRepository = refugioRepository;
        this.alertaRepository = alertaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'REFUGIO')")
    public ResponseEntity<ApiResponseDTO<AlertaResponseDTO>> crearAlerta(
            Authentication authentication,
            @Valid @RequestBody AlertaRequestDTO request,
            @RequestAttribute("usuarioId") Long usuarioId) {
        try {
            refugioAccessGuard.verificar((Usuario) authentication.getPrincipal(), request.getRefugioId());
            Alerta alerta = gestionAlertaUseCase.crear(
                    request.getTitulo(), request.getDescripcion(),
                    request.getNivelUrgencia(), request.getRefugioId(), usuarioId,
                    request.getTipoAyuda(), request.getFecha(), request.getPerfilRequerido());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponseDTO.ok("Alerta creada", enriquecerAlerta(AlertaResponseDTO.fromDomain(alerta))));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @GetMapping("/refugio/{refugioId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'REFUGIO', 'VOLUNTARIO', 'PADRINO')")
    public ResponseEntity<ApiResponseDTO<List<AlertaResponseDTO>>> listarPorRefugio(
            @PathVariable Long refugioId) {
        List<Alerta> alertas = gestionAlertaUseCase.listarPorRefugio(refugioId);
        var dto = alertas.stream()
                .map(a -> enriquecerAlerta(AlertaResponseDTO.fromDomain(a)))
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponseDTO.ok(dto));
    }

    @GetMapping("/activas")
    @PreAuthorize("hasAnyRole('ADMIN', 'REFUGIO', 'VOLUNTARIO', 'PADRINO')")
    public ResponseEntity<ApiResponseDTO<List<AlertaResponseDTO>>> listarActivas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        List<Alerta> alertas = gestionAlertaUseCase.listarActivas(page, size);
        var dto = alertas.stream()
                .map(a -> enriquecerAlerta(AlertaResponseDTO.fromDomain(a)))
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponseDTO.ok(dto));
    }

    @PutMapping("/{id}/resolver")
    @PreAuthorize("hasAnyRole('ADMIN', 'REFUGIO')")
    public ResponseEntity<ApiResponseDTO<AlertaResponseDTO>> marcarResuelta(@PathVariable Long id) {
        try {
            Alerta alerta = gestionAlertaUseCase.marcarResuelta(id);
            return ResponseEntity.ok(ApiResponseDTO.ok("Alerta resuelta", enriquecerAlerta(AlertaResponseDTO.fromDomain(alerta))));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<Void>> eliminarAlerta(@PathVariable Long id) {
        try {
            gestionAlertaUseCase.eliminar(id);
            return ResponseEntity.ok(ApiResponseDTO.ok("Alerta eliminada", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @PostMapping("/{id}/responder")
    @PreAuthorize("hasAnyRole('VOLUNTARIO', 'PADRINO')")
    public ResponseEntity<ApiResponseDTO<RespuestaAlertaResponseDTO>> responderAlerta(
            Authentication authentication,
            @PathVariable Long id,
            @Valid @RequestBody ResponderAlertaRequestDTO request) {
        try {
            Usuario usuario = (Usuario) authentication.getPrincipal();
            RespuestaAlerta respuesta = gestionAlertaUseCase.responder(
                    id, usuario.getId(), request.getTipoAyuda(), request.getMensaje(), request.getDisponibilidad());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponseDTO.ok("Respuesta registrada",
                            enriquecerRespuesta(RespuestaAlertaResponseDTO.fromDomain(respuesta))));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @GetMapping("/mis-respuestas")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponseDTO<List<RespuestaAlertaResponseDTO>>> misRespuestas(
            Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        List<RespuestaAlerta> respuestas = gestionAlertaUseCase.listarRespuestasPorUsuario(usuario.getId());
        var dto = respuestas.stream()
                .map(r -> enriquecerRespuesta(RespuestaAlertaResponseDTO.fromDomain(r)))
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponseDTO.ok(dto));
    }

    @GetMapping("/{id}/respuestas")
    @PreAuthorize("hasAnyRole('ADMIN', 'REFUGIO', 'VOLUNTARIO', 'PADRINO')")
    public ResponseEntity<ApiResponseDTO<List<RespuestaAlertaResponseDTO>>> listarRespuestas(
            @PathVariable Long id) {
        List<RespuestaAlerta> respuestas = gestionAlertaUseCase.listarRespuestasPorAlerta(id);
        var dto = respuestas.stream()
                .map(r -> enriquecerRespuesta(RespuestaAlertaResponseDTO.fromDomain(r)))
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponseDTO.ok(dto));
    }

    private AlertaResponseDTO enriquecerAlerta(AlertaResponseDTO dto) {
        refugioRepository.findById(dto.getRefugioId())
                .ifPresent(r -> dto.setRefugioNombre(r.getNombre()));
        return dto;
    }

    private RespuestaAlertaResponseDTO enriquecerRespuesta(RespuestaAlertaResponseDTO dto) {
        alertaRepository.findById(dto.getAlertaId())
                .ifPresent(a -> dto.setAlertaTitulo(a.getTitulo()));
        usuarioRepository.findById(dto.getUsuarioId())
                .ifPresent(u -> dto.setUsuarioNombre(u.getNombre()));
        return dto;
    }
}
