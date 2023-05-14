package weather.endpoints;

import weather.records.WeatherGovData;
import weather.records.WeatherGovGrid;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class WeatherGovClient {
    private final Map<String, WeatherGovGrid> fakeGridLocations;
    private final Map<String, Iterable<WeatherGovData>> fakeLocationData;
    private static final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String directions = "NSEW";

    public WeatherGovClient() {
        fakeGridLocations = new HashMap<>();
        fakeLocationData = new HashMap<>();
    }

    public Iterable<WeatherGovData> requestData(double longitude, double latitude) {
        WeatherGovGrid grid = convertLatLongToGrid(longitude, latitude);
        return submitRequest(grid);
    }

    private WeatherGovGrid convertLatLongToGrid(double longitude, double latitude) {
        return getFakeGridLocation(longitude, latitude);
    }

    private Iterable<WeatherGovData> submitRequest(WeatherGovGrid grid) {
        String locationKey = grid.office() + grid.gridX() + grid.gridY();
        if (fakeLocationData.containsKey(locationKey)) {
            return fakeLocationData.get(locationKey);
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        ArrayList<WeatherGovData> weatherData = new ArrayList<>();

        for (int i = 0; i < 14; i++) {
            weatherData.add(makeFakeWeatherGovData(calendar));
            calendar.add(Calendar.DATE, 1);
        }

        fakeLocationData.put(locationKey, weatherData);

        return weatherData;
    }

    private WeatherGovGrid getFakeGridLocation(double longitude, double latitude) {
        String locationKey = longitude + "," + latitude;
        if (fakeGridLocations.containsKey(locationKey)) {
            return fakeGridLocations.get(locationKey);
        }

        ThreadLocalRandom localRandom = ThreadLocalRandom.current();
        String office = generateFakeOffice();
        int gridX = localRandom.nextInt(1, 100);
        int gridY = localRandom.nextInt(1, 100);
        WeatherGovGrid grid = new WeatherGovGrid(
            office,
            gridX,
            gridY
        );

        fakeGridLocations.put(locationKey, grid);

        return grid;
    }

    private String generateFakeOffice() {
        ThreadLocalRandom localRandom = ThreadLocalRandom.current();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < 3; i++) {
            int index = localRandom.nextInt(alphabet.length());
            stringBuilder.append(
                alphabet.charAt(index)
            );
        }

        return stringBuilder.toString();
    }

    private WeatherGovData makeFakeWeatherGovData(Calendar calendar) {
        ThreadLocalRandom localRandom = ThreadLocalRandom.current();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        Date start = getStartDate(calendar);
        Date end = getEndDate(calendar);
        double temperature = localRandom.nextDouble(-10, 90);
        String unit = "F";
        double humidity = localRandom.nextDouble(0, 100);
        String windSpeed = localRandom.nextInt(0, 40) + " MPH";
        String windDirection = String.valueOf(
            directions.charAt(
                localRandom.nextInt(directions.length())
            )
        );

        return new WeatherGovData(
            dateFormat.format(start),
            dateFormat.format(end),
            temperature,
            unit,
            humidity,
            windSpeed,
            windDirection
        );
    }

    private Date getStartDate(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    private Date getEndDate(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }
}
