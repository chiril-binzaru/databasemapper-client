package io.github.chirilbinzaru.databasemapper.client.example;

import io.github.chirilbinzaru.databasemapper.client.assertion.JsonAssert;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

class JsonAssertExampleTest {
    @Test
    void printsReadableJsonMismatches() {
        String expectedFromDatabase = """
                {
                  "id": 1,
                  "name": "John Smith",
                  "specialty": {
                    "id": 3,
                    "name": "Cardiology"
                  },
                  "pets": [
                    { "id": 10, "name": "Buddy", "type": "Dog" }
                  ]
                }
                """;

        String actualFromService = """
                {
                  "id": 1,
                  "name": "Jon Smith",
                  "specialty": {
                    "id": 4
                  },
                  "pets": [],
                  "active": true
                }
                """;

        AssertionError assertionError = null;
        try {
            JsonAssert.assertEquals(expectedFromDatabase, actualFromService);
        } catch (AssertionError error) {
            assertionError = error;
            System.out.println(error.getMessage());
        }

        assertFalse(assertionError == null, "Example data should produce visible mismatches");
    }
}
