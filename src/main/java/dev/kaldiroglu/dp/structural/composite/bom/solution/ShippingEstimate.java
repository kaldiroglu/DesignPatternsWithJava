package dev.kaldiroglu.dp.structural.composite.bom.solution;

import dev.kaldiroglu.dp.structural.composite.bom.domain.Money;

/**
 * A second, unrelated client. It was written against {@link BomComponent} and therefore
 * works for a whole bicycle and for a single spoke, with no changes and no type tests.
 */
final class ShippingEstimate {

    private ShippingEstimate() {
    }

    static Money of(BomComponent component) {
        int kilos = Math.max(1, (component.totalWeightGrams() + 999) / 1000);
        return Money.of(4.90).times(kilos);
    }
}
