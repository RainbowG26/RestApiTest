package authorizationTests;

import io.restassured.response.Response;
import libs.WebLibrary;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class LogInTest {

    static private String ApiEndPoint = "url";
    static private String pathGetUid = "/api/v2/getuid";
    static private String UserId;
    static private String accessToken;
    static private String pathSignTest = "/api/v2/signtest/";
    static private String privateKey;
    static private String pathToServer = "Path to server";
    static private String sigNature;
    static private String pubKey;
    static private String pathLogIn = "/api/v2/login";
    static private String amount = "1000";

    @Before
    public void urlToServerPresent() {
        WebLibrary.readConfig(pathToServer);
    }

    @Test(timeout = 5000)
    //1
    public void getTokenAuthTest() {
        Response responseGet = given()
                .contentType("application/json")
                .when().get(ApiEndPoint + pathGetUid)
                .then().log().all()
                .assertThat().statusCode(200)
                .extract().response();
        UserId = responseGet.jsonPath().get("uid");
        accessToken = responseGet.jsonPath().get("token");


        //2
        Map<String, String> authBody = new HashMap<>();
        authBody.put("forsign", UserId);
        authBody.put("private", privateKey);

        Response responsePost = given()
                .contentType("application/json")
                .body(authBody).log().all()
                .when().post(ApiEndPoint + pathSignTest)
                .then().log().all()
                .assertThat().statusCode(200)
                .extract().response();
        sigNature = responsePost.jsonPath().get("signature");
        pubKey = responsePost.jsonPath().get("pubkey");


        //3
        Map<String, String> tokenBody = new HashMap<>();
        tokenBody.put("pubkey", sigNature);
        tokenBody.put("signature", pubKey);
        tokenBody.put("amount", amount);

        given().auth().oauth2(accessToken)
                .contentType("application/json")
                .body(tokenBody).log().all()
                .when().post(ApiEndPoint + pathLogIn)
                .then().log().all()
                .assertThat().statusCode(200)
                .extract().response().prettyPrint();
    }

    @Test(timeout = 5000)
    public void reAuthorization() {
        Response responseGet = given()
                .contentType("application/json")
                .when().get(ApiEndPoint + pathGetUid)
                .then().log().all()
                .assertThat().statusCode(200)
                .extract().response();
        UserId = responseGet.jsonPath().get("uid");
        accessToken = responseGet.jsonPath().get("token");
        if (UserId == null || UserId == ""){
            System.out.println("UserId not found");
        } else {
            System.out.println("UserId =" + UserId);
        }


        //2
        Map<String, String> authBody = new HashMap<>();
        authBody.put("forsign", UserId);
        authBody.put("private", privateKey);

        Response responsePost = given()
                .contentType("application/json")
                .body(authBody).log().all()
                .when().post(ApiEndPoint + pathSignTest)
                .then().log().all()
                .assertThat().statusCode(200)
                .extract().response();
        sigNature = responsePost.jsonPath().get("signature");
        pubKey = responsePost.jsonPath().get("pubkey");
        if (privateKey == null || privateKey == ""){
            System.out.println("privateKey not found");
        } else {
            System.out.println("privateKey =" + privateKey);
        }

        //3
        Map<String, String> tokenBody = new HashMap<>();
        tokenBody.put("pubkey", sigNature);
        tokenBody.put("signature", pubKey);
        tokenBody.put("amount", amount);

        given().auth().oauth2(accessToken)
                .contentType("application/json")
                .body(tokenBody).log().all()
                .when().post(ApiEndPoint + pathLogIn)
                .then().log().all()
                .assertThat().statusCode(200)
                .extract().response().prettyPrint();
        if (amount == "amount" || amount == ""){
            System.out.println("amount =" + amount);
        } else {
            System.out.println("amount not found");
        }
    }
}
