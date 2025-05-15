package eu.deschler.dapro.Exception;

public class DriverLicenseNotValidException extends RuntimeException {
    public DriverLicenseNotValidException(String message) {
        super(message);
    }
}
