import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.mockito.internal.matchers.Any;

import java.util.*;

import static io.restassured.RestAssured.given;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TeamsCrudTest {

    private String token;
    private String appQa = "application";
    private String applicationQa= "siie.qa.interedes.com.co";
    private String Roles;
    @BeforeEach
    public void setUp(){
        var tkn = new TokenGenerator();
        token = tkn.getToken();
    }


    @Order(1)
    @Test
    public void GetAllTeams(){
            given()
                    .log()
                    .all()
                    .header(appQa,applicationQa)
                    .header("Authorization",token)
                    .header("tenant","INTEREDES")
                    .contentType(ContentType.JSON)
                    .body("{\n" +
                            "    \"filters\": [],\n" +
                            "    \"sorts\": [],\n" +
                            "    \"page\": null,\n" +
                            "    \"size\": null\n" +
                            "}")
                    .post("/dynamic-service/services/security-service/teams/v1/list_teams_criteria")
                    .then()
                    .log()
                    .all()
                    .extract()
                    .body();

    }

    @Order(2)
    @Test
    public void listRoles(){

        Roles = given()
                .log()
                .all()
                .header(appQa,applicationQa)
                .header("Authorization",token)
                .header("tenant","INTEREDES")
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"filters\": [],\n" +
                        "    \"sorts\": [{\n" +
                        "            \"key\":\"id\",\n" +
                        "            \"direction\":\"ASC\"\n" +
                        "        }],\n" +
                        "    \"page\": null,\n" +
                        "    \"size\": null\n" +
                        "}")
                .post("/dynamic-service/services/user-service/rol/v1/get_roles_criteria")
                .then()
                .extract()
                .body()
                .path("data.id")
                .toString();

        System.out.println(Roles);




        /*Random rand = new Random();
        int indiceRandon = rand.nextInt(Roles.length());
        int idRandon = Roles[indiceRandon]*/


    }

    @Order(3)
    @Test
    public void listUsers(){

    }


    @Order(4)
    @Test
    public void searchEmployee(){

    }


    @Order(5)
    @Test
    public void getRolByTeamsId(){

        given()
                .log()
                .all()
                .header(appQa,applicationQa)
                .header("Authorization",token)
                .header("tenant","INTEREDES")
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"filters\": [\n" +
                        "        \n" +
                        "    ],\n" +
                        "    \"sorts\": [],\n" +
                        "    \"page\": null,\n" +
                        "    \"size\": null\n" +
                        "}")
                .post("/security-service/teams/v1/get_team_role/1")
                .then()
                .log()
                .all();





    }



}
