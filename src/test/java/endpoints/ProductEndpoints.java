package endpoints;

import auth.Authentication;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.util.ResourceBundle;

public class ProductEndpoints {
    Authentication auth;

    // get value from properties file
    static ResourceBundle getValue() {
        return ResourceBundle.getBundle("routes");
    }

    @BeforeClass
    public void setup() {
        auth = new Authentication();
    }

    @Test(priority = 1)
    public void postProduct() {
        File img1 = new File("src/test/resources/image/klepon1.png");
        File img2 = new File("src/test/resources/image/garuda biru.jpg");

        String url = getValue().getString("postProductUrl");
        Response response = RestAssured.given()
                .filter(auth.getSession())
                .multiPart("product[name]", "Product xyz array")
                .multiPart("product[price]", "20000")
                .multiPart("product[description]", "Ini deskripsi produk")
                .multiPart("product[status]", "published")
                .multiPart("product[category_id]", 1)
                .multiPart("product[images][]", img1)
                .multiPart("product[images][]", img2)
                .when()
                .post(url);
        response.then().log().body();
        Assert.assertEquals(response.getStatusCode(), 201);
    }

    @Test(priority = 2)
    public void getProduct() {
        String url = getValue().getString("getProductUrl");
        Response response = RestAssured.given()
                .queryParam("page",1)
                .get(url);
        response.then().log().body();
        Assert.assertEquals(response.getStatusCode(), 200);
    }
}
