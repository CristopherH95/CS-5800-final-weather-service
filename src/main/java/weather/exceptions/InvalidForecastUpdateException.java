package weather.exceptions;

public class InvalidForecastUpdateException extends RuntimeException {
    public InvalidForecastUpdateException(String message) {
        super(message);
    }

    public InvalidForecastUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
