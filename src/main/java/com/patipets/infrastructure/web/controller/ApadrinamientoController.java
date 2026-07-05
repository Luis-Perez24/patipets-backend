package com.patipets.infrastructure.web.controller;

import com.patipets.core.application.ports.output.AnimalRepositoryPort;
import com.patipets.core.application.ports.output.RefugioRepositoryPort;
import com.patipets.core.application.useCase.GestionApadrinamientoUseCase;
import com.patipets.core.domain.models.Apadrinamiento;
import com.patipets.core.domain.models.Animal;
import com.patipets.core.domain.models.Refugio;
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
    private final AnimalRepositoryPort animalRepository;
    private final RefugioRepositoryPort refugioRepository;

    public ApadrinamientoController(GestionApadrinamientoUseCase apadrinamientoUseCase,
                                    AnimalRepositoryPort animalRepository,
                                    RefugioRepositoryPort refugioRepository) {
        this.apadrinamientoUseCase = apadrinamientoUseCase;
        this.animalRepository = animalRepository;
        this.refugioRepository = refugioRepository;
    }

    @PostMapping
    @PreAuthorize("hasRole('PADRINO')")
    public ResponseEntity<ApiResponseDTO<ApadrinamientoResponseDTO>> apadrinar(
            Authentication authentication,
            @Valid @RequestBody ApadrinarRequestDTO request) {
        try {
            Usuario usuario = (Usuario) authentication.getPrincipal();
            Apadrinamiento apadrinamiento = apadrinamientoUseCase.apadrinar(
                    usuario.getId(), request.getAnimalId(), request.getTipoApoyo(), request.getCompromiso());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponseDTO.ok("Apadrinamiento registrado",
                            enriquecer(ApadrinamientoResponseDTO.fromDomain(apadrinamiento))));
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
                .map(a -> enriquecer(ApadrinamientoResponseDTO.fromDomain(a)))
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponseDTO.ok(dto));
    }

    @GetMapping("/animal/{animalId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponseDTO<List<ApadrinamientoResponseDTO>>> listarPorAnimal(
            @PathVariable Long animalId) {
        List<Apadrinamiento> lista = apadrinamientoUseCase.listarPorAnimal(animalId);
        var dto = lista.stream()
                .map(a -> enriquecer(ApadrinamientoResponseDTO.fromDomain(a)))
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponseDTO.ok(dto));
    }

    @GetMapping("/refugio/{refugioId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'REFUGIO')")
    public ResponseEntity<ApiResponseDTO<List<ApadrinamientoResponseDTO>>> listarPorRefugio(
            @PathVariable Long refugioId) {
        List<Apadrinamiento> lista = apadrinamientoUseCase.listarPorRefugio(refugioId);
        var dto = lista.stream()
                .map(a -> enriquecer(ApadrinamientoResponseDTO.fromDomain(a)))
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
                    enriquecer(ApadrinamientoResponseDTO.fromDomain(apadrinamiento))));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    private ApadrinamientoResponseDTO enriquecer(ApadrinamientoResponseDTO dto) {
        animalRepository.findById(dto.getAnimalId())
                .ifPresent(a -> dto.setAnimalNombre(a.getNombre()));
        refugioRepository.findById(dto.getRefugioId())
                .ifPresent(r -> dto.setRefugioNombre(r.getNombre()));
        return dto;
    }
}
