package io.github.chirilbinzaru.databasemapper.client.example.petclinic_automation;

import io.github.chirilbinzaru.databasemapper.client.assertion.JsonAssert;
import io.github.chirilbinzaru.databasemapper.client.example.models.codegen.Specialty;
import io.github.chirilbinzaru.databasemapper.client.example.models.codegen.Vet;
import org.testng.annotations.Test;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class VetTest extends BaseTest {

    @Test
    void getVetByIdTest() {
        Integer vetId = 2;

        Vet expectedFromDatabase = databaseMapperClient
                .data()
                .endpointPath(petclinicBasePath + "/vets/{vetId}")
                .httpGET()
                .filters(Map.of("id", vetId))
                .getModel(Vet.class);
        Vet actualFromService = restWrapper.get(petclinicBasePath + "/vets/" + vetId, Vet.class);

        JsonAssert.assertEquals(actualFromService, expectedFromDatabase);
    }

    @Test
    void getVetsTest() {
        List<Vet> expectedFromDatabase = databaseMapperClient
                .data()
                .endpointPath(petclinicBasePath + "/vets")
                .httpGET()
                .getList(Vet.class);
        List<Vet> actualFromService = restWrapper.getList(petclinicBasePath + "/vets", Vet.class);

        expectedFromDatabase.sort(Comparator.comparing(Vet::getId));
        actualFromService.sort(Comparator.comparing(Vet::getId));

        for (Vet vet : expectedFromDatabase) vet.getSpecialties().sort(Comparator.comparing(Specialty::getId));
        for (Vet vet : actualFromService) vet.getSpecialties().sort(Comparator.comparing(Specialty::getId));

        JsonAssert.assertEquals(actualFromService, expectedFromDatabase);
    }

    @Test
    void createVetTest() {
        Vet newVet = new Vet()
                .firstName(randomName())
                .lastName(randomName())
                .specialties(List.of(
                        new Specialty(1).name("radiology"),
                        new Specialty(2).name("surgery"),
                        new Specialty(3).name("dentistry")
                ));

        Vet createdVetFromService = restWrapper
                .post(petclinicBasePath + "/vets", newVet, Vet.class, 201);

        Vet createdVetFromDatabase = databaseMapperClient
                .data()
                .endpointPath(petclinicBasePath + "/vets")
                .httpPOST()
                .filters(Map.of("id", createdVetFromService.getId()))
                .getModel(Vet.class);

        createdVetFromService.getSpecialties().sort(Comparator.comparing(Specialty::getId));
        createdVetFromDatabase.getSpecialties().sort(Comparator.comparing(Specialty::getId));

        JsonAssert.assertEquals(createdVetFromService, createdVetFromDatabase);
    }

    private String randomName() {
        Random random = new Random();
        char[] name = new char[8];
        name[0] = (char) ('A' + random.nextInt(26));
        for (int i = 1; i < 8; i++) {
            name[i] = (char) ('a' + random.nextInt(26));
        }
        return new String(name);
    }

}
