package io.github.chirilbinzaru.databasemapper.client.assertion;

import com.fasterxml.jackson.databind.JsonNode;

public record JsonMismatch(String path, MismatchType type, String description) {
    static JsonMismatch type(String path, String expectedType, String actualType) {
        return new JsonMismatch(path, MismatchType.TYPE, "expected " + expectedType + ", actual " + actualType);
    }

    static JsonMismatch value(String path, JsonNode expected, JsonNode actual) {
        return new JsonMismatch(path, MismatchType.VALUE, "expected " + render(expected) + ", actual " + render(actual));
    }

    static JsonMismatch missing(String path, JsonNode expected) {
        return new JsonMismatch(path, MismatchType.MISSING, "expected " + render(expected));
    }

    static JsonMismatch unexpected(String path, JsonNode actual) {
        return new JsonMismatch(path, MismatchType.EXTRA, "actual " + render(actual));
    }

    static JsonMismatch arraySize(String path, int expectedSize, int actualSize) {
        return new JsonMismatch(path, MismatchType.SIZE, "expected " + expectedSize + ", actual " + actualSize);
    }

    public String format() {
        return type.tag() + " " + path + " -> " + description;
    }

    private static String render(JsonNode node) {
        return node == null ? "null" : node.toString();
    }
}
