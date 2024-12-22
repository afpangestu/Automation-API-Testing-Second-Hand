package endpoints;

import auth.Authentication;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.util.ResourceBundle;

public class ProductEndpoints {
    Authentication authen;

    // get value from properties file
    static ResourceBundle getValue() {
        return ResourceBundle.getBundle("routes");
    }

    @BeforeClass
    public void setup() {
        authen = new Authentication();
    }

    @Test
    public void postProduct() {
        File img = new File("src/test/resources/image/klepon1.png");
        String url = getValue().getString("postProductUrl");
        Response response = RestAssured.given()
                .filter(authen.getSession())
                .multiPart("product[name]", "Product xyz")
                .multiPart("product[price]", "20000")
                .multiPart("product[description]", "Ini deskripsi produk")
                .multiPart("product[status]", "published")
                .multiPart("product[category_id]", 1)
                .multiPart("product[images][]", img)
                .when()
                .post(url);
        response.then().log().body();
        Assert.assertEquals(response.getStatusCode(), 201);
    }
}
