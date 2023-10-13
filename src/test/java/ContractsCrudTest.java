import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ContractsCrudTest {

    private String token;
    private String appQa = "application";
    private String applicationQa= "siie.qa.interedes.com.co";
    private String IdContract;


    @BeforeEach
    public void setUp(){
        var tkn = new TokenGenerator();
        token = tkn.getToken();
    }

    @Order(1)
    @Test
    public void getAllContrats(){
        given()
                .log()
                .all()
                .header(appQa,applicationQa)
                .header("Authorization", this.token)
                .header("tenant","INTEREDES")
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"filters\": [],\n" +
                        "    \"sorts\": [\n" +
                        "        {\n" +
                        "            \"key\":\"id\",\n" +
                        "            \"direction\":\"ASC\"\n" +
                        "        }\n" +
                        "    ],\n" +
                        "    \"page\": null,\n" +
                        "    \"size\": null\n" +
                        "}")
                .post("/dynamic-service/services/parameters-service/contract/v1/get_contracts_criteria")
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
    public void creteContract(){
        IdContract =
        given()
                .log()
                .all()
                .header(appQa,applicationQa)
                .header("Authorization", this.token)
                .header("tenant","INTEREDES")
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "        \"numContract\": \"CQA3425\",\n" +
                        "        \"description\": \"Contrato de Prueba para QA\",\n" +
                        "        \"startDate\": \"2023-09-01T00:00:00Z\",\n" +
                        "        \"finalDate\": \"2023-10-01T00:00:00Z\",\n" +
                        "        \"omaContractTypeId\": 5,\n" +
                        "        \"companyId\": 2\n" +
                        "    }")
                .post("/dynamic-service/services/parameters-service/contract/v1/create")
                .then()
                .log()
                .all()
                .statusCode(201)
                .extract()
                .path("data.id")
                .toString();

        //System.out.println(IdContract);

    }

    @Order(3)
    @Test
    public void updateContract(){

        given()
                .log()
                .all()
                .header(appQa,applicationQa)
                .header("Authorization", this.token)
                .header("tenant","INTEREDES")
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"id\": "+ IdContract +",\n" +
                        "    \"numContract\": \"CQA3425\",\n" +
                        "    \"description\": \"Contrato de Prueba para QA/QC 2023\",\n" +
                        "    \"startDate\": \"2023-09-01T00:00:00Z\",\n" +
                        "    \"finalDate\": \"2023-10-01T00:00:00Z\"\n" +
                        "}")
                .put("/dynamic-service/services/parameters-service/contract/v1/update")
                .then()
                .log()
                .all()
                .statusCode(200);


    }

    @Order(4)
    @Test
    public void deleteContract(){

        given()
                .log()
                .all()
                .header(appQa,applicationQa)
                .header("Authorization", this.token)
                .header("tenant","INTEREDES")
                .delete("/dynamic-service/services/parameters-service/contract/v1/delete/"+ IdContract )
                .then()
                .log()
                .all();
    }

    @Order(2)
    @Test
    public void gatAllDocumentsType(){

    }

}
