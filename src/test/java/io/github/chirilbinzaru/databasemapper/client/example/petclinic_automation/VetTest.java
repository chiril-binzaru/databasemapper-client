package io.github.chirilbinzaru.databasemapper.client.example.petclinic_automation;

import io.github.chirilbinzaru.databasemapper.client.assertion.JsonAssert;
import io.github.chirilbinzaru.databasemapper.client.example.models.codegen.Vet;
import org.testng.annotations.Test;

import java.util.Map;

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

}
