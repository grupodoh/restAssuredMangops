import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;

import static io.restassured.RestAssured.given;

public class ContractorCompaniesCrudTest {

    private String token;
    private  String cantCompanies;
    private  String idCreatedCompany;



    @Test
    public void ContractorCompaniesCrud(){
        RestAssured.baseURI ="https://siie.qa.interedes.com.co/services";

        token = given()
                .log()
                .all()
                .header("application","siie.qa.interedes.com.co")
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"username\": \"3332\",\n" +
                        "    \"password\": 3332\n" +
                        "}")
                .post("/dynamic-service/auth/login")
                .then()
                .log()
                .all()
                .statusCode(200)
                .extract()
                .path("data.accessToken")
                .toString();

        System.out.println(token);


        cantCompanies = given()
                .log()
                .all()
                .header("application","siie.qa.interedes.com.co")
                .header("Authorization", token)
                .header("tenant","INTEREDES")
                .post("/user-service/company/v1/get_all_companies")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        JsonPath arregloCompanies = new JsonPath(cantCompanies);
        List<Objects> CompaniesArray = arregloCompanies.getList("data");
        int CantCompanies = CompaniesArray.size();

        System.out.println("La cantidad de empresas registradas son: " + CantCompanies);



        idCreatedCompany = given()
                .log()
                .all()
                .header("application","siie.qa.interedes.com.co",
                        "Authorization",token,
                        "tenant","INTEREDES")
                .body("{\n" +
                        "    \"name\": \"GRUPO EMPRESARIAL DOH - GEDOH 2\",\n" +
                        "    \"identificationTypeId\": 2,\n" +
                        "    \"idNumber\": \"1075900802\",\n" +
                        "    \"checkDigit\": \"1\",\n" +
                        "    \"address\": \"Carrera 5 # 13 - 62\",\n" +
                        "    \"cityId\": 1,\n" +
                        "    \"phone\": \"3157712202\",\n" +
                        "    \"email\": \"gedoh2@gmail.com\",\n" +
                        "    \"personTypeId\": 1,\n" +
                        "    \"status\": 1,\n" +
                        "    \"main\": 1\n" +
                        "}")
                .post("/dynamic-service/services/parameters-service/company/v1/create")
                .then()
                .statusCode(200)
                .extract()
                .path("data.id")
                .toString();

        System.out.println("El ide de la empresa creada es: "+idCreatedCompany);


    }


}
