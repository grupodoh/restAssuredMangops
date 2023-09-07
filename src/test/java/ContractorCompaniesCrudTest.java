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
    private String updatedContractorCompany;
    private String lastPositionCompany;
    private String companyName;

    @BeforeEach
    public void setUp(){
        var tkn = new TokenGenerator();
        this.token = tkn.getToken();
    }

    @Order(1)
    @Test
    public void getAllCompanies(){

        cantCompanies = given()
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
                        "    \"sorts\": [" +
                        "                   {\n" +
                                "               \"key\":\"id\",\n" +
                                "               \"direction\": \"ASC\"\n" +
                                "           }\n" +
                        "               ],\n" +
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

    @Order(2)
    @Test
    public void createContractorCompanyTest(){

        idCreatedCompany = given()
                .log()
                .all()
                .header(appQa,applicationQa)
                .header("Authorization",this.token)
                .header("tenant","INTEREDES")
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"name\":\"Dillancol\",\n" +
                        "    \"identificationTypeId\": 1,\n" +
                        "    \"idNumber\": 7654243,\n" +
                        "    \"checkDigit\": 6,\n" +
                        "    \"address\": \"Cra. 7 #2-63, Neiva, Huila\",\n" +
                        "    \"cityId\": 1,\n" +
                        "    \"phone\": \"88707448\",\n" +
                        "    \"email\": \"dillancolcomercial@gmail.com\",\n" +
                        "    \"personTypeId\": 1,\n" +
                        "    \"status\": 1,\n" +
                        "    \"main\": 1\n" +
                        "}")
                .post("/dynamic-service/services/parameters-service/company/v1/create")
                .then()
                .log()
                .all()
                .statusCode(200)
                .extract()
                .path("data.id")
                .toString();

        System.out.println(idCreatedCompany);

    }

    @Order(3)
    @Test
    public void updateContractorCompany(){
        updatedContractorCompany =  given()
                .log()
                .all()
                .header(appQa,applicationQa)
                .header("Authorization",this.token)
                .header("tenant","INTEREDES")
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"id\":"+idCreatedCompany+", \n" +
                        "    \"name\":\"Dillancol S.A.S\",\n" +
                        "    \"identificationTypeId\":1, \n" +
                        "    \"indentificationTypeDTO\":null, \n" +
                        "    \"idNumber\":7654253, \n" +
                        "    \"checkDigit\":6, \n" +
                        "    \"address\":\"Cra. 7 #2-68\", \n" +
                        "    \"latitude\":0, \n" +
                        "    \"longitude\":0, \n" +
                        "    \"branchTypeId\":1, \n" +
                        "    \"cityId\":1, \n" +
                        "    \"cityDTO\":null, \n" +
                        "    \"phone\":88707448, \n" +
                        "    \"email\":\"dillancolcomercial@gmail.com\", \n" +
                        "    \"contactName\":null, \n" +
                        "    \"personTypeId\":1, \n" +
                        "    \"personTypeDTO\":null, \n" +
                        "    \"status\":0, \n" +
                        "    \"main\":1, \n" +
                        "    \"companyId\":null, \n" +
                        "    \"companyName\":null\n" +
                        "    }")
                .put("/dynamic-service/services/parameters-service/company/v1/update")
                .then()
                .log()
                .all()
                .extract()
                .body()
                .asString();

        System.out.println(updatedContractorCompany);
    }

    @Order(4)
    @Test
    public void validateContractorCompanieName(){
        lastPositionCompany =
        given()
                .log()
                .all()
                .header(appQa,applicationQa)
                .header("Authorization",this.token)
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
                .extract()
                .body()
                .asString();

        JsonPath arregloPositions = new JsonPath(lastPositionCompany);
        List<Objects> PositionsArray = arregloPositions.getList("data");
        int Cant = PositionsArray.size()-1;



        companyName =
        given()
                .log()
                .all()
                .header(appQa,applicationQa)
                .header("Authorization",this.token)
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
                .extract()
                .path("data["+Cant+"].name");

        Assertions.assertEquals("Dillancol S.A.S",companyName);
        System.out.println("La empresa asociada se le actualizo correctamente el nombre por: "+companyName);

    }


    @Order(5)
    @Test
    public void delteContractorCompany(){


        given()
                .log()
                .all()
                .header(appQa,applicationQa)
                .header("Authorization",this.token)
                .header("tenant","INTEREDES")
                .delete("/dynamic-service/services/user-service/company/v1/delete_company/"+idCreatedCompany)
                .then()
                .statusCode(200);

        System.out.println("Se elimino la empresa con ID: "+ idCreatedCompany +" correctamente.");

    }


}
