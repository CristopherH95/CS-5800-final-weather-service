package weather.records;

public record WeatherLocation(
        double latitude,
        double longitude,
        String timezone
) {}
