package dev.kaldiroglu.dp.structural.decorator.middleware.solution.classic;

import dev.kaldiroglu.dp.structural.decorator.middleware.domain.PriceFeed;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.Quote;

import java.util.Objects;

/**
 * The Decorator role: a price feed that <em>is</em> a {@link PriceFeed} and <em>has</em> a
 * {@link PriceFeed}, and by default does nothing but forward.
 * <p>
 * Compare it with {@code problem.BasicPriceFeed}, which looks almost the same. The
 * difference is one word — {@code extends} versus a field — and it is the difference
 * between a hierarchy that has to anticipate every combination and a chain that is built
 * when the program runs.
 * <p>
 * GoF's implementation issue 2 (p. 179) notes that the abstract decorator can be omitted
 * when there is only one responsibility to add. There are five here, so it earns its keep.
 */
public abstract class PriceFeedDecorator implements PriceFeed {

    private final PriceFeed inner;

    protected PriceFeedDecorator(PriceFeed inner) {
        this.inner = Objects.requireNonNull(inner, "a decorator must decorate something");
    }

    /** The next feed in the chain. Every decorator ends up calling this, or deciding not to. */
    protected final PriceFeed inner() {
        return inner;
    }

    @Override
    public Quote quoteFor(String sku) {
        return inner.quoteFor(sku);
    }
}
