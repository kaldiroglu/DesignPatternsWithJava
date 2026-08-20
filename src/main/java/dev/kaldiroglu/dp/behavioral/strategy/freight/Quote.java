package dev.kaldiroglu.dp.behavioral.strategy.freight;

/**
 * What one carrier would charge for one shipment.
 *
 * @param carrier who quoted
 * @param price   what they want for it
 */
public record Quote(String carrier, Money price) {

    @Override
    public String toString() {
        return "%-10s %8s".formatted(carrier, price);
    }
}
