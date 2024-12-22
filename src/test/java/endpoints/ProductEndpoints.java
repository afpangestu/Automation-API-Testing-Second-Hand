package endpoints;

import io.restassured.RestAssured;
import io.restassured.filter.cookie.CookieFilter;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pojo.Auth;
import pojo.User;

import java.io.File;
import java.util.ResourceBundle;

public class ProductEndpoints {
//    User user;
//    Auth auth;
    CookieFilter filter;
    // get value from properties file
    static ResourceBundle getValue() {
        return ResourceBundle.getBundle("routes");
    }
//    AuthEndpoints authEndpoints;
//    @BeforeClass
//    public void setup() {
//        filter = new CookieFilter();
//        user = new User();
//        user.setName("aji f p");
//        user.setEmail("ajimail@mail.com");
//        user.setPassword("ajifp123");
//        auth = new Auth();
//        auth.setUser(user);
//
//        authEndpoints.postLoginUser();
//    }

    @Test
    public void postProduct() {
        String url = getValue().getString("postProductUrl");
        File img = new File("src/test/resources/image/klepon1.png");
        Response response = RestAssured.given()
                .filter(filter)
                .multiPart("price[name]", "Product xyz")
                .multiPart("price[price]", "20000")
                .multiPart("price[description]", "Ini deskripsi produk")
                .multiPart("price[status]", "published")
                .multiPart("price[category_id]", 2)
                .when()
                .post(url);
        response.then().log().body();
        Assert.assertEquals(response.getStatusCode(), 200);
    }
}
