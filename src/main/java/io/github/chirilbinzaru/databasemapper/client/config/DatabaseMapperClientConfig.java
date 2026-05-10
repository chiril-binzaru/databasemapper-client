package io.github.chirilbinzaru.databasemapper.client.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.time.Duration;
import java.util.Objects;
import java.util.Optional;

public record DatabaseMapperClientConfig(
        URI baseUrl,
        Optional<String> bearerToken,
        Duration connectTimeout,
        Duration requestTimeout,
        ObjectMapper objectMapper
) {
    private static final Duration DEFAULT_CONNECT_TIMEOUT = Duration.ofSeconds(5);
    private static final Duration DEFAULT_REQUEST_TIMEOUT = Duration.ofSeconds(30);

    public DatabaseMapperClientConfig {
        Objects.requireNonNull(baseUrl, "baseUrl must not be null");
        bearerToken = bearerToken == null ? Optional.empty() : bearerToken;
        connectTimeout = connectTimeout == null ? DEFAULT_CONNECT_TIMEOUT : connectTimeout;
        requestTimeout = requestTimeout == null ? DEFAULT_REQUEST_TIMEOUT : requestTimeout;
        objectMapper = objectMapper == null ? new ObjectMapper() : objectMapper;
    }

    public static Builder builder(URI baseUrl) {
        return new Builder(baseUrl);
    }

    public static final class Builder {
        private final URI baseUrl;
        private Optional<String> bearerToken = Optional.empty();
        private Duration connectTimeout = DEFAULT_CONNECT_TIMEOUT;
        private Duration requestTimeout = DEFAULT_REQUEST_TIMEOUT;
        private ObjectMapper objectMapper = new ObjectMapper();

        private Builder(URI baseUrl) {
            this.baseUrl = baseUrl;
        }

        public Builder bearerToken(String bearerToken) {
            this.bearerToken = Optional.ofNullable(bearerToken).filter(token -> !token.isBlank());
            return this;
        }

        public Builder connectTimeout(Duration connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }

        public Builder requestTimeout(Duration requestTimeout) {
            this.requestTimeout = requestTimeout;
            return this;
        }

        public Builder objectMapper(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
            return this;
        }

        public DatabaseMapperClientConfig build() {
            return new DatabaseMapperClientConfig(baseUrl, bearerToken, connectTimeout, requestTimeout, objectMapper);
        }
    }
}
