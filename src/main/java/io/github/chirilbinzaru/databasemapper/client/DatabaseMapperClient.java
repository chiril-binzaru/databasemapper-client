package io.github.chirilbinzaru.databasemapper.client;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.chirilbinzaru.databasemapper.client.api.EndpointDataApi;
import io.github.chirilbinzaru.databasemapper.client.config.DatabaseMapperClientConfig;

import java.net.URI;
import java.net.http.HttpClient;
import java.util.Map;
import java.util.Objects;

/**
 * Main entry point used by automation tests.
 */
public final class DatabaseMapperClient {
    private final EndpointDataApi endpointDataApi;

    public DatabaseMapperClient(String baseUrl) {
        this(DatabaseMapperClientConfig.builder(URI.create(baseUrl)).build());
    }

    public DatabaseMapperClient(DatabaseMapperClientConfig config) {
        this(config, HttpClient.newBuilder()
                .connectTimeout(config.connectTimeout())
                .build());
    }

    public DatabaseMapperClient(DatabaseMapperClientConfig config, HttpClient httpClient) {
        Objects.requireNonNull(config, "config must not be null");
        this.endpointDataApi = new EndpointDataApi(config, Objects.requireNonNull(httpClient, "httpClient must not be null"));
    }

    public JsonNode getJson(long endpointId, Map<String, ?> filters) {
        return endpointDataApi.getData(endpointId, filters);
    }

    public <T> T getModel(long endpointId, Map<String, ?> filters, Class<T> modelClass) {
        return endpointDataApi.getData(endpointId, filters, modelClass);
    }
}
