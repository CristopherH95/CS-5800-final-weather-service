package weather.records;

import java.util.Map;

// Basic data structure representing the raw data sent by Open-Meteo endpoint
public record OpenMeteoData(
    Map<String, String> daily_units,
    Iterable<String> time,
    Iterable<Double> temperature_2m_max,
    Iterable<Double> temperature_2m_min,
    Iterable<Double> windspeed_10m_max,
    Iterable<Double> windgusts_10m_max,
    Iterable<Double> winddirection
) {}
