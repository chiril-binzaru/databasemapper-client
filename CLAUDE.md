# databasemapper-client — CLAUDE.md

## Overview

`databasemapper-client` is a framework-agnostic Java library consumed by QA automation projects.
It calls the `databasemapper-web-backend` REST API to retrieve data from target databases,
already hydrated into the shape of the model objects that the tested service returns — ready to
be used directly in test assertions.

The library is distributed as a Maven dependency and is imported via `pom.xml` in consumer
test projects.

## Sister Project

This library is the client-side counterpart to the `databasemapper-web-backend` Spring Boot
application. The backend stores service-to-database mappings and exposes a **Model Hydration API**
(`POST /api/v1/endpoints/{endpointId}/data`) that accepts filter conditions and returns data from
the target database shaped as typed JSON objects — exactly as the service endpoint would return
them.

This library wraps those HTTP calls and presents a clean Java API to the test author, hiding
HTTP, JSON, and configuration concerns entirely.

### Backend database schema (for reference)

```
databases    (database_id, database_type, database_host, database_name)
services     (service_id, database_id, service_name, service_base_url, swagger_endpoint)
connections  (connection_id, database_id, port, username, password, connection_string,
              connection_mode, is_active)
endpoints    (endpoint_id, service_id, http_method, endpoint_path, response_model JSONB)
mappings     (mapping_id, endpoint_id, mapping_data JSONB)
```

## Purpose

- Wrap HTTP calls to the `databasemapper-web-backend` API
- Deserialize JSON responses into typed model objects
- Provide a minimal, clean API for QA testers to use inside any test framework

## Tech Stack

| Layer         | Technology                               |
|---------------|------------------------------------------|
| Language      | Java 21 (Temurin LTS)                    |
| Build tool    | Maven (no wrapper — plain `mvn`)         |
| HTTP client   | Java built-in `java.net.http.HttpClient` |
| Serialization | Jackson (`jackson-databind`)             |
| Testing       | JUnit 5 + Mockito                        |

No framework (Spring, Quarkus, etc.) is used — intentionally. The library must remain
framework-agnostic so it can be imported into any consumer project without imposing transitive
dependencies.

## Project Coordinates

```xml
<groupId>io.github.yourusername</groupId>
<artifactId>databasemapper-client</artifactId>
```

> Replace `yourusername` with the actual GitHub username / Maven group ID before publishing.

## Project Structure

```
databasemapper-client/
├── src/
│   ├── main/java/io/github/yourusername/databasemapper/client/
│   │   ├── DatabaseMapperClient.java   # Main entry point for consumers
│   │   ├── config/                     # Client configuration (base URL, auth, timeouts)
│   │   ├── api/                        # One class per backend API resource group
│   │   ├── model/                      # POJOs representing API response shapes
│   │   └── exception/                  # Library-specific exceptions
│   └── test/java/io/github/yourusername/databasemapper/client/
├── pom.xml
└── CLAUDE.md
```

## Key Design Constraints

- **No framework dependencies** — do not introduce Spring, Guice, or any DI container.
- **Minimal transitive dependencies** — keep the dependency footprint small; consumers own their
  classpaths.
- **Configuration over convention** — base URL, timeouts, and auth must be supplied by the
  consumer; no hardcoded values.
- **No business logic** — this library only calls the backend API and deserializes responses. It
  does not construct queries, connect to databases, or replicate any backend logic.

## Backend API — Endpoints Relevant to This Library

All paths are relative to `{baseUrl}/api/v1/`.

### Primary — Model Hydration

#### `POST /endpoints/{endpointId}/data`

The core endpoint. Fetches data from the target database and returns it as a JSON object shaped
exactly as the model documented for that endpoint.

**Request body:**
```
{
  "filters": {
    "<modelFieldName>": <value>
  }
}
```

`filters` keys are **model field names** (as defined in the mapping's `serviceInfo.modelField`),
not database column names. The backend resolves them to the correct columns internally.
At least one filter is required.

**Response:** `200 OK` — a JSON object (or nested structure) matching the endpoint's response
model shape. The exact shape depends on the stored mapping:

```json
{
  "id": 1,
  "name": "John Smith",
  "specialty": {
    "id": 3,
    "name": "Cardiology"
  },
  "pets": [
    { "id": 10, "name": "Buddy", "type": "Dog" }
  ]
}
```

**Error responses:**

| Status | Meaning                                                               |
|--------|-----------------------------------------------------------------------|
| 400    | Filter field not found in the mapping, or no filter provided          |
| 404    | Endpoint not found, mapping not found, or no data matches the filters |
| 422    | Database connection or query failure                                  |

---

### Supporting — Endpoint Discovery

#### `GET /services/{serviceId}/endpoints`

Returns all endpoints registered for a service, including their `endpointId`, HTTP method,
and path. Useful for resolving an endpoint by path when the ID is not known upfront.

**Response:** `200 OK`
```json
{
  "serviceId": 5,
  "endpoints": [
    { "endpointId": 216, "httpMethod": "GET", "endpointPath": "/api/v1/owners/{ownerId}" }
  ]
}
```

#### `GET /endpoints/{endpointId}/responseModel`

Returns the committed response model snapshot for an endpoint as a JSON Schema-like tree.
Useful for inspecting the model shape without fetching live data.

**Response:** `200 OK` — raw `JsonNode`; `null` body if no model has been committed yet.

---

### Supporting — Mapping Inspection

#### `GET /endpoints/{endpointId}/mapping`

Returns the full mapping configuration for an endpoint. Useful for debugging or generating
typed model classes from the mapping.

**Response:** `200 OK` — `MappingDto` JSON, or `null` body if no mapping exists yet:

```json
{
  "modelName": "Owner",
  "fieldMappings": [
    {
      "kind": "VALUE",
      "serviceInfo": { "modelField": "id", "type": "integer", "format": "int64" },
      "databaseInfo": { "columnPath": "public.owners.id", "columnType": "bigint", "primaryKey": true }
    },
    {
      "kind": "LIST_OF_MODELS",
      "serviceInfo": { "modelField": "pets", "modelName": "Pet" },
      "fieldMappings": [
        {
          "kind": "VALUE",
          "serviceInfo": { "modelField": "id", "type": "integer", "format": "int64" },
          "databaseInfo": { "columnPath": "public.pets.id", "columnType": "bigint", "primaryKey": true }
        }
      ]
    }
  ],
  "joins": [
    { "left": "public.owners.id", "right": "public.pets.owner_id" }
  ]
}
```

**`kind` values:** `VALUE`, `LIST_OF_VALUES`, `MODEL`, `LIST_OF_MODELS`

**`columnPath` format:** `<schema>.<table>.<column>` (e.g. `public.owners.first_name`)

---

## Development Notes

- The backend runs locally on `http://localhost:8080` during development.
- The mock service under test is Spring PetClinic REST, running on port `9966` with its own
  PostgreSQL on `5433`.
- The backend's own PostgreSQL runs on `5432`.
- Keep test coverage focused on HTTP interaction and deserialization. Use Mockito to mock HTTP
  responses — do not start a real backend in unit tests.
