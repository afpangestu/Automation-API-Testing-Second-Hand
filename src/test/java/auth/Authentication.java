package auth;

import io.restassured.RestAssured;
import io.restassured.filter.cookie.CookieFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import pojo.Auth;
import pojo.User;

import java.util.ResourceBundle;

public class Authentication {
    User user;
    Auth auth;

    // get value from properties file
    static ResourceBundle getValue() {
        return ResourceBundle.getBundle("routes");
    }

    public CookieFilter getSession() {
        CookieFilter filter = new CookieFilter();

        user = new User();
        user.setName("aji f p");
        user.setEmail("ajimail@mail.com");
        user.setPassword("ajifp123");
        auth = new Auth();
        auth.setUser(user);

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
