package com.patipets.infrastructure.web.controller;

import com.patipets.core.application.useCase.GestionAlertaUseCase;
import com.patipets.core.domain.models.Alerta;
import com.patipets.core.domain.models.Usuario;
import com.patipets.infrastructure.security.RefugioAccessGuard;
import com.patipets.infrastructure.web.dto.AlertaResponseDTO;
import com.patipets.infrastructure.web.dto.ApiResponseDTO;
import com.patipets.infrastructure.web.dto.request.AlertaRequestDTO;
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

    public AlertaController(GestionAlertaUseCase gestionAlertaUseCase, RefugioAccessGuard refugioAccessGuard) {
        this.gestionAlertaUseCase = gestionAlertaUseCase;
        this.refugioAccessGuard = refugioAccessGuard;
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
                    request.getNivelUrgencia(), request.getRefugioId(), usuarioId);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponseDTO.ok("Alerta creada", AlertaResponseDTO.fromDomain(alerta)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @GetMapping("/refugio/{refugioId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'REFUGIO', 'VOLUNTARIO')")
    public ResponseEntity<ApiResponseDTO<List<AlertaResponseDTO>>> listarPorRefugio(
            @PathVariable Long refugioId) {
        List<Alerta> alertas = gestionAlertaUseCase.listarPorRefugio(refugioId);
        var dto = alertas.stream()
                .map(AlertaResponseDTO::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponseDTO.ok(dto));
    }

    @GetMapping("/activas")
    @PreAuthorize("hasAnyRole('ADMIN', 'REFUGIO', 'VOLUNTARIO')")
    public ResponseEntity<ApiResponseDTO<List<AlertaResponseDTO>>> listarActivas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        List<Alerta> alertas = gestionAlertaUseCase.listarActivas(page, size);
        var dto = alertas.stream()
                .map(AlertaResponseDTO::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponseDTO.ok(dto));
    }

    @PutMapping("/{id}/resolver")
    @PreAuthorize("hasAnyRole('ADMIN', 'REFUGIO')")
    public ResponseEntity<ApiResponseDTO<AlertaResponseDTO>> marcarResuelta(@PathVariable Long id) {
        try {
            Alerta alerta = gestionAlertaUseCase.marcarResuelta(id);
            return ResponseEntity.ok(ApiResponseDTO.ok("Alerta resuelta", AlertaResponseDTO.fromDomain(alerta)));
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
}
