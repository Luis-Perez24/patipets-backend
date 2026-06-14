package com.patipets.infrastructure.web.controller;

import com.patipets.core.application.useCase.ConsultarCatalogoPublicoUseCase;
import com.patipets.core.application.useCase.ConsultarEstadisticasDashboardUseCase;
import com.patipets.core.application.useCase.ConsultarMapaRefugiosUseCase;
import com.patipets.infrastructure.web.dto.AnimalResponseDTO;
import com.patipets.infrastructure.web.dto.ApiResponseDTO;
import com.patipets.infrastructure.web.dto.EstadisticaDashboardResponseDTO;
import com.patipets.infrastructure.web.dto.RefugioUbicacionDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/public")
public class PublicController {

    private final ConsultarCatalogoPublicoUseCase catalogoUseCase;
    private final ConsultarMapaRefugiosUseCase mapaUseCase;
    private final ConsultarEstadisticasDashboardUseCase dashboardUseCase;

    public PublicController(ConsultarCatalogoPublicoUseCase catalogoUseCase,
                             ConsultarMapaRefugiosUseCase mapaUseCase,
                             ConsultarEstadisticasDashboardUseCase dashboardUseCase) {
        this.catalogoUseCase = catalogoUseCase;
        this.mapaUseCase = mapaUseCase;
        this.dashboardUseCase = dashboardUseCase;
    }

    @GetMapping("/animales")
    public ResponseEntity<ApiResponseDTO<List<AnimalResponseDTO>>> listarAnimales(
            @RequestParam(required = false) String especie,
            @RequestParam(required = false) String raza,
            @RequestParam(required = false) Integer edadMin,
            @RequestParam(required = false) Integer edadMax,
            @RequestParam(required = false) String tamano,
            @RequestParam(required = false) String region) {

        var animales = catalogoUseCase.obtenerTodos(especie, raza, edadMin, edadMax, tamano, region);
        var dto = animales.stream()
                .map(AnimalResponseDTO::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponseDTO.ok(dto));
    }

    @GetMapping("/animales/{id}")
    public ResponseEntity<ApiResponseDTO<AnimalResponseDTO>> obtenerAnimal(@PathVariable Long id) {
        return catalogoUseCase.obtenerPorId(id)
                .map(animal -> ResponseEntity.ok(ApiResponseDTO.ok(AnimalResponseDTO.fromDomain(animal))))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/refugios/ubicaciones")
    public ResponseEntity<ApiResponseDTO<List<RefugioUbicacionDTO>>> obtenerUbicacionesRefugios() {
        var refugios = mapaUseCase.obtenerRefugiosAprobados();
        var dto = refugios.stream()
                .map(RefugioUbicacionDTO::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponseDTO.ok(dto));
    }

    @GetMapping("/dashboard/estadisticas")
    public ResponseEntity<ApiResponseDTO<EstadisticaDashboardResponseDTO>> obtenerEstadisticas() {
        var stats = dashboardUseCase.obtenerEstadisticas();
        return ResponseEntity.ok(ApiResponseDTO.ok(EstadisticaDashboardResponseDTO.fromDomain(stats)));
    }
}
