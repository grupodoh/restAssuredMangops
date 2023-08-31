import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Objects;

import static io.restassured.RestAssured.given;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PositionsCrudTest {

    private String token;
    private String appQa = "application";
    private String IdPosition;
    private String IdDelete;
    private String tenant= "INTEREDES";
    private String applicationQa= "siie.qa.interedes.com.co";

    @BeforeEach
    public void setUp(){
        var tkn = new TokenGenerator();
        this.token = tkn.getToken();
    }

    @Order(1)
    @Test()
    public void getAllPositionsTest(){
        given()
                .header(appQa, applicationQa)
                .header("Authorization", token)
                .header("tenant",tenant)
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"filters\": [\n" +
                        "     \n" +
                        "    ],\n" +
                        "    \"sorts\": [\n" +
                        "        {\n" +
                        "         \"key\":\"id\" ,\n" +
                        "         \"direction\": \"ASC\"\n" +
                        "      }\n" +
                        "\n" +
                        "\n" +
                        "    ],\n" +
                        "    \"page\": null,\n" +
                        "    \"size\": null\n" +
                        "}")
                .post("/dynamic-service/services/user-service/job/v1/get_jobs_criteria")
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
    public void createPositionTest(){

        IdPosition =    given()
                 .log()
                 .all()
                .header(appQa,applicationQa)
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"title\": \"SCRUM MASTER \",\n" +
                        "    \"status\": 1\n" +
                        "}")
                .post("/user-service/job/v1/create_job")
                .then()
                 .log()
                 .all()
                .statusCode(200)
                .extract()
                .path("data.id")
                .toString();

        System.out.println(IdPosition);

    }

    @Order(3)
    @Test
    public void updatePositionTest(){

                 given()
                .header(appQa,applicationQa)
                .header("Authorization",token)
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "     \"id\":"+ IdPosition + " ,\n" +
                        "        \"title\": \"SCRUM MASTER SENIOR\",\n" +
                        "        \"status\": 1\n" +
                        "}")
                .post("/user-service/job/v1/update_job")
                .then()
                .extract()
                .toString();

    }



    @Order(4)
    @Test
    public void positionAssignedToACompany(){
        given()
                .header(appQa, applicationQa)
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"companyId\": 2,\n" +
                        "    \"jobId\":"+IdPosition+",\n" +
                        "    \"status\": 1\n" +
                        "}")
                .post("/dynamic-service/services/user-service/company/v1/company_job")
                .then()
                .log()
                .all()
                .statusCode(200)
                .extract()
                .path("data.id");
    }


    @Order(5)
    @Test
    public void deletePositionAssignedToACompany(){
        given()
                .header(appQa, applicationQa)
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"companyId\": 2,\n" +
                        "    \"jobId\":"+IdPosition+",\n" +
                        "    \"status\": 1\n" +
                        "}")
                .put("/dynamic-service/services/user-service/company/v1/remove_company_job")
                .then()
                .log()
                .all()
                .statusCode(200)
                .extract()
                .body()
                .toString();
    }


    @Order(6)
    @Test
    public void deletePositionTest(){
        IdPosition =
                given()
                .header(appQa, applicationQa)
                .header("Authorization", token)
                .header("tenant",tenant)
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"filters\": [\n" +
                        "     \n" +
                        "    ],\n" +
                        "    \"sorts\": [\n" +
                        "        {\n" +
                        "         \"key\":\"id\" ,\n" +
                        "         \"direction\": \"ASC\"\n" +
                        "      }\n" +
                        "\n" +
                        "\n" +
                        "    ],\n" +
                        "    \"page\": null,\n" +
                        "    \"size\": null\n" +
                        "}")
                .post("/dynamic-service/services/user-service/job/v1/get_jobs_criteria")
                .then()
                .log()
                .all()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        JsonPath arregloPositions = new JsonPath(IdPosition);
        List<Objects> PositionsArray = arregloPositions.getList("data");
        int Cant = PositionsArray.size()-1;



        IdDelete =
                given()
                .header(appQa, applicationQa)
                .header("Authorization", token)
                .header("tenant",tenant)
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"filters\": [\n" +
                        "     \n" +
                        "    ],\n" +
                        "    \"sorts\": [\n" +
                        "        {\n" +
                        "         \"key\":\"id\" ,\n" +
                        "         \"direction\": \"ASC\"\n" +
                        "      }\n" +
                        "\n" +
                        "\n" +
                        "    ],\n" +
                        "    \"page\": null,\n" +
                        "    \"size\": null\n" +
                        "}")
                .post("/dynamic-service/services/user-service/job/v1/get_jobs_criteria")
                .then()
                .log()
                .all()
                .statusCode(200)
                .extract()
                .path("data.id["+Cant+"]")
                .toString();



        given()
                .header(appQa,applicationQa)
                .contentType(ContentType.JSON)
                .header("Authorization", token )
                .log()
                .all()
                .delete("/user-service/job/v1/delete_job/"+IdDelete)
                .then()
                .log()
                .all()
                .toString();


    }
}
