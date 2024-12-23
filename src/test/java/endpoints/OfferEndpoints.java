package endpoints;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import pojo.Offer;
import pojo.OfferItem;

import java.util.ResourceBundle;

public class OfferEndpoints {
    OfferItem item;
    Offer offer;

    // get value from properties file
    static ResourceBundle getValue() {
        return ResourceBundle.getBundle("routes");
    }

    @Test(priority = 1, groups = {"smoke, regression, buyer"})
    public void postOffer() {
        item = new OfferItem();
        offer = new Offer();
        item.setPrice(20000);
        item.setToId(197971);
        item.setProductId(136451);
        offer.setOffer(item);

        String url = getValue().getString("postOfferUrl");
        Response response = RestAssured.given()
                .body(offer)
                .when()
                .post(url);
        response.then().log().body();
        Assert.assertEquals(response.getStatusCode(),201);
    }

    @Test(priority = 2)
    public void getOffer() {
        String url = getValue().getString("getOfferUrl");
        Response response = RestAssured.given()
                .pathParam("user_id", "197971")
                .when()
                .get(url);
        response.then().log().body();
        Assert.assertEquals(response.getStatusCode(),200);
    }

    public void updateOffer() {

    }
}
