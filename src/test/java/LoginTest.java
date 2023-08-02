import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LoginTest {


    @Test
    public void logintest(){

        String response  = RestAssured
                .given()
                .header("application","siie.dev.interedes.com.co")
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"username\": 7416,\n" +
                        "    \"password\": 7416\n" +
                        "}")
                .post("https://siie.dev.interedes.com.co/services/dynamic-service/auth/login")
                .then()
                .statusCode(200)
                .extract().path("data.accessToken").toString();

        System.out.println(response);
    }
}
