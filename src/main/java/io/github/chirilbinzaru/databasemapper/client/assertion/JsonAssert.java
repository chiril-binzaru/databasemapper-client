package io.github.chirilbinzaru.databasemapper.client.assertion;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class JsonAssert {
    private static final ObjectMapper DEFAULT_MAPPER = new ObjectMapper();

    private JsonAssert() {
    }

    public static void assertEquals(String actual, String expected) {
        assertEquals(readTree(actual), readTree(expected));
    }

    public static <T> void assertEquals(T actual, T expected) {
        assertEquals(DEFAULT_MAPPER.valueToTree(actual), (JsonNode) DEFAULT_MAPPER.valueToTree(expected));
    }

    public static <T> void assertEquals(List<T> actual, List<T> expected) {
        assertEquals(DEFAULT_MAPPER.valueToTree(actual), (JsonNode) DEFAULT_MAPPER.valueToTree(expected));
    }

    public static void assertEquals(JsonNode actual, JsonNode expected) {
        JsonComparison comparison = compare(expected, actual);
        if (!comparison.matches()) {
            throw new AssertionError(comparison.format());
        }
    }

    private static JsonComparison compare(JsonNode expected, JsonNode actual) {
        Objects.requireNonNull(actual, "actual must not be null");
        Objects.requireNonNull(expected, "expected must not be null");

        List<JsonMismatch> mismatches = new ArrayList<>();
        compareNode("$", expected, actual, mismatches);
        return new JsonComparison(mismatches);
    }

    private static void compareNode(String path, JsonNode expected, JsonNode actual, List<JsonMismatch> mismatches) {
        if (expected == null && actual == null) {
            return;
        }
        if (expected == null || actual == null) {
            mismatches.add(JsonMismatch.value(path, expected, actual));
            return;
        }
        if (expected.getNodeType() != actual.getNodeType()) {
            mismatches.add(JsonMismatch.type(path, expected.getNodeType().name(), actual.getNodeType().name()));
            return;
        }
        if (expected.isObject()) {
            compareObject(path, expected, actual, mismatches);
            return;
        }
        if (expected.isArray()) {
            compareArray(path, expected, actual, mismatches);
            return;
        }
        if (!expected.equals(actual)) {
            mismatches.add(JsonMismatch.value(path, expected, actual));
        }
    }

    private static void compareObject(String path, JsonNode expected, JsonNode actual, List<JsonMismatch> mismatches) {
        Iterator<Map.Entry<String, JsonNode>> expectedFields = expected.fields();
        while (expectedFields.hasNext()) {
            Map.Entry<String, JsonNode> field = expectedFields.next();
            String fieldPath = path + "." + field.getKey();
            if (!actual.has(field.getKey())) {
                mismatches.add(JsonMismatch.missing(fieldPath, field.getValue()));
                continue;
            }
            compareNode(fieldPath, field.getValue(), actual.get(field.getKey()), mismatches);
        }

        Iterator<String> actualFields = actual.fieldNames();
        while (actualFields.hasNext()) {
            String fieldName = actualFields.next();
            if (!expected.has(fieldName)) {
                mismatches.add(JsonMismatch.unexpected(path + "." + fieldName, actual.get(fieldName)));
            }
        }
    }

    private static void compareArray(String path, JsonNode expected, JsonNode actual, List<JsonMismatch> mismatches) {
        if (expected.size() != actual.size()) {
            mismatches.add(JsonMismatch.arraySize(path, expected.size(), actual.size()));
        }

        int comparableSize = Math.min(expected.size(), actual.size());
        for (int index = 0; index < comparableSize; index++) {
            compareNode(path + "[" + index + "]", expected.get(index), actual.get(index), mismatches);
        }
    }

    private static JsonNode readTree(String json) {
        try {
            return DEFAULT_MAPPER.readTree(json);
        } catch (JsonProcessingException exception) {
            throw new IllegalArgumentException("Invalid JSON", exception);
        }
    }
}
