package weather.services;

import weather.interfaces.WeatherServiceStrategy;
import weather.endpoints.WeatherAPIClient;
import weather.records.*;
import weather.enums.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WeatherAPIService implements WeatherServiceStrategy {
    PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    WeatherAPIClient apiClient = new WeatherAPIClient();
    Iterable<WeatherData> data;

    @Override
    public void getForecast(ForecastRequest request) {
        Iterable<WeatherAPIData> APIData = apiClient.requestData("test", request.daysOut());
        updateData(normalizeWeatherData(APIData, request));
    }

    private Iterable<WeatherData> normalizeWeatherData(Iterable<WeatherAPIData> data, ForecastRequest request) {
        ArrayList<WeatherData> weatherData = new ArrayList<>();

        for(WeatherAPIData weatherAPIData : data) {
           weatherData.add(convertDataItem(weatherAPIData, request));
        }

        return weatherData;
    }

    private WeatherData convertDataItem(WeatherAPIData item, ForecastRequest request) {
        double temperatureAverage = request.units().temperatureUnit() == TemperatureUnit.FAHRENHEIT ? item.avgtemp_f() : item.avgtemp_c();
        TemperatureUnit temperatureUnit = request.units().temperatureUnit();

        String windSpeed = request.units().speedUnit() == SpeedUnit.MPH ? String.valueOf(item.maxwind_mph()) : String.valueOf(item.maxwind_kph());
        SpeedUnit windSpeedUnit = request.units().speedUnit();
        String windDirection = "N/A";

        double humidityPercentage = item.avghumidity();

        Date start, end;
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            start = dateFormat.parse(item.date().concat(" 00:00:00"));
            end = dateFormat.parse(item.date().concat(" 23:59:59"));
        }
        catch(ParseException e) {
            System.out.println(e.getMessage());
            start = new Date();
            end = new Date();
        }

        DataSource source = DataSource.WEATHER_API;

        return new WeatherData(temperatureAverage, temperatureUnit, windSpeed, windSpeedUnit, windDirection, humidityPercentage, start, end, source);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    public void updateData(Iterable<WeatherData> data) {
        propertyChangeSupport.firePropertyChange("data", this.data, data);
        this.data = data;
    }
}
