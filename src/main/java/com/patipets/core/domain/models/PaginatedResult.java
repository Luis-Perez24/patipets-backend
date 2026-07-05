package com.patipets.core.domain.models;

import java.util.List;

public class PaginatedResult<T> {
    private final List<T> items;
    private final int totalPages;
    private final long totalElements;
    private final int page;
    private final int size;

    public PaginatedResult(List<T> items, int totalPages, long totalElements, int page, int size) {
        this.items = items;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.page = page;
        this.size = size;
    }

    public List<T> getItems() { return items; }
    public int getTotalPages() { return totalPages; }
    public long getTotalElements() { return totalElements; }
    public int getPage() { return page; }
    public int getSize() { return size; }
}
