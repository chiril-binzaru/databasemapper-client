package io.github.chirilbinzaru.databasemapper.client.example.petclinic_automation;

import io.github.chirilbinzaru.databasemapper.client.assertion.JsonAssert;
import io.github.chirilbinzaru.databasemapper.client.example.models.codegen.Vet;
import org.testng.annotations.Test;

import java.util.Map;

public class VetTest extends BaseTest {

    @Test
    void getVetByIdTest() {
        Vet expectedFromDatabase = databaseMapperClient.getModel(216L, Map.of("id", 2), Vet.class);
        Vet actualFromService = restWrapper.get("/vets/2", Vet.class);

        JsonAssert.assertEquals(actualFromService, expectedFromDatabase);
    }

}
