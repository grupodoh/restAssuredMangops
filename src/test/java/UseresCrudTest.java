import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.PrimitiveIterator;

import static io.restassured.RestAssured.given;

public class UseresCrudTest {

    private String token;
    private String appQa = "application";
    private String application = "siie.qa.interedes.com.co";

    @BeforeEach
    public void setUp(){
        var tkn = new TokenGenerator();
        token = tkn.getToken();
    }
    @Order(1)
    @Test
    public  void getAllUsers(){

        var users = given()
                .log()
                .all()
                .header(appQa,application)
                .header("Authorization",token)
                .header("tenant","INTEREDES")
                .get("/dynamic-service/services/user-service/employee/v1/get_employee_data")
                .then()
                .log()
                .all()
                .extract()
                .body()
                .toString();

    }
}
