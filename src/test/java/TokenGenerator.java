import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;

public class TokenGenerator {

    private String applicationQa = "siie.qa.interedes.com.co";
    private String token;
    public String getToken() {

        RestAssured.baseURI = "https://siie.qa.interedes.com.co/services";

        /***
         * 1. Esta es la prueba de la creación del token a la hora de
         * acceder al loggin.
         */

        token = given()
                .log()
                .all()
                .header("application", applicationQa)
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

        System.out.println("Este es el token que se generó:  \n" + token + "\n \n");
return token;
    }
}
