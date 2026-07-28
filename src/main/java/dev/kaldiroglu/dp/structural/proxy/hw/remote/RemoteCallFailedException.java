package dev.kaldiroglu.dp.structural.proxy.hw.remote;

/** What the network adds to the interface, whether the interface admits it or not. */
public class RemoteCallFailedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RemoteCallFailedException(String message) {
        super(message);
    }
}
