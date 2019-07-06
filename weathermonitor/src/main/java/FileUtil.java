import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

public class FileUtil {
    static JSONParser jsonParser = new JSONParser();

    public static JSONArray parseFileToJsonObject(String fileName) throws IOException {


        try {
            FileReader reader = new FileReader(fileName);

            //Read JSON file
            Object obj = jsonParser.parse(reader);

            JSONArray array = (JSONArray) obj;
            return array;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void parseJsonAndSaveToFile(String string) throws ParseException, IOException {
        JSONObject json = (JSONObject) jsonParser.parse(string);
        String fileName = (String) json.get("name");
        JSONObject tempObj = (JSONObject) json.get("main");
        JSONObject windObj = (JSONObject) json.get("wind");
        String windSpeed = windObj.get("speed").toString();
        double temp = (double) tempObj.get("temp");
        Double temperature = (((temp - 32) * 5) / 9);
        DecimalFormat df = new DecimalFormat("#.##");
        JSONObject newJson = new JSONObject();
        newJson.put("name", fileName);
        newJson.put("time", json.get("dt"));
        newJson.put("temperature", df.format(temperature));
        newJson.put("wind_speed", windSpeed);
        try (FileWriter file = new FileWriter(fileName + ".json")) {
            file.write(newJson.toJSONString());
        }
    }
}
