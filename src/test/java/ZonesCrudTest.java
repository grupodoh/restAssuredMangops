import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Objects;

import static io.restassured.RestAssured.given;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ZonesCrudTest {

    /***
     * LEER POR FAVOR!!!     *
     *
     * Paso a paso de lo que se va a realizar en esta prueba:
     *
     * 1. Se generará el token y se almacenará en una variable llamada token.
     * 2. Se obtendrá la cantidad de zonas existentes antes de crear una nueva zona.
     * 3. Se creará una nueva zona, y se obtendrá el ID de la zona creada.
     * 4. Se validará la cantidad de zonas existentes despues de crear la nueva zona,y esta cantidad
     * debe ser mayor en uno (1) a la respuesta de la prueba dos (2).
     * 5. Se valida la existencia exacta del nombre de la zona creada.
     * 6. Se realiza la busqueda de la zona por ID para poder ver toda la info del registro.
     * 7. Se realiza la actualización del nombre de la zona.
     * 8. Se valida que la actualización se haya realizado satizfactoriamente.
     * 9. Se elimina la zona.
     * 10. Se valida que la zona no exista.
     */

    //private String baseURL = "https://siie.qa.interedes.com.co/services";
    private String token;
    private  String CreatedIdZone;
    private String GetAllZones1;
    private String GetAllZones2;
    private String GetAllZones3;
    private String GetZoneById;
    private String UpdateZone;
    private String responseDeleteZoneById;
    private String validationDeleteZone;
     private String applicationQa = "siie.qa.interedes.com.co";
     int CantZones1;


    @BeforeEach
    public void getTokenTest() {

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

    }

    @Order(1)
    @Test
    public void getZones(){
        /***
         * 2. Esta es la prueba de consultar todas las zonas creadas antes
         * de  crear la nueva zona
         */

        GetAllZones1 = RestAssured.given()
                .log()
                .all()
                .contentType(ContentType.JSON)
                .header("application",applicationQa)
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
                .post("/user-service/zone/v1/get_zones_criteria")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        JsonPath arreglo1 = new JsonPath(GetAllZones1);
        List<Objects> ZonesArray1 = arreglo1.getList("data");
         CantZones1 = ZonesArray1.size();

        System.out.println("La cantidad de zonas existentes son: \n \n"+CantZones1);


        //System.out.println("se muestran todas las zonas existentes: \n " + GetAllZones1+"\n \n");
        }

        @Order(2)
        @Test
        public void createZone(){
            /***
             * 3. Esta es la prueba de la creación de una zona, donde se utiliza el token creado en la prueba anterior
             * y se almacena el ID de la zona creada.
             */

            CreatedIdZone = given()
                    .log()
                    .all()
                    .header("application",applicationQa)
                    .contentType(ContentType.JSON)
                    .header("Authorization", token )
                    .header("tenant","INTEREDES")
                    .body("{\n" +
                            "    \"description\": \"ZONE NORTH WEST10\",\n" +
                            "    \"status\": 1\n" +
                            "}")
                    .post("/dynamic-service/services/user-service/zone/v1/create_zone")
                    .then()
                    .log()
                    .all()
                    .statusCode(200)
                    .extract()
                    .path("data.id")
                    .toString();

            System.out.println("Este es el ID de la zona creada:  \n \n"+CreatedIdZone+"\n \n");


        }

        @Order(3)
        @Test
        public void validationCreateZone(){
            /***
             * 4. Esta es la prueba de consultar todas las zonas creadas despues
             * de haber creada la nueva zona,en la cual se validara el aumento en 1
             */

            GetAllZones2 = RestAssured.given()
                    .log()
                    .all()
                    .contentType(ContentType.TEXT)
                    .contentType(ContentType.JSON)
                    .header("application",applicationQa)
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
                    .post("/user-service/zone/v1/get_zones_criteria")
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

            Assertions.assertEquals(CantZones1 + 1, CantZones2);

            System.out.println("La cantidad de zonas actual es: "+CantZones2+" zonas\n");


        }

        @Order(4)
        @Test
        public void validationZoneByName(){
            /***
             * 5. Esta es la prueba de consultar todas las zonas creadas pero se valida
             * que el nombre de la zona se encuentre en los registros existentes.
             */

            GetAllZones3 = RestAssured.given()
                    .log()
                    .all()
                    .contentType(ContentType.TEXT)
                    .contentType(ContentType.JSON)
                    .header("application",applicationQa)
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
                    .post("/user-service/zone/v1/get_zones_criteria")
                    .then()
                    .log()
                    .all()
                    .statusCode(200)
                    .extract()
                    .body()
                    .asString();

            JsonPath arreglo3 = new JsonPath(GetAllZones3);
            String ZonesArray3 = arreglo3.getJsonObject("data["+CantZones1+"].description");
            System.out.println("El nombre de la zona creada fue: " + ZonesArray3 + "\n");

            Assertions.assertEquals("ZONE NORTH WEST10", ZonesArray3);

        }

        @Order(5)
        @Test
        public void getAllZoneDataById(){
            /***
             * 6. Esta es la prueba de consultar la zona creada por su ID, donde se utiliza el token y
             * se muestra toda la información referente a la zona.
             */

            GetZoneById = given()
                    .log()
                    .all()
                    .header("application",applicationQa)
                    .header("Authorization", token )
                    .header("tenant","INTEREDES")
                    .contentType(ContentType.JSON)
                    .get("/user-service/zone/v1/get_zone/"+ CreatedIdZone)
                    .then()
                    //.statusCode(200)
                    .extract()
                    .path("")
                    .toString();


            System.out.println("Esta es la información completa del registro de la zona de ID "+ CreatedIdZone +":  \n \n"+GetZoneById+"\n \n");


        }

        @Order(6)
        @Test
        public void updateZone(){
            /***
             * 7. Esta es la prueba de actualizar la zona, donde se actualiza el nombre o descripción de la zona,
             */
            UpdateZone = given()
                    .header("application",applicationQa)
                    .contentType(ContentType.JSON)
                    .header("Authorization", token )
                    .log()
                    .all()
                    .body("{\n" +
                            "    \"id\":"+CreatedIdZone+",\n" +
                            "    \"description\": \"ZONE NORTH WEST11\",\n" +
                            "    \"status\": 1\n" +
                            "}")
                    .put("/user-service/zone/v1/update_zone")
                    .then()
                    .extract()
                    .toString();

            //System.out.println("Esta es la información de la zona despues de ser actualizada: \n \n" + UpdateZone+"\n \n");


        }

        @Order(7)
        @Test
        public void validateZoneUpdate(){
            /***
             * 8. Esta es la validación de la actualización del registro de la zona
             */
            GetZoneById = RestAssured.given()
                    .header("application",applicationQa)
                    .header("Authorization", token )
                    .header("tenant","INTEREDES")
                    .contentType(ContentType.JSON)
                    .get("/user-service/zone/v1/get_zone/"+ CreatedIdZone)
                    .then()
                    .statusCode(200)
                    .extract()
                    .path("")
                    .toString();

            System.out.println("Esta es la información de la zona despues de ser z: \n \n"+GetZoneById+"\n \n");


        }

        @Order(8)
        @Test
        public void delteZone(){
            /***
             * 9. Esta es la eliminación de la zona creada
             */

            responseDeleteZoneById = given()
                    .header("application",applicationQa)
                    .contentType(ContentType.JSON)
                    .header("Authorization", token )
                    .log()
                    .all()
                    .delete("/user-service/zone/v1/delete_zone/"+CreatedIdZone)
                    .then()
                    .statusCode(204)
                    .toString();

            System.out.println("Se elimino satisfactoriamente la zona. "+responseDeleteZoneById);



        }

        @Order(9)
        @Test
        public  void validateDeltedZone(){
                /**
                 * 10. Se valida que la zona se haya eliminado
                 */

                validationDeleteZone = RestAssured.given()
                        .header("application",applicationQa)
                        .header("Authorization", token )
                        .header("tenant","INTEREDES")
                        .contentType(ContentType.JSON)
                        .get("/user-service/zone/v1/get_zone/"+ CreatedIdZone)
                        .then()
                        .statusCode(500)
                        .extract()
                        .path("")
                        .toString();

                System.out.println("Se eliminó correctamente la zona.");
            }


}
