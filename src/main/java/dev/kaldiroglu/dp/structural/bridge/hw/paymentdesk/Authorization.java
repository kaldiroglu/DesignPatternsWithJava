package dev.kaldiroglu.dp.structural.bridge.hw.paymentdesk;

import java.math.BigDecimal;

/**
 * A hold placed on money that has not moved yet.
 *
 * @param reference what the provider calls it
 * @param amount    the amount held
 * @param settled   true when the money has already moved, so capture has nothing left to do
 */
public record Authorization(String reference, BigDecimal amount, boolean settled) { }
