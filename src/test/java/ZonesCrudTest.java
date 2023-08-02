import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.Assert.*;

import java.util.List;
import java.util.Objects;

import static io.restassured.RestAssured.given;

import static io.restassured.RestAssured.when;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;


public class ZonesCrudTest {

    /***
     * LEER POR FAVOR!!!     *
     *
     * Paso a paso de lo que se va a realizar en esta prueba:
     *
     * 1. Se generará el token y se almacenará en una variable llamada token.
     * 2. Se validará o se obtendrá la cantidad de zonas creadas antes de crear una nueva zona.
     * 3. Se creara una nueva zona y se obtiene el ID de la zona creada.
     * 4. Se validará la cantidad de zonas despues de la creación de una nueva zona, la cual debe ser
     * mayor en 1 a la respuesta de la prueba 2.
     * 5. Se valida la existencia exacta del nombre de la zona creada.
     * 6. Se realiza la busqueda de la zona por ID para poder ver toda la info del registro.
     * 7. Se realiza la actualización del nombre de la zona.
     * 8. Se valida que la actualización se haya realizado satizfactoriamente.
     * 9. Se elimina la zona.
     * 10. Se valida que la zona no exista.
     */

    private String baseURL = "https://siie.dev.interedes.com.co/services";
    private String token;
    private  String CreatedIdZone;
    private String GetAllZones1;
    private String GetAllZones2;
    private String GetAllZones3;
    private String GetZoneById;
    private String UpdateZone;
    private String responseDeleteZoneById;
    private String validationDeleteZone;


    @Test
    public void CrudZone(){

        /***
         * 1. Esta es la prueba de la creación del token a la hora de
         * acceder al loggin.
         */

        token  = RestAssured
                .given()
                .log()
                .all()
                .header("application","siie.dev.interedes.com.co")
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"username\": \"3332\",\n" +
                        "    \"password\": 3332\n" +
                        "}")
                .post(baseURL+"/dynamic-service/auth/login")
                .then()
                .log()
                .all()
                .statusCode(200)
                .extract()
                .path("data.accessToken")
                .toString();

        //System.out.println("Este es el token que se generó:  "+token+"\n \n");

        /***
         * 2. Esta es la prueba de consultar todas las zonas creadas antes
         * de  crear la nueva zona
         */

        GetAllZones1 = RestAssured.given()
                .log()
                .all()
                .contentType(ContentType.TEXT)
                .contentType(ContentType.JSON)
                .header("application","siie.dev.interedes.com.co")
                .header("Authorization", token)
                .header("tenant","INTEREDES")
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
                .post(baseURL+"/user-service/zone/v1/get_zones_criteria")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        JsonPath arreglo1 = new JsonPath(GetAllZones1);
        List<Objects> ZonesArray1 = arreglo1.getList("data");
        int CantZones1 = ZonesArray1.size();

        System.out.println("La cantidad de zonas existentes son: \n \n"+CantZones1);


        //System.out.println("se muestran todas las zonas existentes: \n " + GetAllZones1+"\n \n");



        /***
         * 3. Esta es la prueba de la creación de una zona, donde se utiliza el token creado en la prueba anterior
         * y se almacena el ID de la zona creada.
         */

        CreatedIdZone = RestAssured.given()
                .log()
                .all()
                .header("application","siie.dev.interedes.com.co")
                .contentType(ContentType.JSON)
                .header("Authorization", token )
                .header("tenant","INTEREDES")
                .body("{\n" +
                        "    \"description\": \"ZONE NORTH WEST10\",\n" +
                        "    \"status\": 1\n" +
                        "}")
                .post(baseURL+"/dynamic-service/services/user-service/zone/v1/create_zone")
                .then()
                .log()
                .all()
                .statusCode(200)
                .extract()
                .path("data.id")
                .toString();

        System.out.println("Este es el ID de la zona creada:  \n \n"+CreatedIdZone+"\n \n");

        /***
         * 4. Esta es la prueba de consultar todas las zonas creadas despues
         * de haber creada la nueva zona,en la cual se validara el aumento en 1
         */

        GetAllZones2 = RestAssured.given()
                .log()
                .all()
                .contentType(ContentType.TEXT)
                .contentType(ContentType.JSON)
                .header("application","siie.dev.interedes.com.co")
                .header("Authorization", token)
                .header("tenant","INTEREDES")
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
                .post(baseURL+"/user-service/zone/v1/get_zones_criteria")
                .then()
                .log()
                .all()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        JsonPath arreglo2 = new JsonPath(GetAllZones2);
        List<Objects> ZonesArray2 = arreglo2.getList("data");
        int CantZones2 = ZonesArray2.size();

        Assertions.assertEquals(CantZones1+1, CantZones2);

        System.out.println("La cantidad de zonas actual es: "+CantZones2+" zonas\n");


        /***
         * 5. Esta es la prueba de consultar todas las zonas creadas pero se valida
         * que el nombre de la zona se encuentre en los registros existentes.
         */

        /*GetAllZones3 = RestAssured.given()
                .log()
                .all()
                .contentType(ContentType.TEXT)
                .contentType(ContentType.JSON)
                .header("application","siie.dev.interedes.com.co")
                .header("Authorization", token)
                .header("tenant","INTEREDES")
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
                        "    \"size\": 200")
                .post(baseURL+"/user-service/zone/v1/get_zones_criteria")
                .then()
                .log()
                .all()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        JsonPath arreglo3 = new JsonPath(GetAllZones3);
        List<Objects> ZonesArray3 = arreglo3.getList("data.description");*/
        //System.out.println(ZonesArray3);


        //System.out.println("La cantidad de zonas existentes son: "+ CantZones3 +"\n");



        /***
         * 6. Esta es la prueba de consultar la zona creada por su ID, donde se utiliza el token y
         * se muestra toda la información referente a la zona.
         */

        GetZoneById = given()
                .log()
                .all()
                .header("application","siie.dev.interedes.com.co")
                .header("Authorization", token )
                .header("tenant","INTEREDES")
                .contentType(ContentType.JSON)
                .get(baseURL+"/user-service/zone/v1/get_zone/"+ CreatedIdZone)
                .then()
                //.statusCode(200)
                .extract()
                .path("")
                .toString();


        System.out.println("Esta es la información completa del registro de la zona de ID "+ CreatedIdZone +":  \n \n"+GetZoneById+"\n \n");


        /***
         * 7. Esta es la prueba de actualizar la zona, donde se actualiza el nombre o descripción de la zona,
         */
        UpdateZone = given()
                .header("application","siie.dev.interedes.com.co")
                .contentType(ContentType.JSON)
                .header("Authorization", token )
                .log()
                .all()
                .body("{\n" +
                        "    \"id\":"+CreatedIdZone+",\n" +
                        "    \"description\": \"ZONE NORTH WEST11\",\n" +
                        "    \"status\": 1\n" +
                        "}")
                .put(baseURL+"/user-service/zone/v1/update_zone")
                .then()
                .extract()
                .toString();

        //System.out.println("Esta es la información de la zona despues de ser actualizada: \n \n" + UpdateZone+"\n \n");


        /***
         * 8. Esta es la validación de la actualización del registro de la zona
         */
        GetZoneById = RestAssured.given()
                .header("application","siie.dev.interedes.com.co")
                .header("Authorization", token )
                .header("tenant","INTEREDES")
                .contentType(ContentType.JSON)
                .get(baseURL+"/user-service/zone/v1/get_zone/"+ CreatedIdZone)
                .then()
                .statusCode(200)
                .extract()
                .path("")
                .toString();

        System.out.println("Esta es la información de la zona despues de ser z: \n \n"+GetZoneById+"\n \n");

        /***
         * 9. Esta es la eliminación de la zona creada
         */

        responseDeleteZoneById = given()
                .header("application","siie.dev.interedes.com.co")
                .contentType(ContentType.JSON)
                .header("Authorization", token )
                .log()
                .all()
                .delete(baseURL+"/user-service/zone/v1/delete_zone/"+CreatedIdZone)
                .then()
                .statusCode(204)
                .toString();

        System.out.println("Se elimino satisfactoriamente la zona. "+responseDeleteZoneById);


        /**
         * 10. Se valida que la zona se haya eliminado
         */

        validationDeleteZone = RestAssured.given()
                .header("application","siie.dev.interedes.com.co")
                .header("Authorization", token )
                .header("tenant","INTEREDES")
                .contentType(ContentType.JSON)
                .get(baseURL+"/user-service/zone/v1/get_zone/"+ CreatedIdZone)
                .then()
                .statusCode(500)
                .extract()
                .path("")
                .toString();

        System.out.println("Se eliminó correctamente la zona.");

    }
   /*@Test
    public void DeleteZone(){
        responseDeleteZoneById = given()
                .header("application","siie.dev.interedes.com.co")
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .log()
                .all()
                .delete(baseURL+"/user-service/zone/v1/delete_zone/251")
                .then()
                .statusCode(204)
                .toString();

        System.out.println("Se elimino = "+responseDeleteZoneById);

    }

    @Test
    public void GetAllZones(){
        GetAllZones = RestAssured.given()
                .header("application","siie.dev.interedes.com.co")
                .header("Authorization", "eyJhbGciOiJIUzUxMiJ9.eyJlbXBsb3llZU5hbWUiOiJqdWFuX3F1acOxb25lcyIsInJvbGUiOiJBRE1JTiIsInNlcmlhbCI6IlFXRVJUWSIsImVtcGxveWVlRW1haWwiOiJqdWFucXVpbm9uZXNAZ21haWwuY29tIiwiZW1wbG95ZWVJZCI6MTI2MywibG9naW5XZWIiOnRydWUsInNlc3Npb25JZCI6IkxPWElKRXhnRFloNTBOK0VQeHpLY0J0T1hwVzlVQzRtODRvS3FSdEsrNldNZlYxL1NRVC81allZY1NHeVFsOCsiLCJ0ZW5hbnQiOjIsInVzZXJuYW1lIjoiMzMzMiIsInN1YiI6IjMzMzIiLCJpYXQiOjE2OTAyMzcwNjYsImV4cCI6MTY5MDU4MjY2Nn0.lHnAnpCJfWkjmVO47MzgZkZ_pxd27RwLHuzWcAnOWEItOY7In7VwYLRzAumToj91YA6HHmMoSscddy5wIuMHIA")
                .header("tenant","INTEREDES")
                .get(baseURL+"/user-service/zone/v1/get_all_zones")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        System.out.println("se muestra esto :\n " + GetAllZones);
    }*/

}
