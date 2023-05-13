package weather.endpoints;

import weather.records.OpenMeteoData;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class OpenMeteoClient {
    private final Map<String, OpenMeteoData> fakeLocationData;
    private static final Map<String, String> units = Map.of(
        "time", "iso8601",
        "temperature_2m_max", "°C",
        "temperature_2m_min", "°C",
        "windspeed_10m_max", "km/h",
        "windgusts_10m_max", "km/h"
    );
    private static final int forecastSize = 7;

    public OpenMeteoClient() {
        fakeLocationData = new HashMap<>();
    }

    public OpenMeteoData requestData(double longitude, double latitude) {
        String locationKey = longitude + "," + latitude;
        if (fakeLocationData.containsKey(locationKey)) {
            return fakeLocationData.get(locationKey);
        }

        Iterable<String> dates = getDates();
        Iterable<Double> maxTemperatures = getFakeDecimalDataMinMax(5, 35);
        Iterable<Double> minTemperatures = getFakeTemperatureMin(maxTemperatures);
        Iterable<Double> windSpeeds = getFakeDecimalDataMinMax(0, 25);
        Iterable<Double> windGusts = getFakeDecimalDataMinMax(0, 25);
        Iterable<Double> windDirections = getFakeDecimalDataMinMax(1, 359);
        OpenMeteoData openMeteoData = new OpenMeteoData(
            units,
            dates,
            maxTemperatures,
            minTemperatures,
            windSpeeds,
            windGusts,
            windDirections
        );

        fakeLocationData.put(locationKey, openMeteoData);

        return openMeteoData;
    }

    private Iterable<String> getDates() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        ArrayList<String> dates = new ArrayList<>();

        for (int i = 0; i < forecastSize; i++) {
            Date date = calendar.getTime();
            dates.add(dateFormat.format(date));
            calendar.add(Calendar.DATE, 1);
        }

        return dates;
    }

    private Iterable<Double> getFakeDecimalDataMinMax(double min, double max) {
        ArrayList<Double> data = new ArrayList<>();
        ThreadLocalRandom localRandom = ThreadLocalRandom.current();

        for (int i = 0; i < forecastSize; i++) {
            data.add(
                    localRandom.nextDouble(min, max)
            );
        }

        return data;
    }

    private Iterable<Double> getFakeTemperatureMin(Iterable<Double> maxTemperatures) {
        ArrayList<Double> temperatures = new ArrayList<>();
        ThreadLocalRandom localRandom = ThreadLocalRandom.current();

        for (double maxTemp : maxTemperatures) {
            temperatures.add(
                localRandom.nextDouble(
                    maxTemp - 10,
                    maxTemp
                )
            );
        }

        return temperatures;
    }
}
