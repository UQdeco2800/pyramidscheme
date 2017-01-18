package uq.deco2800.pyramidscheme.network;

/**
 * Thrown when a login is unsuccessful due to invalid credentials.
 */
public class LoginException extends Exception {
    public LoginException(final Throwable cause) {
        super(cause);
    }
}
