package weather.records;

public record ForecastRequest(
        int daysOut,
        WeatherLocation location,
        WeatherUnits units
) {}
