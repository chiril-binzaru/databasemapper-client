package io.github.chirilbinzaru.databasemapper.client.exception;

public class DatabaseMapperClientException extends RuntimeException {
    public DatabaseMapperClientException(String message) {
        super(message);
    }

    public DatabaseMapperClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
