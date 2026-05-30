package io.github.chirilbinzaru.databasemapper.client;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.chirilbinzaru.databasemapper.client.api.EndpointDataApi;

import java.util.Map;
import java.util.Objects;

public final class DataRequestBuilder {
    private final EndpointDataApi endpointDataApi;
    private String serviceName;
    private String endpointPath;
    private String httpMethod;
    private Map<String, ?> filters;

    DataRequestBuilder(EndpointDataApi endpointDataApi, String defaultServiceName) {
        this.endpointDataApi = Objects.requireNonNull(endpointDataApi, "endpointDataApi must not be null");
        this.serviceName = defaultServiceName;
    }

    public DataRequestBuilder serviceName(String serviceName) {
        this.serviceName = serviceName;
        return this;
    }

    public DataRequestBuilder endpointPath(String endpointPath) {
        this.endpointPath = endpointPath;
        return this;
    }

    public DataRequestBuilder httpGET() {
        return httpMethod("GET");
    }

    public DataRequestBuilder httpPOST() {
        return httpMethod("POST");
    }

    public DataRequestBuilder httpPUT() {
        return httpMethod("PUT");
    }

    public DataRequestBuilder httpPATCH() {
        return httpMethod("PATCH");
    }

    public DataRequestBuilder httpDELETE() {
        return httpMethod("DELETE");
    }

    private DataRequestBuilder httpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
        return this;
    }

    public DataRequestBuilder filters(Map<String, ?> filters) {
        this.filters = filters;
        return this;
    }

    public JsonNode getJsonNode() {
        return endpointDataApi.getData(serviceName, endpointPath, httpMethod, filters);
    }

    public <T> T getModel(Class<T> modelClass) {
        return endpointDataApi.getData(serviceName, endpointPath, httpMethod, filters, modelClass);
    }
}
