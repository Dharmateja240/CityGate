package backend;

/**
 * Exception class for authentication-related errors
 */
public class AuthException extends Exception {
    public AuthException(String message) {
        super(message);
    }
}