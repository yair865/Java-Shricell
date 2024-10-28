package engine.exception;

public class OutdatedVersionException extends RuntimeException {

    public OutdatedVersionException(String message) {
        super(message);
    }
}
