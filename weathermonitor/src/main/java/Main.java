import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.SECONDS;

public class Main {
    static Map<String, Long> frequencyByCity = new ConcurrentHashMap<>();
    static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static void main(String[] args) throws Exception {

        JSONArray cities = FileUtil.parseFileToJsonObject("src/main/resources/cities.json");
        cities.forEach(city -> parseCityObject((JSONObject) city));
        RequestHandler requestHandler = new RequestHandler();
        frequencyByCity.forEach((k, v) -> scheduler.schedule(() -> {
            try {
                String response = requestHandler.sendGet(k);
                FileUtil.parseJsonAndSaveToFile(response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, frequencyByCity.get(k), SECONDS));

    }

    private static void parseCityObject(JSONObject city) {
        String cityId = String.valueOf(city.get("city_id"));
        String cityName = (String) city.get("city_name");
        Long frequency = (Long) city.get("frequency");

        frequencyByCity.put(cityId, frequency);
        System.out.println(cityName + " " + "saved to memory");
    }
}
