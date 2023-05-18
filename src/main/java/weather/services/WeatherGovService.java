package weather.services;

import weather.interfaces.WeatherServiceStrategy;
import weather.endpoints.WeatherGovClient;
import weather.records.*;
import weather.enums.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.StreamSupport;

public class WeatherGovService implements WeatherServiceStrategy {
    PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    WeatherGovClient apiClient = new WeatherGovClient();

    @Override
    public void getForecast(ForecastRequest request) {
        Iterable<WeatherGovData> APIData = apiClient.requestData(request.location().longitude(), request.location().latitude());
        updateData(normalizeWeatherGovData(APIData, request));
    }

    private Iterable<WeatherData> normalizeWeatherGovData(Iterable<WeatherGovData> data, ForecastRequest request) {
        List<WeatherGovData> govData = StreamSupport.stream(data.spliterator(), false).toList();
        ArrayList<WeatherData> weatherData = new ArrayList<>();
        int forecastSize = Math.min(govData.size(), request.daysOut());

        for(int i = 0; i < forecastSize; i++) {
            weatherData.add(convertDataItem(govData.get(i), request, i));
        }

        return weatherData;
    }

    private WeatherData convertDataItem(WeatherGovData item, ForecastRequest request, int index) {
        TemperatureUnit sourceTemperatureUnit = item.temperatureUnit().toLowerCase().contains("f") ? TemperatureUnit.FAHRENHEIT : TemperatureUnit.CELSIUS;
        TemperatureUnit targetTemperatureUnit = request.units().temperatureUnit();
        double temperatureAverage = convertTemperature(
            item.temperature(),
            sourceTemperatureUnit,
            targetTemperatureUnit
        );

        SpeedUnit targetSpeedUnit = request.units().speedUnit();
        double windSpeed = extractWindSpeed(item.windSpeed(), targetSpeedUnit);
        String windDirection = item.windDirection();

        double humidityPercentage = item.relativeHumidity();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, index);
        Date start = getStartDate(calendar);
        Date end = getEndDate(calendar);

        DataSource source = DataSource.WEATHER_GOV;

        return new WeatherData(
            temperatureAverage,
            targetTemperatureUnit,
            String.valueOf(windSpeed),
            targetSpeedUnit,
            windDirection,
            humidityPercentage,
            start,
            end,
            source
        );
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    public void updateData(Iterable<WeatherData> data) {
        propertyChangeSupport.firePropertyChange("data", null, data);
    }

    private double convertTemperature(double value, TemperatureUnit source, TemperatureUnit target) {
        if(source == target)
            return value;

        if(source == TemperatureUnit.FAHRENHEIT && target == TemperatureUnit.CELSIUS)
            return fahrenheitToCelsius(value);
        else
            return celsiusToFahrenheit(value);
    }

    private double fahrenheitToCelsius(double fahrenheit) {
        return (fahrenheit - 32) / 1.8;
    }

    private double celsiusToFahrenheit(double celsius) {
        return (celsius + 32) * 1.8;
    }

    private double extractWindSpeed(String speedText, SpeedUnit targetUnit) {
        String digitsOnly = speedText.replaceAll("[^0-9]", "");
        double windSpeed = Double.parseDouble(digitsOnly);
        if (targetUnit == SpeedUnit.MPH) {
            return windSpeed;
        }

        return windSpeed * 1.60934;
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
