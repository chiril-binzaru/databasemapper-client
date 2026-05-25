package io.github.chirilbinzaru.databasemapper.client.example;

import io.github.chirilbinzaru.databasemapper.client.assertion.JsonAssert;
import io.github.chirilbinzaru.databasemapper.client.example.models.codegen.Specialty;
import io.github.chirilbinzaru.databasemapper.client.example.models.codegen.Vet;
import org.testng.annotations.Test;

import java.util.List;

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

        JsonAssert.assertEquals(actualFromService, expectedFromDatabase);
    }

    @Test
    void printsReadableJsonMismatchesForObjects() {
        Vet actualFromService = new Vet(1).firstName("Jon").lastName("Smith")
                .specialties(List.of(new Specialty(4)));
        Vet expectedFromDatabase = new Vet(1).firstName("John").lastName("Smith")
                .specialties(List.of(new Specialty(3).name("Cardiology")));

        JsonAssert.assertEquals(actualFromService, expectedFromDatabase);
    }

    @Test
    void printsReadableJsonMismatchesForLists() {
        List<Vet> actualFromService = List.of(
                new Vet(1).firstName("Jon").lastName("Smith").specialties(List.of(new Specialty(3).name("Cardiology"))),
                new Vet(2).firstName("Jane").lastName("Doe").specialties(List.of(new Specialty(5).name("Neurology")))
        );
        List<Vet> expectedFromDatabase = List.of(
                new Vet(1).firstName("John").lastName("Smith").specialties(List.of(new Specialty(3).name("Cardiology"))),
                new Vet(2).firstName("Jane").lastName("Doe").specialties(List.of(new Specialty(7).name("Neurology")))
        );

        JsonAssert.assertEquals(actualFromService, expectedFromDatabase);
    }
}
