package io.github.chirilbinzaru.databasemapper.client.assertion;

import java.util.Arrays;

public enum MismatchType {
    VALUE, MISSING, SIZE, EXTRA, TYPE;

    private static final int WIDTH = Arrays.stream(values())
            .mapToInt(t -> t.name().length() + 2)
            .max()
            .orElseThrow();

    String tag() {
        return String.format("%-" + WIDTH + "s", "[" + name() + "]");
    }
}
