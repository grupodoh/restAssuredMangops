import io.qameta.allure.*;
import io.restassured.http.ContentType;
import jdk.jfr.Description;
import org.junit.jupiter.api.*;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.DisplayName;
import static io.qameta.allure.util.ResultsUtils.*;

import static io.qameta.allure.SeverityLevel.*;
import static io.qameta.allure.util.ResultsUtils.PARENT_SUITE_LABEL_NAME;
import static io.restassured.RestAssured.given;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RolesCrudTest {

    private String token;
    private String appQa = "application";
    private String applicationQa= "siie.qa.interedes.com.co";

    private Integer idRolCreated;

    @BeforeEach
    public void setUp(){
        var tkn = new TokenGenerator();
        token = tkn.getToken();
    }

    @Order(1)
    @Test
    public void getAllRoles(){

        given()
                .log()
                .all()
                .header(appQa,applicationQa)
                .header("Authorization", token)
                .header("tenant","INTEREDES")
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"filters\": [],\n" +
                        "    \"sorts\": [],\n" +
                        "    \"page\": null,\n" +
                        "    \"size\": null\n" +
                        "}")
                .post("/dynamic-service/services/user-service/rol/v1/get_roles_criteria")
                .then()
                .log()
                .all()
                .statusCode(200)
                .extract()
                .body();

    }

    @Order(2)
    @Test
    void createRol(){

        idRolCreated = given()
                .log()
                .all()
                .header(appQa,applicationQa)
                .header("Authorization", token)
                .header("tenant","INTEREDES")
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"name\": \"Rol De Prueba Automatizadcion QA\",\n" +
                        "    \"code\": \"RPAQA\",\n" +
                        "    \"status\": 1,\n" +
                        "    \"components\":[2, 70, 3, 4, 5, 6] \n" +
                        "}")
                .post("/dynamic-service/services/user-service/rol/v1/create_rol")
                .then()
                .log()
                .all()
                .extract()
                .body()
                .path("data.id");

        System.out.println(idRolCreated);
    }

    @Order(3)
    @Test
    public void rolById(){

        given()
                .log()
                .all()
                .header(appQa,applicationQa)
                .header("Authorization", token)
                .header("tenant","INTEREDES")
                .get("/dynamic-service/services/user-service/rol/v1/get_rol/"+idRolCreated)
                .then()
                .log()
                .all()
                .extract()
                .body();


    }

    @Order(4)
    @Test
    public void updateRol(){



        var idUpdated = given()
                .log()
                .all()
                .header(appQa,applicationQa)
                .header("Authorization", token)
                .header("tenant","INTEREDES")
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"id\": "+idRolCreated+",\n" +
                        "    \"name\": \"Rol De Prueba Automatizadcion QA Changed\",\n" +
                        "    \"code\": \"RPAQA\",\n" +
                        "    \"status\": true,\n" +
                        "    \"components\": []\n" +
                        "}")
                .put("/dynamic-service/services/user-service/rol/v1/update_rol")
                .then()
                .log()
                .all()
                .extract()
                .body()
                .path("data.id");

        Assertions.assertEquals(idRolCreated,idUpdated);


    }


    @Order(5)
    @Test
    public void deleteComponentePorRol(){
        given()
                .log()
                .all()
                .header(appQa,applicationQa)
                .header("Authorization", token)
                .header("tenant","INTEREDES")
                .delete("/dynamic-service/services/security-service/role/v1/delete_component_by_role/"+idRolCreated)
                .then()
                .log()
                .all()
                .extract()
                .body();
    }


    @Order(6)
    @Test
    public void deleteRolPorId(){
        given()
                .log()
                .all()
                .header(appQa,applicationQa)
                .header("Authorization", token)
                .header("tenant","INTEREDES")
                .delete("/dynamic-service/services/user-service/rol/v1/delete_rol/"+idRolCreated)
                .then()
                .log()
                .all()
                .extract()
                .body();
    }

}
