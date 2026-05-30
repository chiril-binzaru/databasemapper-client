package io.github.chirilbinzaru.databasemapper.client.example.petclinic_automation;

import io.github.chirilbinzaru.databasemapper.client.DatabaseMapperClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public class BaseTest {

    private static final Properties PETCLINIC_PROPERTIES = loadPetclinicProperties();

    protected String petclinicBasePath = requiredProperty("petclinic.base-path");
    protected DatabaseMapperClient databaseMapperClient = new DatabaseMapperClient("http://localhost:8080")
            .defaultServiceName(requiredProperty("petclinic.service-name"));
    protected RestWrapper restWrapper = new RestWrapper(requiredProperty("petclinic.base-url"));

    private static Properties loadPetclinicProperties() {
        Properties properties = new Properties();
        try (InputStream inputStream = BaseTest.class.getResourceAsStream("petclinic.properties")) {
            Objects.requireNonNull(inputStream, "petclinic.properties must be available on the test classpath");
            properties.load(inputStream);
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to load petclinic.properties", exception);
        }
        return properties;
    }

    private static String requiredProperty(String propertyName) {
        String value = PETCLINIC_PROPERTIES.getProperty(propertyName);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(propertyName + " must be configured in petclinic.properties");
        }
        return value;
    }
}
