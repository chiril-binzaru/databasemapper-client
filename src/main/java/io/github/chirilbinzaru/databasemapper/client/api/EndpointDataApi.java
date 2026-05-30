package io.github.chirilbinzaru.databasemapper.client.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.chirilbinzaru.databasemapper.client.config.DatabaseMapperClientConfig;
import io.github.chirilbinzaru.databasemapper.client.exception.DatabaseMapperClientException;
import io.github.chirilbinzaru.databasemapper.client.exception.DatabaseMapperHttpException;
import io.github.chirilbinzaru.databasemapper.client.model.DataRequest;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Objects;

public final class EndpointDataApi {
    private final DatabaseMapperClientConfig config;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public EndpointDataApi(DatabaseMapperClientConfig config, HttpClient httpClient) {
        this.config = Objects.requireNonNull(config, "config must not be null");
        this.httpClient = Objects.requireNonNull(httpClient, "httpClient must not be null");
        this.objectMapper = config.objectMapper();
    }

    public JsonNode getData(String serviceName, String endpointPath, String httpMethod, Map<String, ?> filters) {
        DataRequest dataRequest = new DataRequest(serviceName, endpointPath, httpMethod, filters);
        HttpRequest request = requestBuilder(dataUri())
                .POST(HttpRequest.BodyPublishers.ofString(toJson(dataRequest)))
                .build();

        HttpResponse<String> response = send(request);
        ensureSuccess(response);
        return fromJson(response.body(), JsonNode.class);
    }

    public <T> T getData(String serviceName, String endpointPath, String httpMethod, Map<String, ?> filters, Class<T> modelClass) {
        Objects.requireNonNull(modelClass, "modelClass must not be null");
        return objectMapper.convertValue(getData(serviceName, endpointPath, httpMethod, filters), modelClass);
    }

    private HttpRequest.Builder requestBuilder(URI uri) {
        HttpRequest.Builder builder = HttpRequest.newBuilder(uri)
                .timeout(config.requestTimeout())
                .header("Accept", "application/json")
                .header("Content-Type", "application/json");

        config.bearerToken().ifPresent(token -> builder.header("Authorization", "Bearer " + token));
        return builder;
    }

    private URI dataUri() {
        return config.baseUrl().resolve("/api/v1/data");
    }

    private String toJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException exception) {
            throw new DatabaseMapperClientException("Failed to serialize request body", exception);
        }
    }

    private <T> T fromJson(String body, Class<T> type) {
        try {
            return objectMapper.readValue(body, type);
        } catch (JsonProcessingException exception) {
            throw new DatabaseMapperClientException("Failed to deserialize backend response", exception);
        }
    }

    private HttpResponse<String> send(HttpRequest request) {
        try {
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException exception) {
            throw new DatabaseMapperClientException("Failed to call Database Mapper backend", exception);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new DatabaseMapperClientException("Interrupted while calling Database Mapper backend", exception);
        }
    }

    private void ensureSuccess(HttpResponse<String> response) {
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new DatabaseMapperHttpException(response.statusCode(), response.body());
        }
    }
}
