package io.github.chirilbinzaru.databasemapper.client.exception;

public final class DatabaseMapperHttpException extends DatabaseMapperClientException {
    private final int statusCode;
    private final String responseBody;

    public DatabaseMapperHttpException(int statusCode, String responseBody) {
        super("Database Mapper backend returned HTTP " + statusCode + bodySuffix(responseBody));
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }

    public int statusCode() {
        return statusCode;
    }

    public String responseBody() {
        return responseBody;
    }

    private static String bodySuffix(String responseBody) {
        if (responseBody == null || responseBody.isBlank()) {
            return "";
        }
        return ": " + responseBody;
    }
}
