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
    private ArrayList<Integer> Roles;
    private  List<Integer> rolIDList =  new ArrayList<>();
    private  List<String> codeEmployeeList = new ArrayList<>();
    private ArrayList<String> EmployeeCode;

    private  String teamIdCreated;








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
                .path("data.id");

        System.out.println(Roles);

        for (int i = 0; i < 2; i++){

            Random random = new Random();
            int indiceAleatoreo = random.nextInt(Roles.toArray().length);
            rolIDList.add(Roles.get(indiceAleatoreo));

            System.out.println(rolIDList);

        }

    }


    @Order(3)
    @Test
    public void searchEmployee(){

        EmployeeCode = given()
                            .log()
                            .all()
                            .header(appQa,applicationQa)
                            .header("Authorization",token)
                            .header("tenant","INTEREDES")
                            .contentType(ContentType.JSON)
                            .body("{\n" +
                                    "    \"filters\": [\n" +
                                    "        {\n" +
                                    "            \"key\": \"name\",\n" +
                                    "            \"operator\": \"LIKE\",\n" +
                                    "            \"field_type\": \"STRING\",\n" +
                                    "            \"value\": \"\"\n" +
                                    "        },\n" +
                                    "        {\n" +
                                    "            \"key\": \"status\",\n" +
                                    "            \"operator\": \"EQUAL\",\n" +
                                    "            \"field_type\": \"INTEGER\",\n" +
                                    "            \"value\": 1\n" +
                                    "        }\n" +
                                    "    ],\n" +
                                    "    \"sorts\": [\n" +
                                    "        {\n" +
                                    "            \"key\":\"id\",\n" +
                                    "            \"direction\":\"ASC\"\n" +
                                    "        }\n" +
                                    "    ],\n" +
                                    "    \"page\": null,\n" +
                                    "    \"size\": null\n" +
                                    "}")
                            .post("/dynamic-service/services/user-service/employee/v1/search")
                            .then()
                            .log()
                            .all()
                            .extract()
                            .body()
                            .path("data.content.code");

                            System.out.println(EmployeeCode);

                            for (int i = 0; i < 3; i++) {
                                Random randomEmp = new Random();
                                int indeiceAleatoreo = randomEmp.nextInt(EmployeeCode.toArray().length);
                                codeEmployeeList.add(EmployeeCode.get(indeiceAleatoreo).toString());

                                System.out.println(codeEmployeeList);

                            }

    }


    @Order(4)
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


    @Order(5)
    @Test
    public void CreateTeam(){


        given()
                .log()
                .all()
                .header(appQa,applicationQa)
                .header("Authorization",token)
                .header("tenant","INTEREDES")
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "  \"name\": \"QA Automatizadores Estandar\",\n" +
                        "  \"status\": true,\n" +
                        "  \"userList\":" + codeEmployeeList + ",\n" +
                        "  \"roleList\": " + rolIDList + "\n" +
                        "}")
                .post("/dynamic-service/services/security-service/teams/v1/create_team")
                .then()
                .log()
                .all()
                .extract()
                .body()
                .path("data.id");

        System.out.println();
    }

    @Order(6)
    @Test
    public void UpdateTeam(){

        given()
                .log()
                .all()
                .header(appQa,applicationQa)
                .header("Authorization",token)
                .header("tenant","INTEREDES")
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"id\": "+teamIdCreated+",\n" +
                        "    \"name\": \"QA Automatizadores Estandar\",\n" +
                        "    \"status\": true,\n" +
                        "    \"userList\": [ ],\n" +
                        "    \"roleList\": [ ]")
                .put("/dynamic-service/services/security-service/teams/v1/update_team")
                .then()
                .log()
                .all();

    }

    /*@Order(7)
    @Test
    public void DeleteTeam(){

        given()
                .log()
                .all()
                .header(appQa,applicationQa)
                .header("Authorization",token)
                .header("tenant","INTEREDES")
                .delete("/dynamic-service/services/security-service/teams/v1/delete_team/ " )
                .then()
                .log()
                .all();

    }*/




}
