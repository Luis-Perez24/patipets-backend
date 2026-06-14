package com.patipets.infrastructure.web.controller;

import com.patipets.core.application.useCase.GestionAnimalUseCase;
import com.patipets.core.domain.models.Animal;
import com.patipets.infrastructure.web.dto.AnimalResponseDTO;
import com.patipets.infrastructure.web.dto.ApiResponseDTO;
import com.patipets.infrastructure.web.dto.request.AnimalRequestDTO;
import com.patipets.infrastructure.web.dto.request.AnimalUpdateRequestDTO;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/refugios")
public class RefugioController {

    private final GestionAnimalUseCase gestionAnimalUseCase;

    public RefugioController(GestionAnimalUseCase gestionAnimalUseCase) {
        this.gestionAnimalUseCase = gestionAnimalUseCase;
    }

    @PostMapping("/{refugioId}/animales")
    @PreAuthorize("hasAnyRole('ADMIN', 'REFUGIO', 'VOLUNTARIO', 'PADRINO', 'CIUDADANO')")
    public ResponseEntity<ApiResponseDTO<AnimalResponseDTO>> crearAnimal(
            @PathVariable Long refugioId,
            @Valid @RequestBody AnimalRequestDTO request) {
        try {
            Animal animal = gestionAnimalUseCase.crearAnimal(
                    request.getNombre(), request.getEspecie(), request.getRaza(),
                    request.getEdad(), request.getTamano(), request.getEstadoSalud(),
                    request.getHistoria(), refugioId, request.getFotos());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponseDTO.ok("Animal creado", AnimalResponseDTO.fromDomain(animal)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @PutMapping("/{refugioId}/animales/{animalId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'REFUGIO', 'VOLUNTARIO', 'PADRINO', 'CIUDADANO')")
    public ResponseEntity<ApiResponseDTO<AnimalResponseDTO>> actualizarAnimal(
            @PathVariable Long refugioId,
            @PathVariable Long animalId,
            @Valid @RequestBody AnimalUpdateRequestDTO request) {
        try {
            Animal animal = gestionAnimalUseCase.actualizarAnimal(
                    animalId, request.getNombre(), request.getEspecie(), request.getRaza(),
                    request.getEdad(), request.getTamano(), request.getEstadoSalud(),
                    request.getHistoria(), request.getEstadoAdopcion(), request.getFotos());
            return ResponseEntity.ok(ApiResponseDTO.ok("Animal actualizado", AnimalResponseDTO.fromDomain(animal)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{refugioId}/animales/{animalId}")
    public ResponseEntity<ApiResponseDTO<Void>> eliminarAnimal(
            @PathVariable Long refugioId,
            @PathVariable Long animalId) {
        try {
            gestionAnimalUseCase.eliminarAnimal(animalId);
            return ResponseEntity.ok(ApiResponseDTO.ok("Animal eliminado", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error(e.getMessage()));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponseDTO.error("No se puede eliminar: el animal tiene postulaciones asociadas"));
        }
    }

    @GetMapping("/{refugioId}/animales")
    public ResponseEntity<ApiResponseDTO<List<AnimalResponseDTO>>> listarAnimales(
            @PathVariable Long refugioId) {
        List<Animal> animales = gestionAnimalUseCase.listarPorRefugio(refugioId);
        var dto = animales.stream()
                .map(AnimalResponseDTO::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponseDTO.ok(dto));
    }
}
