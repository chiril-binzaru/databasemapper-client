package io.github.chirilbinzaru.databasemapper.client;

import io.github.chirilbinzaru.databasemapper.client.api.EndpointDataApi;
import io.github.chirilbinzaru.databasemapper.client.config.DatabaseMapperClientConfig;

import java.net.URI;
import java.net.http.HttpClient;
import java.util.Objects;

/**
 * Main entry point used by automation tests.
 */
public final class DatabaseMapperClient {
    private final EndpointDataApi endpointDataApi;
    private String defaultServiceName;

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

    public DatabaseMapperClient defaultServiceName(String defaultServiceName) {
        Objects.requireNonNull(defaultServiceName, "defaultServiceName must not be null");
        if (defaultServiceName.isBlank()) {
            throw new IllegalArgumentException("defaultServiceName must not be blank");
        }
        this.defaultServiceName = defaultServiceName;
        return this;
    }

    public DataRequestBuilder data() {
        return new DataRequestBuilder(endpointDataApi, defaultServiceName);
    }
}
