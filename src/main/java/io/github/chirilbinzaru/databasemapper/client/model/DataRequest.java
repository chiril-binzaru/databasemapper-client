package io.github.chirilbinzaru.databasemapper.client.model;

import java.util.Map;
import java.util.Objects;

public record DataRequest(Map<String, ?> filters) {
    public DataRequest {
        Objects.requireNonNull(filters, "filters must not be null");
        if (filters.isEmpty()) {
            throw new IllegalArgumentException("At least one filter is required");
        }
    }
}
