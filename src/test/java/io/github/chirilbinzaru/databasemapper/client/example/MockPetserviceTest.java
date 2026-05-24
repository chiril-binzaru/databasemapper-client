package io.github.chirilbinzaru.databasemapper.client.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.chirilbinzaru.databasemapper.client.DatabaseMapperClient;
import io.github.chirilbinzaru.databasemapper.client.assertion.JsonAssert;
import io.github.chirilbinzaru.databasemapper.client.config.DatabaseMapperClientConfig;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import io.github.chirilbinzaru.databasemapper.client.example.models.codegen.Vet;
import java.util.Map;

public class MockPetserviceTest {

    @Test
    void getVetByIdTest() throws Exception {
        Long vetEndpointId = 216L;

        DatabaseMapperClient dbMapperClient = new DatabaseMapperClient(
                DatabaseMapperClientConfig.builder(URI.create("http://localhost:8080")).build()
        );
        Vet expectedFromDatabase = dbMapperClient.getModel(vetEndpointId, Map.of("id", 2), Vet.class);

        HttpResponse<String> response = HttpClient.newHttpClient().send(
                HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:9966/petclinic/api/vets/2"))
                        .header("Accept", "application/json")
                        .GET()
                        .build(),
                HttpResponse.BodyHandlers.ofString()
        );
        Vet actualFromService = new ObjectMapper().readValue(response.body(), Vet.class);

        JsonAssert.assertEquals(actualFromService, expectedFromDatabase);
    }

}
