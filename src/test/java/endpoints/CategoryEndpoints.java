package endpoints;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ResourceBundle;
import java.util.concurrent.ThreadLocalRandom;

public class CategoryEndpoints {

    // get value from properties file
    static ResourceBundle getValue() {
        return ResourceBundle.getBundle("routes");
    }

    @Test(groups = {"smoke", "regression"})
    public void getCategory() {
        String url = getValue().getString("getCategoryUrl");
        Response response = RestAssured.given()
                .when()
                .get(url);
        response.then().log().body();
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test(groups = {"regression"})
    public void getCategoryById() {
        String url = getValue().getString("getCategoryByIdUrl");
        Response response = RestAssured.given()
                .pathParam("id", getRandomValue(1, 5))
                .when()
                .get(url);
        response.then().log().body();
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    public static int getRandomValue(int min, int max) {
        // Get and return the random integer
        // within Min and Max
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}
