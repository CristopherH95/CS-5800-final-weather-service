package weather.services;

import weather.endpoints.OpenMeteoClient;
import weather.enums.DataSource;
import weather.enums.SpeedUnit;
import weather.enums.TemperatureUnit;
import weather.interfaces.WeatherServiceStrategy;
import weather.records.ForecastRequest;
import weather.records.OpenMeteoData;
import weather.records.WeatherData;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

public class OpenMeteoService implements WeatherServiceStrategy {
    PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    OpenMeteoClient apiClient = new OpenMeteoClient();
    Iterable<WeatherData> data;

    @Override
    public void getForecast(ForecastRequest request) {
        OpenMeteoData APIData = apiClient.requestData(request.location().longitude(), request.location().latitude());
        updateData(normalizeOpenMeteoData(APIData, request));
    }

    private Iterable<WeatherData> normalizeOpenMeteoData(OpenMeteoData data, ForecastRequest request) {
        return convertDataItem(data, request);
    }

    private Iterable<WeatherData> convertDataItem(OpenMeteoData item, ForecastRequest request) {
        TemperatureUnit sourceTempUnit = TemperatureUnit.CELSIUS;
        SpeedUnit sourceSpeedUnit = SpeedUnit.KPH;

        List<String> dates = iterableToList(item.time());
        List<Double> maxTemperatures = iterableToList(item.temperature_2m_max());
        List<Double> minTemperatures = iterableToList(item.temperature_2m_min());
        List<Double> windSpeeds = iterableToList(item.windspeed_10m_max()).stream().map(speed -> convertSpeed(speed, sourceSpeedUnit, request.units().speedUnit())).collect(Collectors.toList());
        List<Double> windDirections = iterableToList(item.winddirection());

        List<Double> avgTemperatures = IntStream.range(0, maxTemperatures.size()).mapToObj(i -> (maxTemperatures.get(i) + minTemperatures.get(i)) / 2).map(temp -> convertTemperature(temp, sourceTempUnit, request.units().temperatureUnit())).toList();

        double humidityPercentage = -100.0;

        List<Date> startDates = new ArrayList<>();
        List<Date> endDates = new ArrayList<>();

        for(String stringDate : dates) {
            Date start, end;
            try {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                start = dateFormat.parse(stringDate.concat(" 00:00:00"));
                end = dateFormat.parse(stringDate.concat(" 23:59:59"));
            } catch (ParseException e) {
                System.out.println(e.getMessage());
                start = new Date();
                end = new Date();
            }

            startDates.add(start);
            endDates.add(end);
        }

        ArrayList<WeatherData> weatherData = new ArrayList<>();

        for(int i = 0; i < dates.size(); i++) {
            weatherData.add(new WeatherData(avgTemperatures.get(i), request.units().temperatureUnit(), String.valueOf(windSpeeds.get(i)), request.units().speedUnit(), String.valueOf(windDirections.get(i)), humidityPercentage, startDates.get(i), endDates.get(i), DataSource.OPEN_METEO));
        }

        return weatherData;
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

    private double convertSpeed(double value, SpeedUnit source, SpeedUnit target) {
        if(source == target)
            return value;

        if(source == SpeedUnit.MPH && target == SpeedUnit.KPH)
            return mphToKPH(value);
        else
            return kphToMPH(value);
    }

    private double mphToKPH(double mph) {
        return mph * 1.60934;
    }

    private double kphToMPH(double kph) {
        return kph * 0.62137;
    }

    private <E> List<E> iterableToList(Iterable<E> input) {
        return StreamSupport.stream(input.spliterator(), false).collect(Collectors.toList());
    }
}
