package weather.endpoints;

import weather.exceptions.InvalidWeatherAPIRequestException;
import weather.records.WeatherAPIData;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class WeatherAPIClient {
    private final Map<String, Iterable<WeatherAPIData>> fakeLocationData;

    public WeatherAPIClient() {
        fakeLocationData = new HashMap<>();
    }

    public Iterable<WeatherAPIData> requestData(String q, int days) {
        throwOnInvalidQuery(q);
        if (fakeLocationData.containsKey(q)) {
            return fakeLocationData.get(q);
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        ArrayList<WeatherAPIData> weatherData = new ArrayList<>();

        for (int i = 0; i < days; i++) {
            weatherData.add(makeFakeData(calendar.getTime()));
            calendar.add(Calendar.DATE, 1);
        }

        fakeLocationData.put(q, weatherData);

        return weatherData;
    }

    private void throwOnInvalidQuery(String q) {
        if (q.isBlank()) {
            throw new InvalidWeatherAPIRequestException(
                "WeatherAPI query cannot be empty"
            );
        }
    }

    private WeatherAPIData makeFakeData(Date date) {
        ThreadLocalRandom localRandom = ThreadLocalRandom.current();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        double tempCelsius = localRandom.nextDouble(-10, 40);
        double tempFahrenheit = (tempCelsius * 1.8) + 32;
        double windKPH = localRandom.nextDouble(0, 25);
        double windMPH = windKPH * 0.6214;
        double humidity = localRandom.nextDouble(0, 100);

        return new WeatherAPIData(
            tempCelsius,
            tempFahrenheit,
            windMPH,
            windKPH,
            humidity,
            dateFormat.format(date)
        );
    }
}
