package endpoints;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.filter.cookie.CookieFilter;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pojo.Auth;
import pojo.AuthItem;

import java.io.File;
import java.util.ResourceBundle;

public class AuthEndpoints {
    AuthItem authItem;
    Auth auth;
    CookieFilter filter;
    Faker faker;

    // get value from properties file
    static ResourceBundle getValue() {
        return ResourceBundle.getBundle("routes");
    }

    @BeforeClass(groups = {"smoke", "regression", "seller", "buyer"})
    public void setup() {
        filter = new CookieFilter();
        authItem = new AuthItem();
        faker = new Faker();

        authItem.setName(faker.name().fullName());
        authItem.setEmail(faker.internet().emailAddress());
        authItem.setPassword(faker.internet().password(1, 8));

        auth = new Auth();
        auth.setUser(authItem);
    }

    @Test(priority = 1, groups = {"seller", "smoke"})
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
        JsonPath res = response.jsonPath();
        if (res.get("user.email") != authItem.getEmail() && res.get("user.email") != null) { // if email not exists
            Assert.assertEquals(res.get("user.name"), authItem.getName());
            Assert.assertEquals(response.getStatusCode(), 200);
        } else {
            Assert.assertEquals(res.get("errors.email[0]"), "has already been taken");
        }
    }

    @Test(priority = 2, groups = {"smoke", "regression", "seller", "buyer"})
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

    @Test(priority = 3, groups = {"smoke", "regression", "seller", "buyer"})
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

    @Test(priority = 4, groups = {"smoke", "regression", "seller", "buyer"})
    public void getProfile() {
        String url = getValue().getString("getProfileUrl");
        Response response = RestAssured.given()
                .filter(filter)
                .get(url);
        response.then().log().body();
        Assert.assertEquals(response.getStatusCode(), 200);
    }
}
