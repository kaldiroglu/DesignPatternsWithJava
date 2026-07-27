package dev.kaldiroglu.dp.structural.flyweight.quote.solution;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class FeedHandler {

    private final List<Quote> quotes = new ArrayList<>();
    private final InstrumentRegistry registry;

    public FeedHandler(InstrumentRegistry registry) {
        this.registry = registry;
    }

    public void onTick(Instrument instrument, BigDecimal bid, BigDecimal ask,
                       BigDecimal last, long volume) {
        quotes.add(new Quote(instrument, bid, ask, last, volume, Instant.now()));
    }

    public int total() { return quotes.size(); }
    public int distinctInstruments() { return registry.size(); }
}
