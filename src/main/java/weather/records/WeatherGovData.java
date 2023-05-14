package weather.records;

// Basic data structure representing the raw data sent by Weather-Gov endpoint
public record WeatherGovData(
    String startTime,
    String endTime,
    double temperature,
    String temperatureUnit,
    double relativeHumidity,
    String windSpeed,
    String windDirection
) {}
