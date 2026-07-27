package dev.kaldiroglu.dp.structural.flyweight.quote.problem;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Every quote carries a full copy of the instrument metadata. Receive a billion
 * events and you allocate a billion of these strings.
 */
public class Quote {

    private final String symbol;
    private final String exchange;
    private final String currency;
    private final String isin;
    private final String sector;
    private final String companyName;
    private final BigDecimal tickSize;
    private final int lotSize;

    private final BigDecimal bid;
    private final BigDecimal ask;
    private final BigDecimal last;
    private final long volume;
    private final Instant timestamp;

    public Quote(String symbol, String exchange, String currency, String isin,
                 String sector, String companyName, BigDecimal tickSize, int lotSize,
                 BigDecimal bid, BigDecimal ask, BigDecimal last, long volume, Instant timestamp) {
        this.symbol = symbol;
        this.exchange = exchange;
        this.currency = currency;
        this.isin = isin;
        this.sector = sector;
        this.companyName = companyName;
        this.tickSize = tickSize;
        this.lotSize = lotSize;
        this.bid = bid;
        this.ask = ask;
        this.last = last;
        this.volume = volume;
        this.timestamp = timestamp;
    }

    public String symbol() { return symbol; }
    public BigDecimal last() { return last; }
}
