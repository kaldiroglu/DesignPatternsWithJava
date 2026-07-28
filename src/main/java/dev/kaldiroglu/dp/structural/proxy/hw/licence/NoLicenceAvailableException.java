package dev.kaldiroglu.dp.structural.proxy.hw.licence;

/**
 * Thrown when every seat is taken.
 *
 * <p>A refusal, made loudly. The alternative — returning a half-working application, or
 * blocking the caller forever — is the failure mode this exception exists to prevent.</p>
 */
public class NoLicenceAvailableException extends RuntimeException {

    private final int position;

    public NoLicenceAvailableException(String message, int position) {
        super(message);
        this.position = position;
    }

    /** Where the refused student now stands in the queue, 1-based. */
    public int position() {
        return position;
    }
}
