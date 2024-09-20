package exceptions;

// A generic "Not Found" exception for any entity.
public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}