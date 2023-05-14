package weather.records;

import weather.enums.DataSource;
import weather.enums.SpeedUnit;
import weather.enums.TemperatureUnit;

import java.util.Date;

public record WeatherData(
        double temperatureAverage,
        TemperatureUnit temperatureUnit,
        String windSpeed,
        SpeedUnit windSpeedUnit,
        String windDirection,
        double humidityPercentage,
        Date start,
        Date end,
        DataSource source
) {}
