package by.googlemapid.sample;

import java.io.IOException;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.collect.Maps;

public class DirectionSample extends AbstractSample {
    public static void main(final String[] args) throws IOException, JSONException {
        final String proxy = "http://anonymouse.org/cgi-bin/anon-www.cgi/";
        final String baseUrl = proxy + "http://maps.googleapis.com/maps/api/directions/json";// путь к Geocoding API по
        // HTTP
        final Map<String, String> params = Maps.newHashMap();
        params.put("sensor", "false");// указывает, исходит ли запрос на геокодирование от устройства с датчиком
        params.put("language", "ru");// язык данные на котором мы хочем получить
        params.put("mode", "walking");// способ перемещения, может быть driving, walking, bicycling
        params.put("origin", "Россия, Москва, улица Дениса Давыдова, 7");// адрес или текстовое значение широты и
        // отправного пункта маршрута
        params.put("destination", "Россия, Москва, улица Кульнева 3");// адрес или текстовое значение широты и долготы
        // долготы конечного пункта маршрута
        final String url = baseUrl + '?' + encodeParams(params);// генерируем путь с параметрами
        System.out.println(url); // Можем проверить что вернет этот путь в браузере
        final JSONObject response = JsonReader.read(url);// делаем запрос к вебсервису и получаем от него ответ
        // как правило наиболее подходящий ответ первый и данные о кординатах можно получить по пути
        // //results[0]/geometry/location/lng и //results[0]/geometry/location/lat
        JSONObject location = response.getJSONArray("routes").getJSONObject(0);
        location = location.getJSONArray("legs").getJSONObject(0);
        final String distance = location.getJSONObject("distance").getString("text");
        final String duration = location.getJSONObject("duration").getString("text");
        System.out.println(distance + "\n" + duration);
    }
}
