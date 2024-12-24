package utility;

import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

public class JsonManager {

    public static void jsonSave(JSONObject data, String fileName) throws IOException {
        FileWriter file = new FileWriter("src/test/java/credentials/"+fileName);
        file.write(data.toString());
        file.close();
    }
}
