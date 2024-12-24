package endpoints;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pojo.Offer;
import pojo.OfferItem;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ResourceBundle;

public class OfferEndpoints {
    OfferItem item;
    Offer offer;
    static int product_id;
    static int seller_id;
    static int buyer_id;
    static int offer_id;
    static String cookie_user;
    static String cookie_seller;

    // get value from properties file
    static ResourceBundle getValue() {
        return ResourceBundle.getBundle("routes");
    }

    @BeforeClass(groups = {"buyer", "seller"})
    public void setup() throws IOException {
        // read product id from json file
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode data_seller = objectMapper.readTree(new File("src/test/java/credentials/seller.json"));
        seller_id = data_seller.get("seller_id").asInt();
        cookie_seller = data_seller.get("token_seller").asText();

        JsonNode data_user = objectMapper.readTree(new File("src/test/java/credentials/buyer.json"));
        buyer_id = data_user.get("buyer_id").asInt();
        cookie_user = data_user.get("token_buyer").asText();

        JsonNode data_product = objectMapper.readTree(new File("src/test/java/credentials/product.json"));
        product_id = data_product.get("product_id").asInt();

        JsonNode data_offer = objectMapper.readTree(new File("src/test/java/credentials/offer.json"));
        offer_id = data_offer.get("offer_id").asInt();
    }

    @Test(priority = 1, groups = {"smoke", "regression", "buyer"})
    public void postOffer() throws IOException {
        JSONObject detail = new JSONObject();
        detail.put("price", 10101);
        detail.put("to_id", seller_id);
        detail.put("product_id", product_id);
        JSONObject data = new JSONObject();
        data.put("offer", detail);

        String url = getValue().getString("postOfferUrl");
        Response response = RestAssured.given()
                .cookie("_binar_playground_secondhand_marketplace_session",cookie_user)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(data.toMap())
                .when()
                .post(url);
        response.then().log().body();
        Assert.assertEquals(response.getStatusCode(),201);

        // save offer id to json file
        JSONObject offerid = new JSONObject();
        JsonPath jP = response.jsonPath();
        offer_id = jP.getInt("offer.id");
        offerid.put("offer_id", offer_id);
        FileWriter file = new FileWriter("src/test/java/credentials/offer.json");
        file.write(offerid.toString());
        file.close();
    }

    @Test(priority = 2, groups = {"seller"})
    public void getOffer() {
        String url = getValue().getString("getOfferUrl");
        Response response = RestAssured.given()
                .cookie("_binar_playground_secondhand_marketplace_session",cookie_seller)
                .pathParam("user_id", buyer_id)
                .when()
                .get(url);
        response.then().log().body();
        Assert.assertEquals(response.getStatusCode(),200);
    }

    @Test(priority = 3, groups = {"seller"})
    public void updateOffer() {
        item = new OfferItem();
        item.setStatus("accepted");
        offer = new Offer();
        offer.setOffer(item);

        String url = getValue().getString("updateOfferUrl");
        Response response = RestAssured.given()
                .cookie("_binar_playground_secondhand_marketplace_session",cookie_seller)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .pathParam("id", offer_id)
                .body(offer)
                .when()
                .put(url);
        response.then().log().body();
        Assert.assertEquals(response.getStatusCode(),200);
    }
}
