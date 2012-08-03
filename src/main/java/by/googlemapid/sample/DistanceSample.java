package by.googlemapid.sample;

import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

import java.io.IOException;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.collect.Maps;

public class DistanceSample extends AbstractSample {
    private static final double EARTH_RADIUS = 6371.; // Радиус Земли

    public static void main(final String[] args) throws IOException, JSONException {
        final Point subwayStationPoint = getPoint("Россия, Москва, улица Поклонная, 12");
        final Point addressPoint = getPoint("Россия, Москва, станция метро Парк Победы");

        // Рассчитываем расстояние между точками
        final double dlng = deg2rad(subwayStationPoint.lng - addressPoint.lng);
        final double dlat = deg2rad(subwayStationPoint.lat - addressPoint.lat);
        final double a = sin(dlat / 2) * sin(dlat / 2) + cos(deg2rad(addressPoint.lat))
                * cos(deg2rad(subwayStationPoint.lat)) * sin(dlng / 2) * sin(dlng / 2);
        final double c = 2 * atan2(sqrt(a), sqrt(1 - a));
        System.out.println("distance: " + c * EARTH_RADIUS); // получаем расстояние в километрах
    }

    /**
     * Класс точки, хранит зачения в градусах
     * 
     */
    private static class Point {
        public double lat;
        public double lng;

        public Point(final double lng, final double lat) {
            this.lng = lng;
            this.lat = lat;
        }

        @Override
        public String toString() {
            return lat + "," + lng;
        }
    }

    /**
     * Геокодирует адрес
     * 
     * @param address
     * @return
     * @throws IOException
     * @throws JSONException
     */
    private static Point getPoint(final String address) throws IOException, JSONException {
        final String baseUrl = "http://maps.googleapis.com/maps/api/geocode/json";// путь к Geocoding API по HTTP
        final Map<String, String> params = Maps.newHashMap();
        params.put("sensor", "false");// указывает, исходит ли запрос на геокодирование от устройства с датчиком
        // местоположения
        params.put("address", address);// адрес, который нужно геокодировать
        final String url = baseUrl + '?' + encodeParams(params);// генерируем путь с параметрами
        System.out.println(url);// Можем проверить что вернет этот путь в браузере
        final JSONObject response = JsonReader.read(url);// делаем запрос к вебсервису и получаем от него ответ
        // как правило наиболее подходящий ответ первый и данные о кординатах можно получить по пути
        // //results[0]/geometry/location/lng и //results[0]/geometry/location/lat
        JSONObject location = response.getJSONArray("results").getJSONObject(0);
        location = location.getJSONObject("geometry");
        location = location.getJSONObject("location");
        final double lng = location.getDouble("lng");// долгота
        final double lat = location.getDouble("lat");// широта
        final Point point = new Point(lng, lat);
        System.out.println(address + " " + point); // выводим адрес и точку для него
        return point;
    }

    /**
     * Преобразует значение из градусов в радианы
     * 
     * @param degree
     * @return
     */
    private static double deg2rad(final double degree) {
        return degree * (Math.PI / 180);
    }
}
