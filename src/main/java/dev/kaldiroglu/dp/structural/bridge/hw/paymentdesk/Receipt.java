package dev.kaldiroglu.dp.structural.bridge.hw.paymentdesk;

import java.math.BigDecimal;

/** Proof that money moved. */
public record Receipt(String reference, BigDecimal amount, String provider) { }
