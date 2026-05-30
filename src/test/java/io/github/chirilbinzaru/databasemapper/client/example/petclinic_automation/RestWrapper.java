package io.github.chirilbinzaru.databasemapper.client.example.petclinic_automation;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class RestWrapper {

    private final String baseUri;

    public RestWrapper(String baseUri) {
        this.baseUri = baseUri;
    }

    public <T> T get(String path, Class<T> type) {
        return request()
                .when().get(url(path))
                .then().statusCode(200)
                .extract().as(type);
    }

    public <T> T post(String path, Object body, Class<T> type) {
        return request()
                .body(body)
                .when().post(url(path))
                .then().statusCode(200)
                .extract().as(type);
    }

    public <T> T put(String path, Object body, Class<T> type) {
        return request()
                .body(body)
                .when().put(url(path))
                .then().statusCode(200)
                .extract().as(type);
    }

    private RequestSpecification request() {
        return RestAssured.given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON);
    }

    private String url(String path) {
        if (baseUri.endsWith("/") && path.startsWith("/")) {
            return baseUri + path.substring(1);
        }
        if (!baseUri.endsWith("/") && !path.startsWith("/")) {
            return baseUri + "/" + path;
        }
        return baseUri + path;
    }
}
