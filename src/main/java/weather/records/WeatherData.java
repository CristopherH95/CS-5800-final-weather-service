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
) {
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        String labelTemperature = "Temp.";
        String labelWind = "Wind";
        String labelHumidity = "Humidity";
        String temperatureDisplay = getHumanTemperature();
        String windDisplay = getHumanWind();
        String humidityDisplay = getHumanHumidity();

        stringBuilder.append(String.format("[%s to %s]%n", start, end));
        stringBuilder.append(String.format("[source: %s]%n", source.name()));
        stringBuilder.append(String.format("%-20s %-25s %-20s%n", labelTemperature, labelWind, labelHumidity));
        stringBuilder.append(String.format("%s%n", "=".repeat(65)));
        stringBuilder.append(String.format("%-20s %-25s %-20s%n", temperatureDisplay, windDisplay, humidityDisplay));
        stringBuilder.append(String.format("%s%n", "=".repeat(65)));

        return stringBuilder.toString();
    }

    private String getHumanTemperature() {
        return String.format("%.2f", temperatureAverage) + " " + temperatureUnit.getShortLabel();
    }

    private String getHumanWind() {
        return windSpeed + " " + windSpeedUnit.name();
    }

    private String getHumanHumidity() {
        if (humidityPercentage < 0) {
            return "N/A";
        }
        return String.format("%%%.0f", humidityPercentage);
    }
}
