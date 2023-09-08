import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HeadquartersCrudTest {


    private String token;
    private String applicationQa = "siie.qa.interedes.com.co";
    private String appQa = "application";
    private String  IdHeadquarter;

    @BeforeEach
    public void setUp(){
        var tkn = new TokenGenerator();
        this.token = tkn.getToken();
    }


    @Order(1)
    @Test
    public void getAllHeadquarters(){

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
                                "            \"key\": \"name\",\n" +
                                "            \"operator\": \"LIKE\",\n" +
                                "            \"field_type\": \"STRING\",\n" +
                                "            \"value\": \"\"\n" +
                                "        }\n" +
                                "    ],\n" +
                                "    \"sorts\": [\n" +
                                "        {\n" +
                                "            \"key\":\"id\",\n" +
                                "            \"direction\": \"ASC\"\n" +
                                "        }\n" +
                                "    ],\n" +
                                "    \"page\": null,\n" +
                                "    \"size\": null\n" +
                                "}")
                        .post("/dynamic-service/services/parameters-service/company/v1/get_companies_criteria")
                        .then()
                        .log()
                        .all()
                        .statusCode(200)
                        .extract()
                        .body()
                        .asString();

    }

    @Order(2)
    @Test
    public void branchTypeHeadquarter(){
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
                        "            \"key\": \"status\",\n" +
                        "            \"operator\": \"EQUAL\",\n" +
                        "            \"field_type\": \"INTEGER\",\n" +
                        "            \"value\": 1\n" +
                        "        }\n" +
                        "    ],\n" +
                        "    \"sorts\": [\n" +
                        "        {\n" +
                        "            \"key\":\"id\",\n" +
                        "            \"direction\":\"ASC\"\n" +
                        "        }\n" +
                        "\n" +
                        "    \n" +
                        "    ],\n" +
                        "    \"page\": null,\n" +
                        "    \"size\": null\n" +
                        "}")
                .post("/dynamic-service/services/parameters-service/company/v1/get_branch_type_criteria")
                .then()
                .log()
                .all()
                .extract()
                .body()
                .asString();


    }

    @Order(3)
    @Test
    public void getCitiesHeadquarter(){
        given()
                .log()
                .all()
                .header(appQa,applicationQa)
                .header("Authorization", this.token)
                .header("tenant","INTEREDES")
                .contentType(ContentType.JSON)
                .get("/dynamic-service/services/user-service/city/v1/get_city?size=2000")
                .then()
                .log()
                .all()
                .statusCode(200)
                .extract()
                .body()
                .asString();


    }


    @Order(4)
    @Test
    public void createAHeadquarterTest(){

        IdHeadquarter =
                given()
                .log()
                .all()
                .header(appQa,applicationQa)
                .header("Authorization", this.token)
                .header("tenant","INTEREDES")
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"name\": \"Agencia Comercial\",\n" +
                        "    \"identificationTypeId\": null,\n" +
                        "    \"indentificationTypeDTO\": null,\n" +
                        "    \"idNumber\": 900340866,\n" +
                        "    \"checkDigit\": 4,\n" +
                        "    \"address\": \"Parque Industrial Lote 11\",\n" +
                        "    \"latitude\": 2.9281528193026842,\n" +
                        "    \"longitude\": -75.31640244006543,\n" +
                        "    \"branchTypeId\": 1,\n" +
                        "    \"cityId\": 626,\n" +
                        "    \"cityDTO\": null,\n" +
                        "    \"phone\": \"3157712205\",\n" +
                        "    \"email\": \"oscar.calvache@interedes.com.co\",\n" +
                        "    \"contactName\": \"Oscar Mauricio Calvache\",\n" +
                        "    \"personTypeId\": null,\n" +
                        "    \"personTypeDTO\": null,\n" +
                        "    \"status\": 1,\n" +
                        "    \"main\": 0,\n" +
                        "    \"companyId\": 2,\n" +
                        "    \"companyName\": null\n" +
                        "}")
                .post("/dynamic-service/services/parameters-service/company/v1/create")
                .then()
                .log()
                .all()
                .statusCode(200)
                .extract()
                .path("data.id")
                .toString();

        System.out.println(IdHeadquarter);

        System.out.println("El id dela sede creada es: " + IdHeadquarter);


    }

    @Order(5)
    @Test
    void updateHeadquarter(){
        given()
                .log()
                .all()
                .header(appQa,applicationQa)
                .header("Authorization", this.token)
                .header("tenant","INTEREDES")
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"id\":"+ IdHeadquarter +" ,\n" +
                        "    \"name\": \"Agencia Comercial 2\",\n" +
                        "    \"companyId\": 2,\n" +
                        "    \"identificationTypeId\": null,\n" +
                        "    \"idNumber\": 900340866,\n" +
                        "    \"checkDigit\": 4,\n" +
                        "    \"address\": \"Parque Industrial Lote 12\",\n" +
                        "    \"latitude\": 2.92815282,\n" +
                        "    \"longitude\": -75.31640244,\n" +
                        "    \"branchTypeId\": 1,\n" +
                        "    \"cityId\": 626,\n" +
                        "    \"phone\": \"3157712205\",\n" +
                        "    \"email\": \"oscar.calvache@interedes.com.co\",\n" +
                        "    \"contactName\": \"Oscar Mauricio Calvache\",\n" +
                        "    \"personTypeId\": null,\n" +
                        "    \"status\": 1,\n" +
                        "    \"main\": 0\n" +
                        "}")
                .put("/dynamic-service/services/parameters-service/company/v1/update")
                .then()
                .log()
                .all()
                .extract()
                .body()
                .asString();



    }

    @Order(6)
    @Test
    public void deleteHeadquarter(){

        System.out.println(IdHeadquarter);

        given()
                .log()
                .all()
                .header(appQa,applicationQa)
                .header("Authorization", this.token)
                .header("tenant","INTEREDES")
                .contentType(ContentType.JSON)
                .delete("/dynamic-service/services/user-service/company/v1/delete_company/"+IdHeadquarter)
                .then()
                .log()
                .all();

    }








}
