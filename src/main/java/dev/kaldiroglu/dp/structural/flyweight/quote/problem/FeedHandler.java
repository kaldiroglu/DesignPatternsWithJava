package dev.kaldiroglu.dp.structural.flyweight.quote.problem;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class FeedHandler {

    private final List<Quote> quotes = new ArrayList<>();

    public void onTick(String symbol, String exchange, String currency, String isin,
                       String sector, String companyName, BigDecimal tickSize, int lotSize,
                       BigDecimal bid, BigDecimal ask, BigDecimal last, long volume) {
        quotes.add(new Quote(symbol, exchange, currency, isin, sector, companyName,
            tickSize, lotSize, bid, ask, last, volume, Instant.now()));
    }

    public int total() { return quotes.size(); }
}
