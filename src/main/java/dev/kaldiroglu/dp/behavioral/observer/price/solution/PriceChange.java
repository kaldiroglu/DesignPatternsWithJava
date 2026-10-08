package dev.kaldiroglu.dp.behavioral.observer.price.solution;

/** The event: what changed, from what, to what. Sent to every listener. */
public record PriceChange(String symbol, int oldPrice, int newPrice) {
}
