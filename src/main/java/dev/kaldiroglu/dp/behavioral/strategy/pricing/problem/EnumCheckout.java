package dev.kaldiroglu.dp.behavioral.strategy.pricing.problem;

import dev.kaldiroglu.dp.behavioral.strategy.pricing.domain.Basket;
import dev.kaldiroglu.dp.behavioral.strategy.pricing.domain.Line;
import dev.kaldiroglu.dp.behavioral.strategy.pricing.domain.Money;
import dev.kaldiroglu.dp.behavioral.strategy.pricing.domain.Receipt;

/**
 * Stage two: the same branches, over an enum the compiler checks.
 * <p>
 * This is better than stage one and the improvement is real. The switch is exhaustive, so
 * adding a constant to {@link Campaign} makes this file stop compiling until the new case
 * is written — the compiler now tells you what you forgot, which is exactly what it failed
 * to do before.
 *
 * <h2>What it still costs</h2>
 * <ul>
 *   <li><b>A campaign is still two edits in two files.</b> One in the enum, one here, and
 *       they are only kept in step by the compiler noticing.</li>
 *   <li><b>The pricing logic and the campaign list are the same class's business.</b> The
 *       till knows every rule the company has ever run, which is why this file is the one
 *       that changes every Thursday.</li>
 *   <li><b>Nothing outside this file can add a campaign.</b> Marketing cannot, a test
 *       cannot, and a regional store cannot. A rule the company invents is a release.</li>
 * </ul>
 */
public final class EnumCheckout {

    public Receipt ring(Basket basket, Campaign campaign) {
        Money list = basket.listTotal();
        Money paid = switch (campaign) {
            case NONE -> list;
            case STUDENT -> basket.customer().student() ? list.percentOff(20) : list;
            case STAFF -> basket.customer().staff() ? list.percentOff(30) : list;
            case BLACK_FRIDAY -> list.isAtLeast(Money.of("1000.00"))
                    ? list.percentOff(40)
                    : list.percentOff(25);
            case BUY_TWO_GET_ONE -> {
                Money discount = Money.ZERO;
                for (Line line : basket.inCategory("book")) {
                    discount = discount.plus(line.unitPrice().times(line.quantity() / 3));
                }
                yield list.minus(discount);
            }
        };
        return new Receipt(campaign.name(), list, paid);
    }
}
