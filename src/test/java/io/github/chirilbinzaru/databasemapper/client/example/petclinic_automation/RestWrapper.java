package io.github.chirilbinzaru.databasemapper.client.example.petclinic_automation;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class RestWrapper {

    private final String baseUri;
    private final String basePath;

    public RestWrapper(String baseUri, String basePath) {
        this.baseUri = baseUri;
        this.basePath = basePath;
    }

    public <T> T get(String path, Class<T> type) {
        return request()
                .when().get(path)
                .then().statusCode(200)
                .extract().as(type);
    }

    public <T> T post(String path, Object body, Class<T> type) {
        return request()
                .body(body)
                .when().post(path)
                .then().statusCode(200)
                .extract().as(type);
    }

    public <T> T put(String path, Object body, Class<T> type) {
        return request()
                .body(body)
                .when().put(path)
                .then().statusCode(200)
                .extract().as(type);
    }

    private RequestSpecification request() {
        return RestAssured.given()
                .baseUri(baseUri)
                .basePath(basePath)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON);
    }
}
