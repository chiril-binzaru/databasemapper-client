package io.github.chirilbinzaru.databasemapper.client.model;

import java.util.Map;
import java.util.Objects;

public record DataRequest(
        String serviceName,
        String endpointPath,
        String httpMethod,
        Map<String, ?> filters
) {
    public DataRequest {
        serviceName = requireNonBlank(serviceName, "serviceName");
        endpointPath = requireNonBlank(endpointPath, "endpointPath");
        httpMethod = requireNonBlank(httpMethod, "httpMethod").toUpperCase();
        Objects.requireNonNull(filters, "filters must not be null");
        if (filters.isEmpty()) {
            throw new IllegalArgumentException("At least one filter is required");
        }
    }

    private static String requireNonBlank(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName + " must not be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
        return value;
    }
}
