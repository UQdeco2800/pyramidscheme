package uq.deco2800.pyramidscheme.network;

/**
 * Thrown when there is an error connecting to a remote endpoint.
 */
public class NetworkException extends Exception {
    public NetworkException(Throwable cause) {
        super(cause);
    }
}
