package dev.kaldiroglu.dp.structural.facade.hw.checkout;

/**
 * What the facade hands back.
 * <p>
 * A single object rather than four, because a client that has to assemble the outcome from
 * four subsystem return values has not really been simplified.
 */
public record OrderResult(boolean placed, String paymentReference,
                          String shipmentReference, String failure) {

    public static OrderResult success(String payment, String shipment) {
        return new OrderResult(true, payment, shipment, null);
    }

    public static OrderResult failed(String reason) {
        return new OrderResult(false, null, null, reason);
    }
}
