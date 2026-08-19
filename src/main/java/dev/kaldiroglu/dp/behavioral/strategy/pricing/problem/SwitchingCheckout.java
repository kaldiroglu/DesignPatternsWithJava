package dev.kaldiroglu.dp.behavioral.strategy.pricing.problem;

import dev.kaldiroglu.dp.behavioral.strategy.pricing.domain.Basket;
import dev.kaldiroglu.dp.behavioral.strategy.pricing.domain.Line;
import dev.kaldiroglu.dp.behavioral.strategy.pricing.domain.Money;
import dev.kaldiroglu.dp.behavioral.strategy.pricing.domain.Receipt;

/**
 * Stage one: one method, and a branch per campaign.
 * <p>
 * This is what the till looks like after the third campaign and before anybody has time to
 * think. It works. Every price it produces is correct, and for one or two campaigns it is
 * the clearest thing in the file.
 *
 * <h2>What it costs</h2>
 * <ul>
 *   <li><b>Every campaign is a branch, and the branches share a method.</b> Adding the
 *       staff discount means editing a method that already works for four other campaigns.
 *       The compiler cannot help: the campaign arrives as a {@code String}.</li>
 *   <li><b>No rule can be tested on its own.</b> To check the Black Friday tier you must
 *       build a basket, call the till, and hope nothing above the branch interfered.</li>
 *   <li><b>The rules leak.</b> Look how many times the 1000-lira threshold appears below,
 *       and note that {@code STUDENT} silently caps at a different figure that nobody has
 *       written down anywhere else.</li>
 * </ul>
 */
public final class SwitchingCheckout {

    public Receipt ring(Basket basket, String campaign) {
        Money list = basket.listTotal();
        Money paid = switch (campaign) {
            case "NONE" -> list;
            case "STUDENT" -> basket.customer().student() ? list.percentOff(20) : list;
            case "STAFF" -> basket.customer().staff() ? list.percentOff(30) : list;
            case "BLACKFRIDAY" -> list.isAtLeast(Money.of("1000.00"))
                    ? list.percentOff(40)
                    : list.percentOff(25);
            case "BUY2GET1" -> {
                Money discount = Money.ZERO;
                for (Line line : basket.inCategory("book")) {
                    int free = line.quantity() / 3;
                    discount = discount.plus(line.unitPrice().times(free));
                }
                yield list.minus(discount);
            }
            default -> throw new IllegalArgumentException("unknown campaign: " + campaign);
        };
        return new Receipt(campaign, list, paid);
    }
}
