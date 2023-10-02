import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.*;

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

        List<Integer> arreglo1 = new JsonPath(iDBackUps).getList("");
        System.out.println(arreglo1);

        Random rand = new Random();
        int size = arreglo1.size();
        if (size > 0) {
            int bound = size - 1;

            int index = bound > 0 ? rand.nextInt(bound) : 0;
            BackUpCrudTest.numRandom = arreglo1.get(index);

            System.out.println(numRandom);
        }
    }


    @Order(2)
    @Test
    public void downloadBackupFile(){

        given()
                .log()
                .all()
                .header("Authorization",token)
                .header(appQa, applicationQa)
                .header("TENANT", "INTEREDES")
                .get("/dynamic-service/services/operacion-campo-service/backup/download-backup/"+BackUpCrudTest.numRandom)
                .then()
                .log()
                .all();

    }

    @Order(3)
    @Test
    public void deleteBackUp(){
        given()
                .log()
                .all()
                .header("Authorization",token)
                .header(appQa, applicationQa)
                .header("TENANT", "INTEREDES")
                .delete("/dynamic-service/services/operacion-campo-service/backup/delete-backup/"+BackUpCrudTest.numRandom)
                .then()
                .log()
                .all()
                .statusCode(200);
    }
}
