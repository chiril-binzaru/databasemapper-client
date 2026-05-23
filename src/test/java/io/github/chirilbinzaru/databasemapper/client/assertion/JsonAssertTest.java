package io.github.chirilbinzaru.databasemapper.client.assertion;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JsonAssertTest {
    @Test
    void passesForEqualJson() {
        assertDoesNotThrow(() -> JsonAssert.assertEquals(
                "{\"id\":1,\"name\":\"John\",\"pets\":[{\"id\":10}]}",
                "{\"id\":1,\"name\":\"John\",\"pets\":[{\"id\":10}]}"
        ));
    }

    @Test
    void reportsReadableMismatchesWithPaths() {
        AssertionError error = assertThrows(AssertionError.class, () -> JsonAssert.assertEquals(
                "{\"id\":1,\"name\":\"John\",\"pets\":[{\"id\":10}],\"active\":true}",
                "{\"id\":2,\"pets\":[],\"extra\":\"value\"}"
        ));

        String message = error.getMessage();
        assertTrue(message.contains("[VALUE]   $.id -> expected 1, actual 2"));
        assertTrue(message.contains("[MISSING] $.name -> expected \"John\""));
        assertTrue(message.contains("[SIZE]    $.pets -> expected 1, actual 0"));
        assertTrue(message.contains("[EXTRA]   $.extra -> actual \"value\""));
        assertFalse(message.isBlank());
    }
}
