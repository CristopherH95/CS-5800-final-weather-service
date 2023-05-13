package weather.exceptions;

public class InvalidWeatherAPIRequestException extends RuntimeException {
    public InvalidWeatherAPIRequestException(String message) {
        super(message);
    }
}
