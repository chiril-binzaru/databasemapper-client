package io.github.chirilbinzaru.databasemapper.client.example.petclinic_automation;

import io.github.chirilbinzaru.databasemapper.client.DatabaseMapperClient;

public class BaseTest {

    protected DatabaseMapperClient databaseMapperClient = new DatabaseMapperClient("http://localhost:8080");
    protected RestWrapper restWrapper = new RestWrapper("http://localhost:9966", "/petclinic/api");

}
