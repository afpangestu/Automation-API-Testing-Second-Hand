package endpoints;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ResourceBundle;

public class ProductEndpoints {
    JSONObject data;
    static int product_id;
    static String cookie;

    // get value from properties file
    static ResourceBundle getValue() {
        return ResourceBundle.getBundle("routes");
    }

    @BeforeClass(groups = {"seller"})
    public void setup() throws IOException {
        data = new JSONObject();
        // read cookie to json file
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(new File("src/test/java/credentials/seller.json"));
        cookie = jsonNode.get("token_seller").asText();
    }

    @Test(priority = 1, groups = {"seller"})
    public void postProduct() throws IOException {
        File img1 = new File("src/test/resources/image/klepon1.png");
        File img2 = new File("src/test/resources/image/garuda biru.jpg");
        String url = getValue().getString("postProductUrl");
        Response response = RestAssured.given()
                .cookie("_binar_playground_secondhand_marketplace_session",cookie)
                .multiPart("product[name]", "Product xyz")
                .multiPart("product[price]", "20000")
                .multiPart("product[description]", "Ini deskripsi produk")
                .multiPart("product[status]", "published")
                .multiPart("product[category_id]", 4)
                .multiPart("product[images][]", img1)
                .multiPart("product[images][]", img2)
                .when()
                .post(url);
        response.then().log().body();
        JsonPath jP = response.jsonPath();
        product_id = jP.getInt("product.id");
        Assert.assertEquals(response.getStatusCode(), 201);

        // save product id to json file
        data.put("product_id", product_id);
        FileWriter file = new FileWriter("src/test/java/credentials/product.json");
        file.write(data.toString());
        file.close();
    }

    @Test(priority = 2, groups = {"seller"})
    public void getProduct() {
        String url = getValue().getString("getProductUrl");
        Response response = RestAssured.given()
                .queryParam("page",1)
                .get(url);
        response.then().log().body();
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 3, groups = {"seller"})
    public void getProductbyId() {
        String url = getValue().getString("getProductByIdUrl");
        Response response = RestAssured.given()
                .pathParam("id", product_id)
                .get(url);
        response.then().log().body();
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 4, groups = {"seller"})
    public void updateProduct() {
        File img = new File("src/test/resources/image/gorengan.jpg");

        String url = getValue().getString("updateProductUrl");
        Response response = RestAssured.given()
                .cookie("_binar_playground_secondhand_marketplace_session",cookie)
                .pathParam("id", product_id)
                .multiPart("product[name]", "updatecProduct xyz yihii")
                .multiPart("product[price]", "50000")
                .multiPart("product[description]", "Ini deskripsi produk")
                .multiPart("product[status]", "published")
                .multiPart("product[category_id]", 4)
                .multiPart("product[images][]", img)
                .put(url);
        response.then().log().body();
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 5)
    public void deleteProduct() {
        String url = getValue().getString("deleteProductUrl");
        Response response = RestAssured.given()
                .cookie("_binar_playground_secondhand_marketplace_session",cookie)
                .pathParam("id", product_id)
                .delete(url);
        response.then().log().body();
        Assert.assertEquals(response.getStatusCode(), 204);
    }
}
