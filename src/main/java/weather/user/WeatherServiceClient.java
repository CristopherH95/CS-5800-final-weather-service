package weather.user;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class WeatherServiceClient implements PropertyChangeListener {
    private String name;
    // TODO: replace 'Object' with 'WeatherData'
    private final List<Object> dataFeed;

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

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        dataFeed.add(evt.getNewValue());
    }
}
