package com.patipets.core.application.ports.output;

import java.util.Optional;

public interface GeocodingPort {
    Optional<double[]> geocode(String direccion, String comuna, String region);
}
