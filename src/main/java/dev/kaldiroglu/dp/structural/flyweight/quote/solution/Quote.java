package dev.kaldiroglu.dp.structural.flyweight.quote.solution;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Extrinsic flyweight state + reference to the shared Instrument.
 * No copies of symbol, exchange, etc. are kept on the per-event object.
 */
public record Quote(
    Instrument instrument,
    BigDecimal bid,
    BigDecimal ask,
    BigDecimal last,
    long volume,
    Instant timestamp
) {}
