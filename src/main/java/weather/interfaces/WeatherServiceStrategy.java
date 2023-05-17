package weather.interfaces;

import weather.records.ForecastRequest;
import weather.records.WeatherData;

public interface WeatherServiceStrategy {
    public void getForecast(ForecastRequest request);
}
