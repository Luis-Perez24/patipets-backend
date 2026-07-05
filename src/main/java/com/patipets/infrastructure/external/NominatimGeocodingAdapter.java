package com.patipets.infrastructure.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.patipets.core.application.ports.output.GeocodingPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Component
public class NominatimGeocodingAdapter implements GeocodingPort {

    private static final Logger log = LoggerFactory.getLogger(NominatimGeocodingAdapter.class);

    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final String userAgent;

    public NominatimGeocodingAdapter(
            @Value("${geocoding.nominatim-url:https://nominatim.openstreetmap.org}") String baseUrl,
            @Value("${geocoding.user-agent:patipets/1.0}") String userAgent) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(3000);
        factory.setReadTimeout(5000);
        this.restTemplate = new RestTemplate(factory);
        this.baseUrl = baseUrl;
        this.userAgent = userAgent;
    }

    @Override
    public Optional<double[]> geocode(String direccion, String comuna, String region) {
        StringBuilder query = new StringBuilder();
        if (direccion != null && !direccion.isBlank()) {
            query.append(direccion.trim());
        }
        if (comuna != null && !comuna.isBlank()) {
            if (query.length() > 0) query.append(", ");
            query.append(comuna.trim());
        }
        if (region != null && !region.isBlank()) {
            if (query.length() > 0) query.append(", ");
            query.append(region.trim());
        }
        query.append(", Chile");

        if (query.length() == ", Chile".length()) {
            return Optional.empty();
        }

        String encoded = URLEncoder.encode(query.toString(), StandardCharsets.UTF_8);
        String url = baseUrl + "/search?format=json&limit=1&q=" + encoded;

        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", userAgent);
        headers.set("Accept", "application/json");

        try {
            ResponseEntity<NominatimResult[]> response = restTemplate.exchange(
                    url, HttpMethod.GET,
                    new org.springframework.http.HttpEntity<>(headers),
                    NominatimResult[].class);

            NominatimResult[] results = response.getBody();
            if (results == null || results.length == 0) {
                log.warn("Geocoding sin resultados para: {}", query);
                return Optional.empty();
            }

            NominatimResult first = results[0];
            double lat = Double.parseDouble(first.lat);
            double lon = Double.parseDouble(first.lon);
            return Optional.of(new double[]{lat, lon});
        } catch (RestClientException | NumberFormatException e) {
            log.warn("Error en geocoding para '{}': {}", query, e.getMessage());
            return Optional.empty();
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NominatimResult {
        public String lat;
        public String lon;
        public String display_name;
        public List<String> boundingbox;
    }
}
