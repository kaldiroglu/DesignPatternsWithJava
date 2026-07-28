package dev.kaldiroglu.dp.structural.flyweight.quote.solution;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Objects;

/**
 * Intrinsic flyweight state. Immutable. Shared across every Quote that
 * references this instrument. Construction is package-private so only
 * InstrumentRegistry can mint instances — guarantees identity equality.
 */
public final class Instrument {

    private final String symbol;
    private final String exchange;
    private final String currency;
    private final String isin;
    private final String sector;
    private final String companyName;
    private final BigDecimal tickSize;
    private final int lotSize;
    private final LocalTime sessionOpen;
    private final LocalTime sessionClose;

    Instrument(String symbol, String exchange, String currency, String isin,
               String sector, String companyName, BigDecimal tickSize, int lotSize,
               LocalTime sessionOpen, LocalTime sessionClose) {
        this.symbol = Objects.requireNonNull(symbol);
        this.exchange = Objects.requireNonNull(exchange);
        this.currency = Objects.requireNonNull(currency);
        this.isin = Objects.requireNonNull(isin);
        this.sector = Objects.requireNonNull(sector);
        this.companyName = Objects.requireNonNull(companyName);
        this.tickSize = Objects.requireNonNull(tickSize);
        this.lotSize = lotSize;
        this.sessionOpen = sessionOpen;
        this.sessionClose = sessionClose;
    }

    public String symbol() { return symbol; }
    public String exchange() { return exchange; }
    public String currency() { return currency; }
    public String isin() { return isin; }
    public String sector() { return sector; }
    public String companyName() { return companyName; }
    public BigDecimal tickSize() { return tickSize; }
    public int lotSize() { return lotSize; }
    public LocalTime sessionOpen() { return sessionOpen; }
    public LocalTime sessionClose() { return sessionClose; }

    /**
     * Whether two definitions of the same symbol agree on every field.
     *
     * <p>Deliberately not {@code equals}. Instruments are compared by identity everywhere
     * else — that is the whole point of interning them — and giving the class a value
     * {@code equals} would invite callers to stop caring whether sharing actually happened.
     * This method exists for exactly one caller: {@link InstrumentRegistry#define}, checking
     * that a redefinition is harmless.</p>
     */
    boolean hasSameDefinitionAs(Instrument other) {
        return symbol.equals(other.symbol)
                && exchange.equals(other.exchange)
                && currency.equals(other.currency)
                && isin.equals(other.isin)
                && sector.equals(other.sector)
                && companyName.equals(other.companyName)
                && tickSize.compareTo(other.tickSize) == 0
                && lotSize == other.lotSize
                && Objects.equals(sessionOpen, other.sessionOpen)
                && Objects.equals(sessionClose, other.sessionClose);
    }
}
