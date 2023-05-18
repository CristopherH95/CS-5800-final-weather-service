package weather.enums;

public enum TemperatureUnit {
    FAHRENHEIT("F"),
    CELSIUS("C");

    private final String shortLabel;

    private TemperatureUnit(String shortLabel) {
        this.shortLabel = shortLabel;
    }

    public String getShortLabel() {
        return shortLabel;
    }
}
