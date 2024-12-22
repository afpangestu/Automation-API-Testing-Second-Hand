package endpoints;

import io.restassured.RestAssured;
import io.restassured.filter.cookie.CookieFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pojo.Auth;
import pojo.User;

import java.io.File;
import java.util.ResourceBundle;

public class AuthEndpoints {
    User user;
    Auth auth;
    CookieFilter filter;

    // get value from properties file
    static ResourceBundle getValue() {
        return ResourceBundle.getBundle("routes");
    }

    @BeforeClass
    public void setup() {
        filter = new CookieFilter();
        user = new User();
        user.setName("aji f p");
        user.setEmail("ajimail@mail.com");
        user.setPassword("ajifp123");
        auth = new Auth();
        auth.setUser(user);
    }

    @Test(priority = 1)
    public void postRegisterUser() {
        String url = getValue().getString("userRegistrationUrl");
        Response response = RestAssured.given()
                .filter(filter)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(auth)
                .when()
                .post(url);
        response.then().log().body();
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 2)
    public void postLoginUser() {
        String url = getValue().getString("userLoginUrl");
        Response response = RestAssured.given()
                .filter(filter)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(auth)
                .when()
                .post(url);
        response.then().log().body();
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 3)
    public void putUpdateProfile() {
        String url = getValue().getString("updateProfileUrl");
        File img = new File("src/test/resources/image/garuda biru.jpg");
        Response response = RestAssured.given()
                .filter(filter)
                .multiPart("user[name]", "Peringatan DaruratWWW")
                .multiPart("user[phone_number]", "082727272")
                .multiPart("user[address]", "Jl. Monogl 3")
                .multiPart("user[city]", 2)
                .multiPart("user[avatar]", img)
                .when()
                .put(url);
        response.then().log().body();
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 4)
    public void getProfile() {
        String url = getValue().getString("getProfileUrl");
        Response response = RestAssured.given()
                .filter(filter)
                .get(url);
        response.then().log().body();
        Assert.assertEquals(response.getStatusCode(), 200);
    }
}
