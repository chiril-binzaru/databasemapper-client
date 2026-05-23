package io.github.chirilbinzaru.databasemapper.client.assertion;

import java.util.List;

public record JsonComparison(List<JsonMismatch> mismatches) {
    public JsonComparison {
        mismatches = List.copyOf(mismatches);
    }

    public boolean matches() {
        return mismatches.isEmpty();
    }

    public String format() {
        if (matches()) {
            return "JSON documents match";
        }

        StringBuilder builder = new StringBuilder();
        for (JsonMismatch mismatch : mismatches) {
            builder.append(System.lineSeparator()).append("- ").append(mismatch.format());
        }
        return builder.toString();
    }
}
