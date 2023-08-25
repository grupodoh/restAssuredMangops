import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Objects;

import static io.restassured.RestAssured.given;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ContractorCompaniesCrudTest {

    private String token;
    private String appQa = "application";
    private String applicationQa= "siie.qa.interedes.com.co";
    private  String cantCompanies;
    private  String idCreatedCompany;



    @Order(1)
    @BeforeEach
    public void getTokenTest(){

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
    public void getAllCompanies(){

        cantCompanies = given()
                .log()
                .all()
                .header(appQa,applicationQa)
                .header("Authorization", token)
                .header("tenant","INTEREDES")
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"filters\": [\n" +
                        "        {\n" +
                        "            \"key\": \"name\",\n" +
                        "            \"operator\": \"LIKE\",\n" +
                        "            \"field_type\": \"STRING\",\n" +
                        "            \"value\": \"INFRA\"\n" +
                        "        }\n" +
                        "    ],\n" +
                        "    \"sorts\": [],\n" +
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

        JsonPath arregloCompanies = new JsonPath(cantCompanies);
        List<Objects> CompaniesArray = arregloCompanies.getList("data");
        int CantCompanies = CompaniesArray.size();

        System.out.println("La cantidad de empresas registradas son: " + CantCompanies);




    }


}
