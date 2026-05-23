package io.github.chirilbinzaru.databasemapper.client.example;

import io.github.chirilbinzaru.databasemapper.client.assertion.JsonAssert;
import org.junit.jupiter.api.Test;

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

        JsonAssert.assertEquals(expectedFromDatabase, actualFromService);
    }
}
