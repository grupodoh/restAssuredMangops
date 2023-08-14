import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import io.restassured.response.*;

import static io.restassured.RestAssured.given;

public class PositionsCrudTest {

    private String token;
    private String appQa = "application";
    private Integer Positions;
    private String applicationQa= "siie.qa.interedes.com.co";
    @BeforeEach
    public void positionsCrud(){

        RestAssured.baseURI= "https://siie.qa.interedes.com.co/services";

        token = given()
                .header(appQa,applicationQa)
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"username\": \"3332\",\n" +
                        "    \"password\": 3332\n" +
                        "}")
                .post("/dynamic-service/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .path("data.accessToken")
                .toString();

        //System.out.println(token);
    }

@Test
    public void getAllPositions(){
        given()
                .header(appQa, applicationQa)
                .header("Authorization", token)
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
                .statusCode(200)
                .extract()
                .body()
                .asString();
            }

            @Test
}
