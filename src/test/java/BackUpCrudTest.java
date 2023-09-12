import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;

public class BackUpCrudTest {

    private String token;
    private String appQa = "application";
    private String applicationQa= "siie.qa.interedes.com.co";
    private String iDBackUps;
    private String nr;
    private Integer numRandom;



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
        int index = rand.nextInt(arreglo1.size()-1);
        numRandom =arreglo1.get(index);

         nr = numRandom.toString();
        System.out.println(numRandom);
        System.out.println(nr);



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
                .get("/dynamic-service/services/operacion-campo-service/backup/download-backup/"+nr)
                .then()
                .log()
                .all();


    }
}
