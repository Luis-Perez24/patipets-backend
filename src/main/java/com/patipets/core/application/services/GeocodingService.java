package com.patipets.core.application.services;

import com.patipets.core.application.ports.output.GeocodingPort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GeocodingService {

    private final GeocodingPort port;
    private final ConcurrentHashMap<String, double[]> cache = new ConcurrentHashMap<>();

    public GeocodingService(GeocodingPort port) {
        this.port = port;
    }

    public Optional<double[]> geocode(String direccion, String comuna, String region) {
        String key = buildKey(direccion, comuna, region);
        if (key.isEmpty()) {
            return Optional.empty();
        }
        double[] cached = cache.get(key);
        if (cached != null) {
            return Optional.of(cached);
        }
        Optional<double[]> result = port.geocode(direccion, comuna, region);
        result.ifPresent(coords -> cache.put(key, coords));
        return result;
    }

    private String buildKey(String direccion, String comuna, String region) {
        StringBuilder sb = new StringBuilder();
        if (direccion != null) sb.append(direccion.trim().toLowerCase());
        sb.append('|');
        if (comuna != null) sb.append(comuna.trim().toLowerCase());
        sb.append('|');
        if (region != null) sb.append(region.trim().toLowerCase());
        return sb.toString();
    }
}
