package io.github.chirilbinzaru.databasemapper.client.assertion;

import com.fasterxml.jackson.databind.JsonNode;

public record JsonMismatch(String path, String message) {
    static JsonMismatch type(String path, String expectedType, String actualType) {
        return new JsonMismatch(path, "type mismatch: expected " + expectedType + ", actual " + actualType);
    }

    static JsonMismatch value(String path, JsonNode expected, JsonNode actual) {
        return new JsonMismatch(path, "value mismatch: expected " + render(expected) + ", actual " + render(actual));
    }

    static JsonMismatch missing(String path, JsonNode expected) {
        return new JsonMismatch(path, "missing field: expected " + render(expected));
    }

    static JsonMismatch unexpected(String path, JsonNode actual) {
        return new JsonMismatch(path, "unexpected field: actual " + render(actual));
    }

    static JsonMismatch arraySize(String path, int expectedSize, int actualSize) {
        return new JsonMismatch(path, "array size mismatch: expected " + expectedSize + ", actual " + actualSize);
    }

    public String format() {
        return path + " -> " + message;
    }

    private static String render(JsonNode node) {
        return node == null ? "null" : node.toString();
    }
}
