package exceptions;

// A generic exception for invalid data.
public class InvalidDataException extends RuntimeException {
    public InvalidDataException(String message) {
        super(message);
    }
}