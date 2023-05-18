package driver;

import weather.enums.SpeedUnit;
import weather.enums.TemperatureUnit;
import weather.interfaces.WeatherServiceStrategy;
import weather.records.ForecastRequest;
import weather.records.WeatherLocation;
import weather.records.WeatherUnits;
import weather.services.OpenMeteoService;
import weather.services.WeatherAPIService;
import weather.services.WeatherGovService;
import weather.user.WeatherServiceClient;

public class Main {
    public static void main(String[] args) {
        testOpenMeteoService();
        testWeatherAPIService();
        testWeatherGovService();
    }

    private static void testOpenMeteoService() {
        WeatherServiceClient client = new WeatherServiceClient("OpenMeteo Client");
        OpenMeteoService openMeteoService = new OpenMeteoService();
        openMeteoService.addPropertyChangeListener(client);
        testForecastImperial(client, openMeteoService);
        openMeteoService.removePropertyChangeListener(client);
    }

    private static void testWeatherAPIService() {
        WeatherServiceClient client = new WeatherServiceClient("WeatherAPI Client");
        WeatherAPIService weatherAPIService = new WeatherAPIService();
        weatherAPIService.addPropertyChangeListener(client);
        testForecastMetric(client, weatherAPIService);
        weatherAPIService.removePropertyChangeListener(client);
    }

    private static void testWeatherGovService() {
        WeatherServiceClient client = new WeatherServiceClient("WeatherGov Client");
        WeatherGovService weatherGovService = new WeatherGovService();
        weatherGovService.addPropertyChangeListener(client);
        testForecastMetric(client, weatherGovService);
        weatherGovService.removePropertyChangeListener(client);
    }

    private static void testForecastImperial(WeatherServiceClient client, WeatherServiceStrategy strategy) {
        System.out.println("----- PULLING WEATHER FORECAST -----");
        WeatherLocation location = new WeatherLocation(
            34.0522,
            118.2437,
            "America/Los_Angeles"
        );
        System.out.printf("Latitude: %f%n", location.latitude());
        System.out.printf("Longitude: %f%n", location.longitude());
        System.out.printf("Timezone: %s%n", location.timezone());
        WeatherUnits weatherUnits = new WeatherUnits(TemperatureUnit.FAHRENHEIT, SpeedUnit.MPH);
        System.out.printf("Temperature unit: %s%n", weatherUnits.temperatureUnit().name());
        System.out.printf("Speed unit: %s%n", weatherUnits.speedUnit().name());
        ForecastRequest forecastRequest = new ForecastRequest(3, location, weatherUnits);
        strategy.getForecast(forecastRequest);
        client.printFeed();
        System.out.println("----- FORECAST DONE -----");
    }

    private static void testForecastMetric(WeatherServiceClient client, WeatherServiceStrategy strategy) {
        System.out.println("----- PULLING WEATHER FORECAST -----");
        WeatherLocation location = new WeatherLocation(
                51.5072,
                0.1276,
                "Europe/Isle_of_Man"
        );
        System.out.printf("Latitude: %f%n", location.latitude());
        System.out.printf("Longitude: %f%n", location.longitude());
        System.out.printf("Timezone: %s%n", location.timezone());
        WeatherUnits weatherUnits = new WeatherUnits(TemperatureUnit.CELSIUS, SpeedUnit.KPH);
        System.out.printf("Temperature unit: %s%n", weatherUnits.temperatureUnit().name());
        System.out.printf("Speed unit: %s%n", weatherUnits.speedUnit().name());
        ForecastRequest forecastRequest = new ForecastRequest(3, location, weatherUnits);
        strategy.getForecast(forecastRequest);
        client.printFeed();
        System.out.println("----- FORECAST DONE -----");
    }
}