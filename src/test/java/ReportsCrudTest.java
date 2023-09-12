import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class ReportsCrudTest {

    private String token;
    private String appQa = "application";
    private String applicationQa= "siie.qa.interedes.com.co";

    @BeforeEach
    public void setUp(){
        var tkn = new TokenGenerator();
        token = tkn.getToken();
    }
    @Order(1)
    @Test
    public void getAllReportsTest(){
        given()
                .log()
                .all()
                .header("Authorization",token)
                .header(appQa, applicationQa)
                .header("TENANT", "INTEREDES")
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"filters\": [],\n" +
                        "    \"sorts\": [" +
                        "{\n" +
                        "            \"key\":\"id\",\n" +
                        "            \"direction\":\"ASC\"\n" +
                        "        }" +
                        "]\n" +
                        "}")
                .post("/dynamic-service/services/operacion-campo-service/form/v1/get_forms_criteria")
                .then()
                .log()
                .all()
                .statusCode(200);
    }

    @Order(2)
    @Test
    public void createReportTest(){

        given()
                .log()
                .all()
                .header("Authorization",token)
                .header(appQa, applicationQa)
                .header("TENANT", "INTEREDES")
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"template\": \"{\\\"Dictionary\\\":{\\\"Resources\\\":{\\\"0\\\":{\\\"Name\\\":\\\"empty\\\",\\\"Alias\\\":\\\"Empty image\\\",\\\"Image\\\":\\\"iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNk+M9QDwADhgGAWjR9awAAAABJRU5ErkJggg==\\\"}},\\\"Variables\\\":{\\\"0\\\":{\\\"Value\\\":\\\"\\\",\\\"Name\\\":\\\"Ningun Variable todavia\\\",\\\"DialogInfo\\\":{\\\"DateTimeType\\\":\\\"DateAndTime\\\"},\\\"Alias\\\":\\\"!\\\",\\\"Type\\\":\\\"System.String\\\",\\\"Description\\\":\\\"Variable de pruebas\\\"}},\\\"DataSources\\\":{\\\"0\\\":{\\\"Ident\\\":\\\"StiDataTableSource\\\",\\\"Name\\\":\\\"Plantilla\\\",\\\"Alias\\\":\\\"DataSet\\\",\\\"Columns\\\":{\\\"0\\\":{\\\"Name\\\":\\\"Numero\\\",\\\"Index\\\":-1,\\\"NameInSource\\\":\\\"number\\\",\\\"Alias\\\":\\\"#\\\",\\\"Type\\\":\\\"System.Decimal\\\"},\\\"1\\\":{\\\"Name\\\":\\\"Dato\\\",\\\"Index\\\":-1,\\\"NameInSource\\\":\\\"data\\\",\\\"Alias\\\":\\\"!\\\",\\\"Type\\\":\\\"System.String\\\"}},\\\"NameInSource\\\":\\\"Plantillas.root\\\"}}},\\\"Pages\\\":{\\\"0\\\":{\\\"Ident\\\":\\\"StiPage\\\",\\\"Name\\\":\\\"Page1\\\",\\\"Guid\\\":\\\"4842cf1aff3378705cc218177170a81e\\\",\\\"Interaction\\\":{\\\"Ident\\\":\\\"StiInteraction\\\"},\\\"Border\\\":\\\";;2;;;;;solid:Black\\\",\\\"Brush\\\":\\\"solid:Transparent\\\",\\\"PageWidth\\\":21,\\\"PageHeight\\\":29.7,\\\"Watermark\\\":{\\\"TextBrush\\\":\\\"solid:50,0,0,0\\\"},\\\"Margins\\\":{\\\"Left\\\":1,\\\"Right\\\":1,\\\"Top\\\":1,\\\"Bottom\\\":1}}}}\",\n" +
                        "    \"formId\": 90,\n" +
                        "    \"versionsForm\": \"1043\"\n" +
                        "}")
                .post("/dynamic-service/services/operacion-campo-service/report-template/v1/create")
                .then()
                .log()
                .all()
                .statusCode(200)
                .extract()
                .body()
                .asString();
    }


}
