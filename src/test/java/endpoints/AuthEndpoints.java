package endpoints;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.filter.cookie.CookieFilter;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pojo.Auth;
import pojo.AuthItem;
import utility.JsonManager;

import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

public class AuthEndpoints {
    AuthItem authItem;
    Auth auth;
    CookieFilter filter;
    Faker faker;
    JSONObject data;

    public static String cookie;

    // get value from properties file
    static ResourceBundle getValue() {
        return ResourceBundle.getBundle("routes");
    }

    @BeforeClass(groups = {"smoke", "regression", "seller", "buyer"})
    public void setup() {
        filter = new CookieFilter();
        authItem = new AuthItem();
        faker = new Faker();
        data = new JSONObject();

        authItem.setName(faker.name().fullName());
        authItem.setEmail(faker.internet().emailAddress());
        authItem.setPassword(faker.internet().password(1, 8));

        auth = new Auth();
        auth.setUser(authItem);
    }

    @Test(priority = 1, groups = {"buyer", "smoke"})
    public void postRegisterUser() throws IOException {
        String url = getValue().getString("userRegistrationUrl");
        Response response = RestAssured.given()
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
            // save user information to json file
            cookie = response.getCookie("_binar_playground_secondhand_marketplace_session");
            data.put("buyer_id", res.getInt("user.id"));
            data.put("buyer_name", res.getString("user.name"));
            data.put("token_buyer", cookie);
            JsonManager.jsonSave(data, "buyer.json");
        } else {
            Assert.assertEquals(res.get("errors.email[0]"), "has already been taken");
        }
    }

    @Test(priority = 2, groups = {"smoke", "regression", "buyer"})
    public void postLoginUser() {
        String url = getValue().getString("userLoginUrl");
        Response response = RestAssured.given()
                .cookie("_binar_playground_secondhand_marketplace_session",cookie)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(auth)
                .when()
                .post(url);
        response.then().log().body();
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 3, groups = {"smoke", "regression", "buyer"})
    public void putUpdateProfile() {
        String url = getValue().getString("updateProfileUrl");
        File img = new File("src/test/resources/image/garuda biru.jpg");
        Response response = RestAssured.given()
                .cookie("_binar_playground_secondhand_marketplace_session",cookie)
                .multiPart("user[name]", faker.name().fullName())
                .multiPart("user[phone_number]", faker.phoneNumber().phoneNumber())
                .multiPart("user[address]", faker.address().secondaryAddress())
                .multiPart("user[city_id]", 3)
                .multiPart("user[avatar]", img)
                .when()
                .put(url);
        response.then().log().body();
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 4, groups = {"smoke", "regression", "buyer"})
    public void getProfile() {
        String url = getValue().getString("getProfileUrl");
        Response response = RestAssured.given()
                .cookie("_binar_playground_secondhand_marketplace_session",cookie)
                .get(url);
        response.then().log().body();
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 5, groups = {"smoke", "regression", "seller"})
    public void postLoginSeller() throws IOException {
        data = new JSONObject();
        authItem = new AuthItem();
        authItem.setEmail("ajiseller@mail.com");
        authItem.setPassword("ajiseller");
        auth = new Auth();
        auth.setUser(authItem);

        String url = getValue().getString("userLoginUrl");
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(auth)
                .when()
                .post(url);
        response.then().log().body();
        Assert.assertEquals(response.getStatusCode(), 200);
        // save cookie to json file
        JsonPath res = response.jsonPath();
        cookie = response.getCookie("_binar_playground_secondhand_marketplace_session");
        data.put("seller_id", res.getInt("user.id"));
        data.put("seller_name", res.getString("user.name"));
        data.put("token_seller", cookie);
        JsonManager.jsonSave(data, "seller.json");
    }
}
