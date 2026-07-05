package com.patipets.infrastructure.web.dto;

import java.util.List;

public class PaginatedResponseDTO<T> {
    private List<T> contenido;
    private int totalPages;
    private long totalElements;
    private int page;
    private int size;

    public PaginatedResponseDTO(List<T> contenido, int totalPages, long totalElements, int page, int size) {
        this.contenido = contenido;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.page = page;
        this.size = size;
    }

    public List<T> getContenido() { return contenido; }
    public int getTotalPages() { return totalPages; }
    public long getTotalElements() { return totalElements; }
    public int getPage() { return page; }
    public int getSize() { return size; }
}
