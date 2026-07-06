package com.patipets.infrastructure.web.controller;

import com.patipets.core.application.ports.output.AlertaRepositoryPort;
import com.patipets.core.application.ports.output.RefugioRepositoryPort;
import com.patipets.core.application.ports.output.RespuestaAlertaRepositoryPort;
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
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/alertas")
public class AlertaController {

    private final GestionAlertaUseCase gestionAlertaUseCase;
    private final RefugioAccessGuard refugioAccessGuard;
    private final RefugioRepositoryPort refugioRepository;
    private final AlertaRepositoryPort alertaRepository;
    private final RespuestaAlertaRepositoryPort respuestaRepository;
    private final UsuarioRepositoryPort usuarioRepository;

    public AlertaController(GestionAlertaUseCase gestionAlertaUseCase, RefugioAccessGuard refugioAccessGuard,
                            RefugioRepositoryPort refugioRepository,
                            AlertaRepositoryPort alertaRepository,
                            RespuestaAlertaRepositoryPort respuestaRepository,
                            UsuarioRepositoryPort usuarioRepository) {
        this.gestionAlertaUseCase = gestionAlertaUseCase;
        this.refugioAccessGuard = refugioAccessGuard;
        this.refugioRepository = refugioRepository;
        this.alertaRepository = alertaRepository;
        this.respuestaRepository = respuestaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'REFUGIO')")
    public ResponseEntity<ApiResponseDTO<AlertaResponseDTO>> crearAlerta(
            Authentication authentication,
            @Valid @RequestBody AlertaRequestDTO request) {
        try {
            Usuario usuario = (Usuario) authentication.getPrincipal();
            refugioAccessGuard.verificar(usuario, request.getRefugioId());
            Alerta alerta = gestionAlertaUseCase.crear(
                    request.getTitulo(), request.getDescripcion(),
                    request.getNivelUrgencia(), request.getRefugioId(), usuario.getId(),
                    request.getTipoAyuda(), request.getFecha(), request.getPerfilRequerido());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponseDTO.ok("Alerta creada", enriquecerAlerta(AlertaResponseDTO.fromDomain(alerta))));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @GetMapping("/refugio/{refugioId}")
    public ResponseEntity<ApiResponseDTO<List<AlertaResponseDTO>>> listarPorRefugio(
            @PathVariable Long refugioId,
            @RequestParam(name = "tipo", defaultValue = "todas") String tipo) {
        List<Alerta> alertas = gestionAlertaUseCase.listarPorRefugio(refugioId, tipo);
        var dto = alertas.stream()
                .map(a -> enriquecerAlerta(AlertaResponseDTO.fromDomain(a)))
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponseDTO.ok(dto));
    }

    @GetMapping("/activas")
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
    public ResponseEntity<ApiResponseDTO<AlertaResponseDTO>> marcarResuelta(
            Authentication authentication,
            @PathVariable Long id) {
        try {
            Alerta alerta = alertaRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Alerta no encontrada: " + id));
            refugioAccessGuard.verificar((Usuario) authentication.getPrincipal(), alerta.getRefugioId());
            Alerta resuelta = gestionAlertaUseCase.marcarResuelta(id);
            return ResponseEntity.ok(ApiResponseDTO.ok("Alerta resuelta", enriquecerAlerta(AlertaResponseDTO.fromDomain(resuelta))));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'REFUGIO')")
    public ResponseEntity<ApiResponseDTO<Void>> eliminarAlerta(
            Authentication authentication,
            @PathVariable Long id) {
        try {
            Alerta alerta = alertaRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Alerta no encontrada: " + id));
            refugioAccessGuard.verificar((Usuario) authentication.getPrincipal(), alerta.getRefugioId());
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

    @GetMapping("/refugio/{refugioId}/todas-respuestas")
    @PreAuthorize("hasAnyRole('ADMIN', 'REFUGIO')")
    public ResponseEntity<ApiResponseDTO<List<RespuestaAlertaResponseDTO>>> listarRespuestasPorRefugio(
            Authentication authentication,
            @PathVariable Long refugioId) {
        try {
            refugioAccessGuard.verificar((Usuario) authentication.getPrincipal(), refugioId);
            List<RespuestaAlerta> respuestas = gestionAlertaUseCase.listarRespuestasPorRefugio(refugioId);
            var dto = respuestas.stream()
                    .map(r -> enriquecerRespuesta(RespuestaAlertaResponseDTO.fromDomain(r)))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponseDTO.ok(dto));
        } catch (org.springframework.security.access.AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @PutMapping("/respuestas/{id}/cancelar")
    @PreAuthorize("hasAnyRole('ADMIN', 'REFUGIO')")
    public ResponseEntity<ApiResponseDTO<RespuestaAlertaResponseDTO>> cancelarRespuesta(
            Authentication authentication,
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, String> body) {
        try {
            RespuestaAlerta respuesta = respuestaRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Inscripción no encontrada: " + id));
            Alerta alerta = alertaRepository.findById(respuesta.getAlertaId())
                    .orElseThrow(() -> new IllegalArgumentException("Alerta no encontrada: " + respuesta.getAlertaId()));
            refugioAccessGuard.verificar((Usuario) authentication.getPrincipal(), alerta.getRefugioId());
            String motivo = body != null ? body.get("motivo") : null;
            RespuestaAlerta cancelada = gestionAlertaUseCase.cancelarRespuestaPorRefugio(
                    id, alerta.getRefugioId(), motivo);
            return ResponseEntity.ok(ApiResponseDTO.ok("Inscripción cancelada",
                    enriquecerRespuesta(RespuestaAlertaResponseDTO.fromDomain(cancelada))));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(ApiResponseDTO.error(e.getMessage()));
        } catch (org.springframework.security.access.AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @PutMapping("/respuestas/{id}/aceptar")
    @PreAuthorize("hasAnyRole('ADMIN', 'REFUGIO')")
    public ResponseEntity<ApiResponseDTO<RespuestaAlertaResponseDTO>> aceptarRespuesta(
            Authentication authentication,
            @PathVariable Long id) {
        try {
            RespuestaAlerta respuesta = respuestaRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Inscripción no encontrada: " + id));
            Alerta alerta = alertaRepository.findById(respuesta.getAlertaId())
                    .orElseThrow(() -> new IllegalArgumentException("Alerta no encontrada: " + respuesta.getAlertaId()));
            refugioAccessGuard.verificar((Usuario) authentication.getPrincipal(), alerta.getRefugioId());
            RespuestaAlerta aceptada = gestionAlertaUseCase.aceptarRespuesta(id, alerta.getRefugioId());
            return ResponseEntity.ok(ApiResponseDTO.ok("Inscripción aceptada",
                    enriquecerRespuesta(RespuestaAlertaResponseDTO.fromDomain(aceptada))));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(ApiResponseDTO.error(e.getMessage()));
        } catch (org.springframework.security.access.AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @PutMapping("/respuestas/{id}/rechazar")
    @PreAuthorize("hasAnyRole('ADMIN', 'REFUGIO')")
    public ResponseEntity<ApiResponseDTO<RespuestaAlertaResponseDTO>> rechazarRespuesta(
            Authentication authentication,
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, String> body) {
        try {
            RespuestaAlerta respuesta = respuestaRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Inscripción no encontrada: " + id));
            Alerta alerta = alertaRepository.findById(respuesta.getAlertaId())
                    .orElseThrow(() -> new IllegalArgumentException("Alerta no encontrada: " + respuesta.getAlertaId()));
            refugioAccessGuard.verificar((Usuario) authentication.getPrincipal(), alerta.getRefugioId());
            String motivo = body != null ? body.get("motivo") : null;
            RespuestaAlerta rechazada = gestionAlertaUseCase.rechazarRespuesta(id, alerta.getRefugioId(), motivo);
            return ResponseEntity.ok(ApiResponseDTO.ok("Inscripción rechazada",
                    enriquecerRespuesta(RespuestaAlertaResponseDTO.fromDomain(rechazada))));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(ApiResponseDTO.error(e.getMessage()));
        } catch (org.springframework.security.access.AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @PutMapping("/respuestas/{id}/cancelar-voluntario")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponseDTO<RespuestaAlertaResponseDTO>> cancelarPorVoluntario(
            Authentication authentication,
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, String> body) {
        try {
            Usuario usuario = (Usuario) authentication.getPrincipal();
            String motivo = body != null ? body.get("motivo") : null;
            RespuestaAlerta cancelada = gestionAlertaUseCase.cancelarRespuestaPorVoluntario(id, usuario.getId(), motivo);
            return ResponseEntity.ok(ApiResponseDTO.ok("Inscripción cancelada",
                    enriquecerRespuesta(RespuestaAlertaResponseDTO.fromDomain(cancelada))));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    private AlertaResponseDTO enriquecerAlerta(AlertaResponseDTO dto) {
        refugioRepository.findById(dto.getRefugioId())
                .ifPresent(r -> {
                    dto.setRefugioNombre(r.getNombre());
                    String dir = r.getDireccion();
                    String region = r.getRegion();
                    String loc = dir != null ? dir : "";
                    if (region != null) {
                        loc = loc.isEmpty() ? region : loc + ", " + region;
                    }
                    dto.setRefugioUbicacion(loc);
                });
        return dto;
    }

    private RespuestaAlertaResponseDTO enriquecerRespuesta(RespuestaAlertaResponseDTO dto) {
        alertaRepository.findById(dto.getAlertaId())
                .ifPresent(a -> {
                    dto.setAlertaTitulo(a.getTitulo());
                    dto.setRefugioId(a.getRefugioId());
                    refugioRepository.findById(a.getRefugioId())
                            .ifPresent(r -> dto.setRefugioNombre(r.getNombre()));
                });
        usuarioRepository.findById(dto.getUsuarioId())
                .ifPresent(u -> dto.setUsuarioNombre(u.getNombre()));
        return dto;
    }
}
