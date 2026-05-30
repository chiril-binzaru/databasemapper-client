package io.github.chirilbinzaru.databasemapper.client.example.petclinic_automation;

import io.github.chirilbinzaru.databasemapper.client.assertion.JsonAssert;
import io.github.chirilbinzaru.databasemapper.client.example.models.codegen.Vet;
import org.testng.annotations.Test;

import java.util.Map;

public class VetTest extends BaseTest {

    @Test
    void getVetByIdTest() {
        Vet expectedFromDatabase = databaseMapperClient
                .data()
                .serviceName("spring-petclinic-rest")
                .endpointPath("/petclinic/api/vets/{vetId}")
                .httpGet()
                .filters(Map.of("id", 2))
                .getModel(Vet.class);
        Vet actualFromService = restWrapper.get("/vets/2", Vet.class);

        JsonAssert.assertEquals(actualFromService, expectedFromDatabase);
    }

}
