package weather.records;

import weather.enums.SpeedUnit;
import weather.enums.TemperatureUnit;

public record WeatherUnits(
        TemperatureUnit temperatureUnit,
        SpeedUnit speedUnit
) {}
