package auth;

import io.restassured.RestAssured;
import io.restassured.filter.cookie.CookieFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import pojo.Auth;
import pojo.AuthItem;

import java.util.ResourceBundle;

public class Authentication {
    AuthItem authItem;
    Auth auth;

    // get value from properties file
    static ResourceBundle getValue() {
        return ResourceBundle.getBundle("routes");
    }

    public CookieFilter getSession() {
        CookieFilter filter = new CookieFilter();

        authItem = new AuthItem();
        authItem.setName("aji f p");
        authItem.setEmail("ajimail@mail.com");
        authItem.setPassword("ajifp123");
        auth = new Auth();
        auth.setUser(authItem);

        String url = getValue().getString("userLoginUrl");

        Response response = RestAssured.given()
                .filter(filter)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(auth).when().post(url);
        response.then().log().body();
        Assert.assertEquals(response.getStatusCode(), 200);
        return filter;
    }
}
