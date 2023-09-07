import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.*;



import java.util.List;
import java.util.Objects;

import static io.restassured.RestAssured.given;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ResourcesCrudTest {

    private String token;
    private String applicationQa = "siie.qa.interedes.com.co";
    private String appQa = "application";

    private String IdResourceTypeCreated;

    private String IdResourceCreated;


    @BeforeEach
    public void setUp(){
        var tkn = new TokenGenerator();
        this.token = tkn.getToken();
    }


    @Order(1)
    @Test
    public void getAllResourcesTypeTest(){
        given()
                .log()
                .all()
                .header(appQa,applicationQa)
                .header("Authorization", this.token)
                .header("tenant","INTEREDES")
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"filters\": [\n" +
                        "        {\n" +
                        "            \"key\": \"id\",\n" +
                        "            \"operator\": \"LIKE_NUMBER\",\n" +
                        "            \"field_type\": \"INTEGER\",\n" +
                        "            \"value\": \"1\",\n" +
                        "            \"sql_connector\": \"OR\"\n" +
                        "        }\n" +
                        "    ],\n" +
                        "    \"sorts\": [],\n" +
                        "    \"page\": 0,\n" +
                        "    \"size\": 10\n" +
                        "}")
                .post("/dynamic-service/services/parameters-service/resourcetype/v1/get_resource_type")
                .then()
                .log()
                .all().statusCode(200)
                .extract()
                .body()
                .asString();

    }

    @Order(2)
    @Test
    public void createResourceTypeTest(){

        IdResourceTypeCreated =
                given()
                .log()
                .all()
                .header(appQa,applicationQa)
                .header("Authorization", this.token)
                .header("tenant","INTEREDES")
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"description\": \"Tipo de recurso para prueba\",\n" +
                        "    \"status\": 1\n" +
                        "}")
                .post("/dynamic-service/services/parameters-service/resourcetype/v1/create_resource_type")
                .then()
                .log()
                .all()
                .statusCode(200)
                .extract()
                .path("data.id")
                .toString();

        System.out.println(IdResourceTypeCreated);


    }

    @Order(3)
    @Test
    public void updateResourceTypeTest(){

        given()
                .log()
                .all()
                .header(appQa,applicationQa)
                .header("Authorization", this.token)
                .header("tenant","INTEREDES")
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"id\":"+IdResourceTypeCreated+",\n" +
                        "    \"description\": \"Nuevo tipo de recurso para QA\",\n" +
                        "    \"status\": 1\n" +
                        "}")
                .put("/dynamic-service/services/parameters-service/resourcetype/v1/update_resource_type")
                .then()
                .log()
                .all()
                .statusCode(200)
                .extract()
                .path("data")
                .toString();


    }



    @Order(4)
    @Test
    public void getAllResourcesTest(){
        given()
                .log()
                .all()
                .header(appQa,applicationQa)
                .header("Authorization", this.token)
                .header("tenant","INTEREDES")
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"filters\": [\n" +
                        "        {\n" +
                        "            \"key\": \"id\",\n" +
                        "            \"operator\": \"LIKE_NUMBER\",\n" +
                        "            \"field_type\": \"INTEGER\",\n" +
                        "            \"value\": \"1\",\n" +
                        "            \"sql_connector\": \"OR\"\n" +
                        "        }\n" +
                        "    ],\n" +
                        "    \"sorts\": [],\n" +
                        "    \"page\": 0,\n" +
                        "    \"size\": 10\n" +
                        "}")
                .post("/dynamic-service/services/parameters-service/resource/v1/get_resource")
                .then()
                .log()
                .all().statusCode(200)
                .extract()
                .body()
                .asString();

    }

    @Order(5)
    @Test
    public void createResourceTest(){

        IdResourceCreated =
        given()
                .log()
                .all()
                .header(appQa,applicationQa)
                .header("Authorization", this.token)
                .header("tenant","INTEREDES")
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"resourceTypeId\":"+ IdResourceTypeCreated +",\n" +
                        "    \"description\": \"Recurso para realizar pruebas en QA\",\n" +
                        "    \"status\": 1\n" +
                        "}")
                .post("/dynamic-service/services/parameters-service/resource/v1/create_resource")
                .then()
                .log()
                .all()
                .statusCode(200)
                .extract()
                .path("data.id")
                .toString();

        System.out.println(IdResourceCreated);


    }

    @Order(6)
    @Test
    public void updateResourceTest(){

                 given()
                        .log()
                        .all()
                        .header(appQa,applicationQa)
                        .header("Authorization", this.token)
                        .header("tenant","INTEREDES")
                        .contentType(ContentType.JSON)
                        .body("{\n" +
                                "    \"id\":"+IdResourceCreated+" ,\n" +
                                "    \"resourceTypeId\":"+IdResourceTypeCreated+" ,\n" +
                                "    \"resourceTypeDescription\": null,\n" +
                                "    \"description\": \"Recurso para realizar pruebas en QA/QC\",\n" +
                                "    \"status\": 1\n" +
                                "}")
                        .put("/dynamic-service/services/parameters-service/resource/v1/update_resource")
                        .then()
                        .log()
                        .all()
                        .statusCode(200)
                        .extract()
                        .path("data")
                        .toString();


    }


    @Order(7)
    @Test
    public void deleteResourceTest(){

        given()
                .log()
                .all()
                .header(appQa,applicationQa)
                .header("Authorization", this.token)
                .header("tenant","INTEREDES")
                .delete("/dynamic-service/services/parameters-service/resource/v1/delete_resource/"+IdResourceCreated)
                .then()
                .log()
                .all()
                .statusCode(204)
                .extract()
                .body()
                .asString();


    }

    @Order(8)
    @Test
    public void deleteResourceTypeTest(){
        given()
                .log()
                .all()
                .header(appQa,applicationQa)
                .header("Authorization", this.token)
                .header("tenant","INTEREDES")
                .delete("/dynamic-service/services/parameters-service/resourcetype/v1/delete_resource_type/"+IdResourceTypeCreated)
                .then()
                .log()
                .all()
                .statusCode(204)
                .extract()
                .body()
                .asString();

    }




}
