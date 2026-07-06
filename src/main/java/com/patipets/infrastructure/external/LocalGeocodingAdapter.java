package com.patipets.infrastructure.external;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.patipets.core.application.ports.output.GeocodingPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.io.InputStream;
import java.text.Normalizer;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@Component
public class LocalGeocodingAdapter implements GeocodingPort {

    private static final Logger log = LoggerFactory.getLogger(LocalGeocodingAdapter.class);
    private static final Pattern DIACRITICS = Pattern.compile("\\p{M}");

    private Map<String, double[]> comunas = Map.of();

    @PostConstruct
    void cargarComunas() {
        try (InputStream is = new ClassPathResource("geo/comunas-chile.json").getInputStream()) {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, double[]> raw = mapper.readValue(is, new com.fasterxml.jackson.core.type.TypeReference<>() {});
            comunas = raw;
            log.info("Cargadas {} comunas para geocoding local", comunas.size());
        } catch (Exception e) {
            log.error("No se pudo cargar el dataset de comunas de Chile: {}", e.getMessage());
        }
    }

    @Override
    public Optional<double[]> geocode(String direccion, String comuna, String region) {
        if (comuna == null || comuna.isBlank()) {
            return Optional.empty();
        }
        String normalizado = normalizar(comuna);
        return comunas.entrySet().stream()
                .filter(entry -> normalizar(entry.getKey()).equals(normalizado))
                .map(Map.Entry::getValue)
                .findFirst();
    }

    private String normalizar(String texto) {
        String sinTildes = DIACRITICS.matcher(Normalizer.normalize(texto, Normalizer.Form.NFD)).replaceAll("");
        return sinTildes.trim().toLowerCase();
    }
}
