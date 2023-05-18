package weather.user;

import weather.exceptions.InvalidForecastUpdateException;
import weather.records.WeatherData;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class WeatherServiceClient implements PropertyChangeListener {
    private String name;
    private final List<WeatherData> dataFeed;

    public WeatherServiceClient(String name) {
        this.name = name;
        this.dataFeed = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void printFeed() {
        System.out.printf("CLIENT '%s' DATA FEED:%n", name);

        for (Object weatherData : dataFeed) {
            System.out.println(weatherData);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Object updateData = evt.getNewValue();
        if (!(updateData instanceof Iterable)) {
            throw new InvalidForecastUpdateException("Weather forecast data must be iterable for clients to consume");
        }
        try {
            Iterable<WeatherData> weatherUpdateData = (Iterable<WeatherData>) updateData;
            for (WeatherData data : weatherUpdateData) {
                dataFeed.add(data);
            }
        } catch (ClassCastException e) {
            throw new InvalidForecastUpdateException("Weather forecast update must be of type WeatherData", e);
        }
    }
}
