package com.patipets.infrastructure.web.controller;

import com.patipets.core.application.useCase.ConsultarCatalogoPublicoUseCase;
import com.patipets.core.application.useCase.ConsultarEstadisticasDashboardUseCase;
import com.patipets.core.application.useCase.ConsultarMapaRefugiosUseCase;
import com.patipets.core.application.useCase.GestionRefugioUseCase;
import com.patipets.core.application.useCase.RefugioHistorial;
import com.patipets.core.domain.enums.EstadoRefugio;
import com.patipets.core.domain.models.Animal;
import com.patipets.core.domain.models.Refugio;
import com.patipets.infrastructure.web.dto.AnimalResponseDTO;
import com.patipets.infrastructure.web.dto.ApiResponseDTO;
import com.patipets.infrastructure.web.dto.EstadisticaDashboardResponseDTO;
import com.patipets.infrastructure.web.dto.RefugioHistorialResponseDTO;
import com.patipets.infrastructure.web.dto.RefugioResponseDTO;
import com.patipets.infrastructure.web.dto.RefugioUbicacionDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/public")
public class PublicController {

    private final ConsultarCatalogoPublicoUseCase catalogoUseCase;
    private final ConsultarMapaRefugiosUseCase mapaUseCase;
    private final ConsultarEstadisticasDashboardUseCase dashboardUseCase;
    private final GestionRefugioUseCase gestionRefugioUseCase;

    public PublicController(ConsultarCatalogoPublicoUseCase catalogoUseCase,
                             ConsultarMapaRefugiosUseCase mapaUseCase,
                             ConsultarEstadisticasDashboardUseCase dashboardUseCase,
                             GestionRefugioUseCase gestionRefugioUseCase) {
        this.catalogoUseCase = catalogoUseCase;
        this.mapaUseCase = mapaUseCase;
        this.dashboardUseCase = dashboardUseCase;
        this.gestionRefugioUseCase = gestionRefugioUseCase;
    }

    @GetMapping("/refugios/{id}/historial")
    public ResponseEntity<ApiResponseDTO<RefugioHistorialResponseDTO>> obtenerHistorial(@PathVariable Long id) {
        return gestionRefugioUseCase.obtenerPorId(id)
                .filter(refugio -> refugio.getEstado() == EstadoRefugio.APROBADO)
                .map(refugio -> {
                    RefugioHistorial historial = gestionRefugioUseCase.obtenerHistorial(id);
                    List<AnimalResponseDTO> animalesDto = historial.getAnimalesAdoptados().stream()
                            .map(AnimalResponseDTO::fromDomain)
                            .collect(Collectors.toList());
                    RefugioHistorialResponseDTO dto = RefugioHistorialResponseDTO.of(
                            historial.getRefugioId(), historial.getRefugioNombre(),
                            historial.getTotalAnimales(), historial.getTotalAdopciones(),
                            animalesDto);
                    return ResponseEntity.ok(ApiResponseDTO.ok(dto));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/animales")
    public ResponseEntity<ApiResponseDTO<List<AnimalResponseDTO>>> listarAnimales(
            @RequestParam(required = false) String especie,
            @RequestParam(required = false) String raza,
            @RequestParam(required = false) Integer edadMin,
            @RequestParam(required = false) Integer edadMax,
            @RequestParam(required = false) String tamano,
            @RequestParam(required = false) String region) {

        List<Animal> animales = catalogoUseCase.obtenerTodos(especie, raza, edadMin, edadMax, tamano, region);
        Map<Long, Refugio> refugiosPorId = cargarRefugios(animales);
        var dto = animales.stream()
                .map(a -> enriquecer(AnimalResponseDTO.fromDomain(a),
                        refugiosPorId.get(a.getRefugioId())))
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponseDTO.ok(dto));
    }

    @GetMapping("/animales/{id}")
    public ResponseEntity<ApiResponseDTO<AnimalResponseDTO>> obtenerAnimal(@PathVariable Long id) {
        return catalogoUseCase.obtenerPorId(id)
                .map(animal -> {
                    var dto = AnimalResponseDTO.fromDomain(animal);
                    if (animal.getRefugioId() != null) {
                        mapaUseCase.obtenerRefugiosAprobados().stream()
                                .filter(r -> r.getId().equals(animal.getRefugioId()))
                                .findFirst()
                                .ifPresent(refugio -> aplicarDatosRefugio(dto, refugio));
                    }
                    return ResponseEntity.ok(ApiResponseDTO.ok(dto));
                })
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

    @GetMapping("/refugios/{id}")
    public ResponseEntity<ApiResponseDTO<RefugioResponseDTO>> obtenerRefugio(@PathVariable Long id) {
        return gestionRefugioUseCase.obtenerPorId(id)
                .filter(refugio -> refugio.getEstado() == EstadoRefugio.APROBADO)
                .map(refugio -> ResponseEntity.ok(ApiResponseDTO.ok(RefugioResponseDTO.fromDomain(refugio))))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/dashboard/estadisticas")
    public ResponseEntity<ApiResponseDTO<EstadisticaDashboardResponseDTO>> obtenerEstadisticas() {
        var stats = dashboardUseCase.obtenerEstadisticas();
        return ResponseEntity.ok(ApiResponseDTO.ok(EstadisticaDashboardResponseDTO.fromDomain(stats)));
    }

    private Map<Long, Refugio> cargarRefugios(List<Animal> animales) {
        Set<Long> refugioIds = animales.stream()
                .map(Animal::getRefugioId)
                .filter(id -> id != null)
                .collect(Collectors.toSet());
        if (refugioIds.isEmpty()) {
            return Map.of();
        }
        return mapaUseCase.obtenerRefugiosAprobados().stream()
                .filter(r -> refugioIds.contains(r.getId()))
                .collect(Collectors.toMap(Refugio::getId, Function.identity()));
    }

    private AnimalResponseDTO enriquecer(AnimalResponseDTO dto, Refugio refugio) {
        if (refugio != null) {
            aplicarDatosRefugio(dto, refugio);
        }
        return dto;
    }

    private void aplicarDatosRefugio(AnimalResponseDTO dto, Refugio refugio) {
        dto.setRefugioNombre(refugio.getNombre());
        dto.setRefugioTelefono(refugio.getNumeroContacto());
        dto.setRefugioEmail(refugio.getEmail());
        dto.setRefugioDireccion(refugio.getDireccion());
        dto.setRefugioRegion(refugio.getRegion());
        dto.setRefugioComuna(refugio.getComuna());
    }
}
