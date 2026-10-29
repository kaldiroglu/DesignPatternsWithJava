package dev.kaldiroglu.dp.introduction.problems.pricing;

import dev.kaldiroglu.dp.introduction.problems.pricing.domain.Basket;
import dev.kaldiroglu.dp.introduction.problems.pricing.domain.Customer;
import dev.kaldiroglu.dp.introduction.problems.pricing.domain.Line;
import dev.kaldiroglu.dp.introduction.problems.pricing.domain.Money;

/** Ceyda's basket: three books at 400 lira, and she is a student. Every stage prices it. */
final class SampleBasket {

    private SampleBasket() {
    }

    static Basket build() {
        return Basket.of(Customer.student("Ceyda"),
                new Line("java-book", "book", Money.of("400.00"), 3));
    }
}
