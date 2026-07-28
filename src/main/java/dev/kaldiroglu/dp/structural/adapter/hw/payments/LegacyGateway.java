package dev.kaldiroglu.dp.structural.adapter.hw.payments;

/**
 * The Adaptee: a gateway from 2004 that we cannot change and would not want to rewrite.
 * <p>
 * Everything about it is inconvenient and none of it is wrong for its time. Amounts are
 * <em>integer cents</em>, because floating point money was already known to be a mistake.
 * Failure is an <em>integer code</em>, because it predates the convention that exceptions are
 * for exceptional things. The method names describe the wire protocol, not the intent.
 */
public class LegacyGateway {

    public static final int OK = 0;
    public static final int INSUFFICIENT_FUNDS = 51;
    public static final int CARD_EXPIRED = 54;
    public static final int UNKNOWN_CUSTOMER = 14;

    private int nextReference = 1000;
    private int callCount;

    /**
     * Submits a transaction.
     *
     * @param amountInCents the amount, in cents
     * @param out           a two-element array: {@code out[0]} receives the status code and
     *                      {@code out[1]} the reference. Yes, really.
     */
    public void submitTxn(String custId, long amountInCents, String[] out) {
        callCount++;
        if (custId == null || custId.isBlank()) {
            out[0] = String.valueOf(UNKNOWN_CUSTOMER);
            return;
        }
        if (amountInCents > 100_000L) {
            out[0] = String.valueOf(INSUFFICIENT_FUNDS);
            return;
        }
        out[0] = String.valueOf(OK);
        out[1] = "TXN-" + (nextReference++);
    }

    public int reverseTxn(String reference, long amountInCents) {
        callCount++;
        return reference != null && reference.startsWith("TXN-") ? OK : UNKNOWN_CUSTOMER;
    }

    public int callCount() {
        return callCount;
    }
}
