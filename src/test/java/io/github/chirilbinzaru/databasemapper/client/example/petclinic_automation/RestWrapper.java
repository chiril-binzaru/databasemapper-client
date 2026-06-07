package io.github.chirilbinzaru.databasemapper.client.example.petclinic_automation;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class RestWrapper {

    private final String baseUri;

    public RestWrapper(String baseUri) {
        this.baseUri = baseUri;
    }

    public <T> T get(String path, Class<T> type) {
        return get(path, type, 200);
    }

    public <T> T get(String path, Class<T> type, int expectedStatusCode) {
        return request()
                .when().get(url(path))
                .then().statusCode(expectedStatusCode)
                .extract().as(type);
    }

    public <T> List<T> getList(String path, Class<T> elementClass) {
        return getList(path, elementClass, 200);
    }

    public <T> List<T> getList(String path, Class<T> elementClass, int expectedStatusCode) {
        return request()
                .when().get(url(path))
                .then().statusCode(expectedStatusCode)
                .extract().as(listTypeRef(elementClass));
    }

    public <T> T post(String path, Object body, Class<T> type) {
        return post(path, body, type, 200);
    }

    public <T> T post(String path, Object body, Class<T> type, int expectedStatusCode) {
        return request()
                .body(body)
                .when().post(url(path))
                .then().statusCode(expectedStatusCode)
                .extract().as(type);
    }

    public <T> List<T> postList(String path, Object body, Class<T> elementClass) {
        return postList(path, body, elementClass, 200);
    }

    public <T> List<T> postList(String path, Object body, Class<T> elementClass, int expectedStatusCode) {
        return request()
                .body(body)
                .when().post(url(path))
                .then().statusCode(expectedStatusCode)
                .extract().as(listTypeRef(elementClass));
    }

    public <T> T put(String path, Object body, Class<T> type) {
        return put(path, body, type, 200);
    }

    public <T> T put(String path, Object body, Class<T> type, int expectedStatusCode) {
        return request()
                .body(body)
                .when().put(url(path))
                .then().statusCode(expectedStatusCode)
                .extract().as(type);
    }

    private <T> TypeRef<List<T>> listTypeRef(Class<T> elementClass) {
        return new TypeRef<List<T>>() {
            @Override
            public Type getType() {
                return new ParameterizedType() {
                    public Type[] getActualTypeArguments() { return new Type[]{elementClass}; }
                    public Type getRawType() { return List.class; }
                    public Type getOwnerType() { return null; }
                };
            }
        };
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
