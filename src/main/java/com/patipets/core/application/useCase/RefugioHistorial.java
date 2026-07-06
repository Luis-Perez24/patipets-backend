package com.patipets.core.application.useCase;

import com.patipets.core.domain.models.Animal;
import java.util.List;

public class RefugioHistorial {
    private final Long refugioId;
    private final String refugioNombre;
    private final long totalAnimales;
    private final long totalAdopciones;
    private final List<Animal> animalesAdoptados;

    public RefugioHistorial(Long refugioId, String refugioNombre, long totalAnimales,
                            long totalAdopciones, List<Animal> animalesAdoptados) {
        this.refugioId = refugioId;
        this.refugioNombre = refugioNombre;
        this.totalAnimales = totalAnimales;
        this.totalAdopciones = totalAdopciones;
        this.animalesAdoptados = animalesAdoptados;
    }

    public Long getRefugioId() { return refugioId; }
    public String getRefugioNombre() { return refugioNombre; }
    public long getTotalAnimales() { return totalAnimales; }
    public long getTotalAdopciones() { return totalAdopciones; }
    public List<Animal> getAnimalesAdoptados() { return animalesAdoptados; }
}
