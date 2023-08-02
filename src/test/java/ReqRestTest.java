import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class ReqRestTest {
    private  String nameUpdated1;
    private String baseURL = "https://reqres.in/api/";

    @Before
    public void setup(){
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Test
    public void loginTest(){
         given()
                .log()
                .all()
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"email\": \"eve.holt@reqres.in\",\n" +
                        "    \"password\": \"cityslicka\"\n" +
                        "}")
                .post(baseURL+"login")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("token",notNullValue());

        //System.out.println(response);
    }


    @Test
    public void getSingleUserTest(){
        given()
                .log()
                .all()
                .contentType(ContentType.JSON)
                .get(baseURL+"users/2")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("data.id", equalTo(2));

        //System.out.println(response);
    }

    @Test
    public void deleteUserTest(){
       String delete = given()
                .log()
                .all()
                .delete(baseURL+"users/2")
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT).toString();

        System.out.println("se eliminó" + delete);
    }

    @Test
    public void patchUserTest(){
        nameUpdated1 = given()
               .log()
               .all()
                .when()
                .body("{\n" +
                        "    \"name\": \"morpheus\",\n" +
                        "    \"job\": \"zion resident\"\n" +
                        "}")
                .patch(baseURL+"users/2")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .path("").toString();

       System.out.println(nameUpdated1);

       //assertThat(nameUpdated1, equalTo("Neo"));


    }

    @Test
    public void putUserTest(){
        String nameUpdated = given()
                .log()
                .all()
                .when()
                .body("{\n" +
                        "    \"name\": \"Neo\",\n" +
                        "    \"job\": \"zion resident No. 100\"\n" +
                        "}")
                .put(baseURL+"users/2")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .path("").toString();

        System.out.println(nameUpdated);

        //assertThat(nameUpdated, equalTo("zion resident"));


    }


}