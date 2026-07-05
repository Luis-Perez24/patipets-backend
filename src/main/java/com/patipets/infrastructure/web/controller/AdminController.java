package com.patipets.infrastructure.web.controller;

import com.patipets.core.application.useCase.ConsultarAdminDashboardUseCase;
import com.patipets.core.application.useCase.ConsultarCatalogoPublicoUseCase;
import com.patipets.core.application.useCase.GestionAnimalUseCase;
import com.patipets.core.application.useCase.GestionRefugioUseCase;
import com.patipets.core.application.useCase.GestionUsuarioUseCase;
import com.patipets.core.domain.models.Animal;
import com.patipets.core.domain.models.PaginatedResult;
import com.patipets.core.domain.models.Refugio;
import com.patipets.core.domain.models.SolicitudAdopcion;
import com.patipets.core.domain.models.Usuario;
import com.patipets.infrastructure.web.dto.AdminDashboardStatsResponseDTO;
import com.patipets.infrastructure.web.dto.AdminSolicitudAdopcionResponseDTO;
import com.patipets.infrastructure.web.dto.AdminUsuarioResponseDTO;
import com.patipets.infrastructure.web.dto.ApiResponseDTO;
import com.patipets.infrastructure.web.dto.PaginatedResponseDTO;
import com.patipets.infrastructure.web.dto.RefugioResponseDTO;
import com.patipets.infrastructure.web.dto.SolicitudAdopcionResponseDTO;
import com.patipets.infrastructure.web.dto.request.RefugioSolicitudRequestDTO;
import com.patipets.infrastructure.web.dto.request.UsuarioUpdateRequestDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final GestionRefugioUseCase gestionRefugioUseCase;
    private final ConsultarAdminDashboardUseCase adminDashboardUseCase;
    private final GestionUsuarioUseCase gestionUsuarioUseCase;
    private final GestionAnimalUseCase gestionAnimalUseCase;
    private final ConsultarCatalogoPublicoUseCase catalogoUseCase;

    public AdminController(GestionRefugioUseCase gestionRefugioUseCase,
                           ConsultarAdminDashboardUseCase adminDashboardUseCase,
                           GestionUsuarioUseCase gestionUsuarioUseCase,
                           GestionAnimalUseCase gestionAnimalUseCase,
                           ConsultarCatalogoPublicoUseCase catalogoUseCase) {
        this.gestionRefugioUseCase = gestionRefugioUseCase;
        this.adminDashboardUseCase = adminDashboardUseCase;
        this.gestionUsuarioUseCase = gestionUsuarioUseCase;
        this.gestionAnimalUseCase = gestionAnimalUseCase;
        this.catalogoUseCase = catalogoUseCase;
    }

    @PostMapping(value = "/refugios", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponseDTO<RefugioResponseDTO>> solicitarRefugio(
            Authentication authentication,
            @Valid @RequestPart("datos") RefugioSolicitudRequestDTO request,
            @RequestPart(value = "archivo", required = false) MultipartFile archivo) {
        try {
            Usuario usuarioActual = (Usuario) authentication.getPrincipal();
            Refugio refugio = gestionRefugioUseCase.solicitar(
                    request.getNombre(), request.getDireccion(), request.getRegion(),
                    request.getComuna(), request.getLatitud(), request.getLongitud(),
                    request.getCapacidad(), request.getEmail(), request.getNumeroContacto(),
                    usuarioActual.getId(), archivo);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponseDTO.ok("Solicitud de refugio creada", RefugioResponseDTO.fromDomain(refugio)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error(e.getMessage()));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Error al procesar la imagen: " + e.getMessage()));
        }
    }

    @GetMapping("/refugios/pendientes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<List<RefugioResponseDTO>>> listarPendientes() {
        List<Refugio> refugios = gestionRefugioUseCase.listarPendientes();
        var dto = refugios.stream()
                .map(RefugioResponseDTO::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponseDTO.ok(dto));
    }

    @PutMapping("/refugios/{id}/aprobar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<RefugioResponseDTO>> aprobarRefugio(@PathVariable Long id) {
        try {
            Refugio refugio = gestionRefugioUseCase.aprobar(id);
            return ResponseEntity.ok(ApiResponseDTO.ok("Refugio aprobado", RefugioResponseDTO.fromDomain(refugio)));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @PutMapping("/refugios/{id}/rechazar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<RefugioResponseDTO>> rechazarRefugio(@PathVariable Long id) {
        try {
            Refugio refugio = gestionRefugioUseCase.rechazar(id);
            return ResponseEntity.ok(ApiResponseDTO.ok("Refugio rechazado", RefugioResponseDTO.fromDomain(refugio)));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @GetMapping("/dashboard/estadisticas")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<AdminDashboardStatsResponseDTO>> dashboardEstadisticas() {
        var stats = adminDashboardUseCase.obtenerEstadisticasAdmin();
        return ResponseEntity.ok(ApiResponseDTO.ok(AdminDashboardStatsResponseDTO.fromDomain(stats)));
    }

    @GetMapping("/usuarios")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<PaginatedResponseDTO<AdminUsuarioResponseDTO>>> listarUsuarios(
            @RequestParam(required = false) String rol,
            @RequestParam(required = false) Boolean activo,
            @RequestParam(name = "fecha_desde", required = false) String fechaDesde,
            @RequestParam(name = "fecha_hasta", required = false) String fechaHasta,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        PaginatedResult<Usuario> result = gestionUsuarioUseCase.listar(rol, activo, fechaDesde, fechaHasta, page, size);
        List<AdminUsuarioResponseDTO> dto = result.getItems().stream()
                .map(AdminUsuarioResponseDTO::fromDomain)
                .collect(Collectors.toList());
        PaginatedResponseDTO<AdminUsuarioResponseDTO> paginated = new PaginatedResponseDTO<>(
                dto, result.getTotalPages(), result.getTotalElements(), result.getPage(), result.getSize());
        return ResponseEntity.ok(ApiResponseDTO.ok(paginated));
    }

    @GetMapping("/usuarios/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<AdminUsuarioResponseDTO>> obtenerUsuario(@PathVariable Long id) {
        try {
            Usuario usuario = gestionUsuarioUseCase.obtener(id);
            return ResponseEntity.ok(ApiResponseDTO.ok(AdminUsuarioResponseDTO.fromDomain(usuario)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @PutMapping("/usuarios/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<AdminUsuarioResponseDTO>> editarUsuario(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioUpdateRequestDTO request) {
        try {
            Usuario usuario = gestionUsuarioUseCase.editar(id, request.getNombre(),
                    request.getEmail(), request.getRol(),
                    request.getNumeroContacto(), request.getUbicacion(), request.getBiografia());
            return ResponseEntity.ok(ApiResponseDTO.ok("Usuario actualizado", AdminUsuarioResponseDTO.fromDomain(usuario)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @PutMapping("/usuarios/{id}/bloquear")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<AdminUsuarioResponseDTO>> bloquearUsuario(@PathVariable Long id) {
        try {
            Usuario usuario = gestionUsuarioUseCase.bloquear(id);
            return ResponseEntity.ok(ApiResponseDTO.ok("Usuario bloqueado", AdminUsuarioResponseDTO.fromDomain(usuario)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @PutMapping("/usuarios/{id}/desbloquear")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<AdminUsuarioResponseDTO>> desbloquearUsuario(@PathVariable Long id) {
        try {
            Usuario usuario = gestionUsuarioUseCase.desbloquear(id);
            return ResponseEntity.ok(ApiResponseDTO.ok("Usuario desbloqueado", AdminUsuarioResponseDTO.fromDomain(usuario)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @GetMapping("/solicitudes/pendientes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<List<AdminSolicitudAdopcionResponseDTO>>> listarSolicitudesPendientes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        List<SolicitudAdopcion> solicitudes = gestionAnimalUseCase.listarSolicitudesPendientes(page, size);
        var dto = solicitudes.stream()
                .map(this::enriquecer)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponseDTO.ok(dto));
    }

    @PutMapping("/solicitudes/{id}/aprobar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<AdminSolicitudAdopcionResponseDTO>> aprobarSolicitud(@PathVariable Long id) {
        try {
            SolicitudAdopcion solicitud = gestionAnimalUseCase.aprobarSolicitud(id);
            return ResponseEntity.ok(ApiResponseDTO.ok("Solicitud aprobada", enriquecer(solicitud)));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @PutMapping("/solicitudes/{id}/rechazar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<AdminSolicitudAdopcionResponseDTO>> rechazarSolicitud(@PathVariable Long id) {
        try {
            SolicitudAdopcion solicitud = gestionAnimalUseCase.rechazarSolicitud(id);
            return ResponseEntity.ok(ApiResponseDTO.ok("Solicitud rechazada", enriquecer(solicitud)));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    private AdminSolicitudAdopcionResponseDTO enriquecer(SolicitudAdopcion solicitud) {
        Animal animal = catalogoUseCase.obtenerPorId(solicitud.getAnimalId()).orElse(null);
        Refugio refugio = gestionRefugioUseCase.obtenerPorId(solicitud.getRefugioId()).orElse(null);
        return AdminSolicitudAdopcionResponseDTO.fromDomain(solicitud, animal, refugio);
    }
}
