package weather.records;

// Basic data structure representing the raw data sent by Weather-API endpoint
public record WeatherAPIData(
    double avgtemp_c,
    double avgtemp_f,
    double maxwind_mph,
    double maxwind_kph,
    double avghumidity,
    String date
) {}
