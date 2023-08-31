import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class HeadquartersCrudTest {


    private String token;
    private String applicationQa = "siie.qa.interedes.com.co";
    private String appQa = "application";

    @BeforeEach
    public void setUp(){
        var tkn = new TokenGenerator();
        this.token = tkn.getToken();
    }


    @Order(1)
    @Test
    public void GetAllHeadquarters(){

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





}
