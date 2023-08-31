import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class HeadquartersCrudTest {


    private String token;
    private String applicationQa = "siie.qa.interedes.com.co";
    private String appQa = "application";
    private String IdHeadquarter;

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
    public void CreateAHeadquarterTest(){

        IdHeadquarter =
                given()
                .log()
                .all()
                .header(appQa,applicationQa)
                .header("Authorization", token)
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

        System.out.println("El id dela sede creada es: " + IdHeadquarter);


    }





}