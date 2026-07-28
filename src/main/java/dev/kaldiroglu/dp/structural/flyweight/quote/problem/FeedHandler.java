package dev.kaldiroglu.dp.structural.flyweight.quote.problem;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Ingests ticks and keeps them.
 *
 * <p>Thirteen arguments, eight of which describe the instrument rather than the tick. Every
 * one of them is copied onto every event and then held for as long as the event is held.</p>
 */
public class FeedHandler {

    private final List<Quote> quotes = new ArrayList<>();

    public void onTick(String symbol, String exchange, String currency, String isin,
                       String sector, String companyName, BigDecimal tickSize, int lotSize,
                       BigDecimal bid, BigDecimal ask, BigDecimal last, long volume) {
        quotes.add(new Quote(symbol, exchange, currency, isin, sector, companyName,
                tickSize, lotSize, bid, ask, last, volume, Instant.now()));
    }

    public int total() {
        return quotes.size();
    }

    public List<Quote> quotes() {
        return List.copyOf(quotes);
    }
}
