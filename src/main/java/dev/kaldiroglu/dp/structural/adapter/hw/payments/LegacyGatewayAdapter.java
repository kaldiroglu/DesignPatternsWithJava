package dev.kaldiroglu.dp.structural.adapter.hw.payments;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * The Adapter — and the exercise is that most of it is <em>not</em> renaming methods.
 * <p>
 * Three separate mismatches have to be reconciled, and only the first is what people picture
 * when they hear "adapter":
 * <ol>
 *   <li><strong>Names.</strong> {@code submitTxn} becomes {@code charge}. Trivial.</li>
 *   <li><strong>Data.</strong> {@code BigDecimal} lira become integer cents, and the rounding
 *       rule is a decision somebody has to make and write down.</li>
 *   <li><strong>Error model.</strong> Integer codes become exceptions. This is the one that
 *       carries risk: a code that is not translated is a failure that silently becomes a
 *       success.</li>
 * </ol>
 * The out-parameter array is a fourth mismatch of a kind that has no name — an idiom from a
 * language without multiple return values. The adapter is where it stops.
 */
public class LegacyGatewayAdapter implements PaymentProcessor {

    private final LegacyGateway gateway;

    public LegacyGatewayAdapter(LegacyGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public String charge(String customerId, BigDecimal amount) {
        String[] out = new String[2];
        gateway.submitTxn(customerId, toCents(amount), out);

        int code = Integer.parseInt(out[0]);
        if (code != LegacyGateway.OK) {
            throw new PaymentDeclinedException(describe(code), code);
        }
        return out[1];
    }

    @Override
    public void refund(String reference, BigDecimal amount) {
        int code = gateway.reverseTxn(reference, toCents(amount));
        if (code != LegacyGateway.OK) {
            throw new PaymentDeclinedException(describe(code), code);
        }
    }

    /** Half-up to the nearest cent — a decision, made once, in the one place that can. */
    private static long toCents(BigDecimal amount) {
        return amount.setScale(2, RoundingMode.HALF_UP).movePointRight(2).longValueExact();
    }

    /**
     * Every code the gateway can return has a case here.
     * <p>
     * The default matters more than the others: an unrecognized code must still be a failure.
     * Translating only the codes you have seen is how a decline becomes a delivered order.
     */
    private static String describe(int code) {
        return switch (code) {
            case LegacyGateway.INSUFFICIENT_FUNDS -> "insufficient funds";
            case LegacyGateway.CARD_EXPIRED -> "card expired";
            case LegacyGateway.UNKNOWN_CUSTOMER -> "unknown customer";
            default -> "declined by the gateway, code " + code;
        };
    }
}
