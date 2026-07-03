package com.patipets.infrastructure.web.controller;

import com.patipets.core.application.useCase.GestionAnimalUseCase;
import com.patipets.core.domain.models.SolicitudAdopcion;
import com.patipets.core.domain.models.Usuario;
import com.patipets.infrastructure.security.RefugioAccessGuard;
import com.patipets.infrastructure.web.dto.ApiResponseDTO;
import com.patipets.infrastructure.web.dto.SolicitudAdopcionResponseDTO;
import com.patipets.infrastructure.web.dto.request.SolicitudAdopcionRequestDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/adopcion")
public class AdopcionController {

    private final GestionAnimalUseCase gestionAnimalUseCase;
    private final RefugioAccessGuard refugioAccessGuard;

    public AdopcionController(GestionAnimalUseCase gestionAnimalUseCase, RefugioAccessGuard refugioAccessGuard) {
        this.gestionAnimalUseCase = gestionAnimalUseCase;
        this.refugioAccessGuard = refugioAccessGuard;
    }

    @PostMapping("/solicitar")
    public ResponseEntity<ApiResponseDTO<SolicitudAdopcionResponseDTO>> solicitar(
            Authentication authentication,
            @Valid @RequestBody SolicitudAdopcionRequestDTO request) {
        try {
            Usuario usuario = (Usuario) authentication.getPrincipal();
            SolicitudAdopcion solicitud = gestionAnimalUseCase.solicitar(
                    request.getAnimalId(), usuario.getId(),
                    request.getNombreCompleto(), request.getNumeroContacto(),
                    request.getDireccion(), request.getNivelActividad(),
                    request.getHorasSolo(), request.getCuidadoVacaciones(),
                    request.getTipoVivienda(), request.getDescripcionEspacio(),
                    request.getTieneNinos(), request.getTieneOtrasMascotas());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponseDTO.ok("Solicitud de adopción enviada",
                            SolicitudAdopcionResponseDTO.fromDomain(solicitud)));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @GetMapping("/mis-solicitudes")
    public ResponseEntity<ApiResponseDTO<List<SolicitudAdopcionResponseDTO>>> misSolicitudes(
            Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        List<SolicitudAdopcion> solicitudes = gestionAnimalUseCase.listarSolicitudesPorAdoptante(usuario.getId());
        var dto = solicitudes.stream()
                .map(SolicitudAdopcionResponseDTO::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponseDTO.ok(dto));
    }

    @GetMapping("/refugio/{refugioId}/solicitudes")
    @PreAuthorize("hasRole('ADMIN') or hasRole('REFUGIO')")
    public ResponseEntity<ApiResponseDTO<List<SolicitudAdopcionResponseDTO>>> solicitudesPorRefugio(
            Authentication authentication,
            @PathVariable Long refugioId) {
        refugioAccessGuard.verificar((Usuario) authentication.getPrincipal(), refugioId);
        List<SolicitudAdopcion> solicitudes = gestionAnimalUseCase.listarSolicitudesPorRefugio(refugioId);
        var dto = solicitudes.stream()
                .map(SolicitudAdopcionResponseDTO::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponseDTO.ok(dto));
    }

    @PutMapping("/solicitudes/{id}/aprobar")
    @PreAuthorize("hasRole('ADMIN') or hasRole('REFUGIO')")
    public ResponseEntity<ApiResponseDTO<SolicitudAdopcionResponseDTO>> aprobarSolicitud(
            Authentication authentication,
            @PathVariable Long id) {
        try {
            SolicitudAdopcion existente = gestionAnimalUseCase.obtenerSolicitudPorId(id);
            refugioAccessGuard.verificar((Usuario) authentication.getPrincipal(), existente.getRefugioId());
            SolicitudAdopcion solicitud = gestionAnimalUseCase.aprobarSolicitud(id);
            return ResponseEntity.ok(ApiResponseDTO.ok("Solicitud aprobada",
                    SolicitudAdopcionResponseDTO.fromDomain(solicitud)));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @PutMapping("/solicitudes/{id}/rechazar")
    @PreAuthorize("hasRole('ADMIN') or hasRole('REFUGIO')")
    public ResponseEntity<ApiResponseDTO<SolicitudAdopcionResponseDTO>> rechazarSolicitud(
            Authentication authentication,
            @PathVariable Long id) {
        try {
            SolicitudAdopcion existente = gestionAnimalUseCase.obtenerSolicitudPorId(id);
            refugioAccessGuard.verificar((Usuario) authentication.getPrincipal(), existente.getRefugioId());
            SolicitudAdopcion solicitud = gestionAnimalUseCase.rechazarSolicitud(id);
            return ResponseEntity.ok(ApiResponseDTO.ok("Solicitud rechazada",
                    SolicitudAdopcionResponseDTO.fromDomain(solicitud)));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error(e.getMessage()));
        }
    }
}
