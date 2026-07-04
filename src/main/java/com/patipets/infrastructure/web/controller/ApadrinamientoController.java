package com.patipets.infrastructure.web.controller;

import com.patipets.core.application.useCase.GestionApadrinamientoUseCase;
import com.patipets.core.domain.models.Apadrinamiento;
import com.patipets.core.domain.models.Usuario;
import com.patipets.infrastructure.web.dto.ApadrinamientoResponseDTO;
import com.patipets.infrastructure.web.dto.ApiResponseDTO;
import com.patipets.infrastructure.web.dto.request.ApadrinarRequestDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/apadrinamientos")
public class ApadrinamientoController {

    private final GestionApadrinamientoUseCase apadrinamientoUseCase;

    public ApadrinamientoController(GestionApadrinamientoUseCase apadrinamientoUseCase) {
        this.apadrinamientoUseCase = apadrinamientoUseCase;
    }

    @PostMapping
    @PreAuthorize("hasRole('PADRINO')")
    public ResponseEntity<ApiResponseDTO<ApadrinamientoResponseDTO>> apadrinar(
            Authentication authentication,
            @Valid @RequestBody ApadrinarRequestDTO request) {
        try {
            Usuario usuario = (Usuario) authentication.getPrincipal();
            Apadrinamiento apadrinamiento = apadrinamientoUseCase.apadrinar(
                    usuario.getId(), request.getAnimalId(), request.getTipoApoyo());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponseDTO.ok("Apadrinamiento registrado",
                            ApadrinamientoResponseDTO.fromDomain(apadrinamiento)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @GetMapping("/mis-apadrinamientos")
    @PreAuthorize("hasRole('PADRINO')")
    public ResponseEntity<ApiResponseDTO<List<ApadrinamientoResponseDTO>>> misApadrinamientos(
            Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        List<Apadrinamiento> lista = apadrinamientoUseCase.listarPorPadrino(usuario.getId());
        var dto = lista.stream()
                .map(ApadrinamientoResponseDTO::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponseDTO.ok(dto));
    }

    @GetMapping("/animal/{animalId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponseDTO<List<ApadrinamientoResponseDTO>>> listarPorAnimal(
            @PathVariable Long animalId) {
        List<Apadrinamiento> lista = apadrinamientoUseCase.listarPorAnimal(animalId);
        var dto = lista.stream()
                .map(ApadrinamientoResponseDTO::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponseDTO.ok(dto));
    }

    @GetMapping("/refugio/{refugioId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'REFUGIO')")
    public ResponseEntity<ApiResponseDTO<List<ApadrinamientoResponseDTO>>> listarPorRefugio(
            @PathVariable Long refugioId) {
        List<Apadrinamiento> lista = apadrinamientoUseCase.listarPorRefugio(refugioId);
        var dto = lista.stream()
                .map(ApadrinamientoResponseDTO::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponseDTO.ok(dto));
    }

    @PutMapping("/{id}/cancelar")
    @PreAuthorize("hasAnyRole('PADRINO', 'ADMIN')")
    public ResponseEntity<ApiResponseDTO<ApadrinamientoResponseDTO>> cancelar(
            Authentication authentication,
            @PathVariable Long id) {
        try {
            Usuario usuario = (Usuario) authentication.getPrincipal();
            Apadrinamiento apadrinamiento = apadrinamientoUseCase.cancelar(id, usuario.getId());
            return ResponseEntity.ok(ApiResponseDTO.ok("Apadrinamiento cancelado",
                    ApadrinamientoResponseDTO.fromDomain(apadrinamiento)));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error(e.getMessage()));
        }
    }
}
