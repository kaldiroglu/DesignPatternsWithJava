package dev.kaldiroglu.dp.structural.bridge.hw.paymentdesk;

import java.math.BigDecimal;

/**
 * The Implementor: the primitives every way of taking money must support.
 * <p>
 * <strong>The cash drawer is the whole exercise.</strong> A bank gateway and a wallet both
 * authorize first and capture later; cash does neither, because the money is in the drawer or
 * it is not. There are three defensible answers and this file takes the third:
 * <ol>
 *   <li><em>Widen</em> the interface with {@code supportsTwoPhase()} and let the abstraction
 *       branch on it. Rejected: that is a boolean asking "which implementation are you?", and
 *       every branch on it is a piece of the abstraction that now knows about providers.</li>
 *   <li><em>Split</em> into two implementor interfaces and let the abstraction discover which
 *       it holds. Rejected for the same reason, wearing a type instead of a boolean.</li>
 *   <li><em>Keep the two-phase shape and let cash collapse it.</em> Taken here.
 *       {@link CashDrawer#authorize} returns an authorization that is already
 *       {@code settled}, and its capture is a no-op that hands back the receipt. Every
 *       provider answers every primitive honestly, and no caller branches.</li>
 * </ol>
 * The cost of the choice is real and should be said out loud: an authorization from the cash
 * drawer cannot be voided, because the money already moved. That is a property of cash, not a
 * flaw in the design — but it means {@code void} is a primitive we deliberately did not add,
 * since only two of the three providers could implement it.
 */
public interface PaymentProvider {

    String name();

    Authorization authorize(BigDecimal amount);

    Receipt capture(Authorization authorization);

    Receipt refund(BigDecimal amount, String reference);
}
