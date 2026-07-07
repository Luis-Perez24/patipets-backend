package com.patipets.infrastructure.web.controller;

import com.patipets.core.application.useCase.ConsultarCatalogoPublicoUseCase;
import com.patipets.core.application.useCase.GestionAnimalUseCase;
import com.patipets.core.application.useCase.GestionRefugioUseCase;
import com.patipets.core.domain.models.Animal;
import com.patipets.core.domain.models.Refugio;
import com.patipets.core.domain.models.SolicitudAdopcion;
import com.patipets.core.domain.models.Usuario;
import com.patipets.infrastructure.security.RefugioAccessGuard;
import com.patipets.infrastructure.web.dto.ApiResponseDTO;
import com.patipets.infrastructure.web.dto.MiPostulacionResponseDTO;
import com.patipets.infrastructure.web.dto.SolicitudAdopcionResponseDTO;
import com.patipets.infrastructure.web.dto.request.SolicitudAdopcionRequestDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/adopcion")
public class AdopcionController {

    private final GestionAnimalUseCase gestionAnimalUseCase;
    private final GestionRefugioUseCase gestionRefugioUseCase;
    private final ConsultarCatalogoPublicoUseCase catalogoUseCase;
    private final com.patipets.core.application.useCase.GestionUsuarioUseCase gestionUsuarioUseCase;
    private final RefugioAccessGuard refugioAccessGuard;

    public AdopcionController(GestionAnimalUseCase gestionAnimalUseCase,
                               GestionRefugioUseCase gestionRefugioUseCase,
                               ConsultarCatalogoPublicoUseCase catalogoUseCase,
                               com.patipets.core.application.useCase.GestionUsuarioUseCase gestionUsuarioUseCase,
                               RefugioAccessGuard refugioAccessGuard) {
        this.gestionAnimalUseCase = gestionAnimalUseCase;
        this.gestionRefugioUseCase = gestionRefugioUseCase;
        this.catalogoUseCase = catalogoUseCase;
        this.gestionUsuarioUseCase = gestionUsuarioUseCase;
        this.refugioAccessGuard = refugioAccessGuard;
    }

    private MiPostulacionResponseDTO enriquecer(SolicitudAdopcion solicitud) {
        Animal animal = catalogoUseCase.obtenerPorId(solicitud.getAnimalId()).orElse(null);
        Refugio refugio = gestionRefugioUseCase.obtenerPorId(solicitud.getRefugioId()).orElse(null);
        Usuario adoptante = gestionUsuarioUseCase.obtener(solicitud.getAdoptanteId());
        return MiPostulacionResponseDTO.fromDomain(solicitud, animal, refugio, adoptante);
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
                    request.getTieneNinos(), request.getTieneOtrasMascotas(),
                    request.getDetalleMascotas());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponseDTO.ok("Solicitud de adopción enviada",
                            SolicitudAdopcionResponseDTO.fromDomain(solicitud)));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @GetMapping("/mis-solicitudes")
    public ResponseEntity<ApiResponseDTO<List<MiPostulacionResponseDTO>>> misSolicitudes(
            Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        List<SolicitudAdopcion> solicitudes = gestionAnimalUseCase.listarSolicitudesPorAdoptante(usuario.getId());
        var dto = solicitudes.stream()
                .map(this::enriquecer)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponseDTO.ok(dto));
    }

    @GetMapping("/solicitudes/{id}")
    public ResponseEntity<ApiResponseDTO<MiPostulacionResponseDTO>> miSolicitudDetalle(
            Authentication authentication,
            @PathVariable Long id) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        SolicitudAdopcion solicitud = gestionAnimalUseCase.obtenerSolicitudPorId(id);
        if (!solicitud.getAdoptanteId().equals(usuario.getId())) {
            throw new AccessDeniedException("No tienes acceso a esta postulación");
        }
        return ResponseEntity.ok(ApiResponseDTO.ok(enriquecer(solicitud)));
    }

    @GetMapping("/refugio/{refugioId}/solicitudes")
    @PreAuthorize("hasRole('ADMIN') or hasRole('REFUGIO')")
    public ResponseEntity<ApiResponseDTO<List<MiPostulacionResponseDTO>>> solicitudesPorRefugio(
            Authentication authentication,
            @PathVariable Long refugioId) {
        refugioAccessGuard.verificar((Usuario) authentication.getPrincipal(), refugioId);
        List<SolicitudAdopcion> solicitudes = gestionAnimalUseCase.listarSolicitudesPorRefugio(refugioId);
        var dto = solicitudes.stream()
                .map(this::enriquecer)
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

    @PutMapping("/solicitudes/{id}/confirmar")
    @PreAuthorize("hasRole('ADMIN') or hasRole('REFUGIO')")
    public ResponseEntity<ApiResponseDTO<SolicitudAdopcionResponseDTO>> confirmarAdopcion(
            Authentication authentication,
            @PathVariable Long id) {
        try {
            SolicitudAdopcion existente = gestionAnimalUseCase.obtenerSolicitudPorId(id);
            refugioAccessGuard.verificar((Usuario) authentication.getPrincipal(), existente.getRefugioId());
            SolicitudAdopcion solicitud = gestionAnimalUseCase.confirmarAdopcion(id);
            return ResponseEntity.ok(ApiResponseDTO.ok("Adopción confirmada",
                    SolicitudAdopcionResponseDTO.fromDomain(solicitud)));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error(e.getMessage()));
        }
    }
}
