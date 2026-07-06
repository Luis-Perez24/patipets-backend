package com.patipets.infrastructure.web.controller;

import com.patipets.core.application.useCase.GestionNotificacionUseCase;
import com.patipets.core.domain.models.Notificacion;
import com.patipets.core.domain.models.Usuario;
import com.patipets.infrastructure.web.dto.ApiResponseDTO;
import com.patipets.infrastructure.web.dto.NotificacionResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/notificaciones")
public class NotificacionController {

    private final GestionNotificacionUseCase gestionNotificacionUseCase;

    public NotificacionController(GestionNotificacionUseCase gestionNotificacionUseCase) {
        this.gestionNotificacionUseCase = gestionNotificacionUseCase;
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<NotificacionResponseDTO>>> misNotificaciones(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        List<Notificacion> notificaciones = gestionNotificacionUseCase.listarPorUsuario(usuario.getId(), page, size);
        var dto = notificaciones.stream()
                .map(NotificacionResponseDTO::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponseDTO.ok(dto));
    }

    @GetMapping("/no-leidas/count")
    public ResponseEntity<ApiResponseDTO<Long>> contarNoLeidas(Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        long count = gestionNotificacionUseCase.contarNoLeidas(usuario.getId());
        return ResponseEntity.ok(ApiResponseDTO.ok(count));
    }

    @PutMapping("/{id}/leer")
    public ResponseEntity<ApiResponseDTO<NotificacionResponseDTO>> marcarLeida(
            Authentication authentication,
            @PathVariable Long id) {
        try {
            Usuario usuario = (Usuario) authentication.getPrincipal();
            Notificacion notificacion = gestionNotificacionUseCase.marcarLeida(id, usuario.getId());
            return ResponseEntity.ok(ApiResponseDTO.ok("Notificación marcada como leída",
                    NotificacionResponseDTO.fromDomain(notificacion)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @PutMapping("/leer-todas")
    public ResponseEntity<ApiResponseDTO<Integer>> marcarTodasLeidas(Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        int count = gestionNotificacionUseCase.marcarTodasLeidas(usuario.getId());
        return ResponseEntity.ok(ApiResponseDTO.ok("Notificaciones marcadas como leídas", count));
    }
}
