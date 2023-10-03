import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BackUpCrudTest {

    private String token;
    private String appQa = "application";
    private String applicationQa= "siie.qa.interedes.com.co";
    private String iDBackUps;
    public static Integer  numRandom;
    public static String lastBackcUp;
    public static Integer lastIdBackUp;



    @BeforeEach
    public void setUp(){
        var tkn = new TokenGenerator();
        token = tkn.getToken();
    }

    @Order(1)
    @Test
    public void getAllBackUps(){

        iDBackUps =
                given()
                .log()
                .all()
                .header("Authorization",token)
                .header(appQa, applicationQa)
                .header("TENANT", "INTEREDES")
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"filters\": [],\n" +
                        "    \"page\": 0,\n" +
                        "    \"size\": 200,\n" +
                        "    \"sorts\": []\n" +
                        "}")
                .post("/dynamic-service/services/operacion-campo-service/backup/list-backup")
                .then()
                .log()
                .all()
                .statusCode(200)
                .extract()
                .body()
                .path("data.id")
                .toString();

        System.out.println(iDBackUps);

        /*List<Integer> arregloIdBackUps = new JsonPath(iDBackUps).getList("");
        System.out.println(arregloIdBackUps);

        Random rand = new Random();
        int size = arregloIdBackUps.size();
        if (size > 0) {
            int bound = size - 1;

            int index = bound > 0 ? rand.nextInt(bound) : 0;
            BackUpCrudTest.numRandom = arregloIdBackUps.get(index);

            System.out.println(numRandom);*/
        }



    @Order(2)
    @Test
    public void UploadBackUp(){
        given().log().all()
                .header(appQa,applicationQa)
                .header("Authorization",token)
                .header("TENANT", "INTEREDES")
                .multiPart(new File("src/test/files/copia_seguridad_prueba_QA.zip"))
                .post("/synchronization/file/upload-backup/3332")
                .then()
                .log()
                .all();
    }

    @Order(3)
    @Test
    public void GetLastBackUp(){
        lastBackcUp = given()
                .log()
                .all()
                .header("Authorization",token)
                .header(appQa, applicationQa)
                .header("TENANT", "INTEREDES")
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"filters\": [\n" +
                        "        \n" +
                        "     \n" +
                        "    ],\n" +
                        "    \"sorts\": [\n" +
                        "        {\n" +
                        "        \"key\":\"id\",\n" +
                        "        \"direction\": \"ASC\"\n" +
                        "        }\n" +
                        "    ],\n" +
                        "    \"page\": 0,\n" +
                        "    \"size\": 200\n" +
                        "}")
                .post("/dynamic-service/services/operacion-campo-service/backup/list-backup")
                .then()
                .log()
                .all()
                .statusCode(200)
                .extract()
                .body()
                .path("data.id")
                .toString();

        List<Integer> arreglo = new JsonPath(lastBackcUp).getList("");
        var index = arreglo.size() -1;
        lastIdBackUp = arreglo.get(index);


    }


    @Order(4)
    @Test
    public void downloadBackupFile(){

        given()
                .log()
                .all()
                .header("Authorization",token)
                .header(appQa, applicationQa)
                .header("TENANT", "INTEREDES")
                .get("/dynamic-service/services/operacion-campo-service/backup/download-backup/"+lastIdBackUp)
                .then()
                .log()
                .all()
                .extract().body();

    }



    @Order(5)
    @Test
    public void deleteBackUp(){

        System.out.println(lastIdBackUp);

        given()
                .log()
                .all()
                .header("Authorization",token)
                .header(appQa, applicationQa)
                .header("TENANT", "INTEREDES")
                .delete("/dynamic-service/services/operacion-campo-service/backup/delete-backup/"+lastIdBackUp)
                .then()
                .log()
                .all()
                .statusCode(200);
    }
}
