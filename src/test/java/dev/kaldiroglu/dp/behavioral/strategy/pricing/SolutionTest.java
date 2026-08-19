package dev.kaldiroglu.dp.behavioral.strategy.pricing;

import dev.kaldiroglu.dp.behavioral.strategy.pricing.domain.Basket;
import dev.kaldiroglu.dp.behavioral.strategy.pricing.domain.Customer;
import dev.kaldiroglu.dp.behavioral.strategy.pricing.domain.Line;
import dev.kaldiroglu.dp.behavioral.strategy.pricing.domain.Money;
import dev.kaldiroglu.dp.behavioral.strategy.pricing.domain.Receipt;
import dev.kaldiroglu.dp.behavioral.strategy.pricing.solution.CampaignBook;
import dev.kaldiroglu.dp.behavioral.strategy.pricing.solution.CheapestOfEveryThird;
import dev.kaldiroglu.dp.behavioral.strategy.pricing.solution.Checkout;
import dev.kaldiroglu.dp.behavioral.strategy.pricing.solution.PercentageOff;
import dev.kaldiroglu.dp.behavioral.strategy.pricing.solution.PricingRule;
import dev.kaldiroglu.dp.behavioral.strategy.pricing.solution.ShelfPrice;
import dev.kaldiroglu.dp.behavioral.strategy.pricing.solution.TieredPercentageOff;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The point of this class.
 * <p>
 * The pattern applied to the same baskets the naive designs priced, and the operations they
 * could not perform. Every figure the slides quote is counted here rather than written down.
 */
class SolutionTest {

    private static final String SOURCE =
            "src/main/java/dev/kaldiroglu/dp/behavioral/strategy/pricing/solution/";

    private static Basket studentBasket() {
        return Basket.of(Customer.student("Ceyda"),
                new Line("java-book", "book", Money.of("400.00"), 3));
    }

    private static CampaignBook today() {
        return new CampaignBook(
                new ShelfPrice(),
                PercentageOff.student(),
                PercentageOff.staff(),
                TieredPercentageOff.blackFriday(),
                new CheapestOfEveryThird("book"));
    }

    @Test
    @DisplayName("every rule prices the basket the naive designs priced, to the lira")
    void theSamePrices() {
        Basket basket = studentBasket();

        assertEquals(Money.of("1200.00"), new ShelfPrice().priceFor(basket));
        assertEquals(Money.of("960.00"), PercentageOff.student().priceFor(basket));
        assertEquals(Money.of("720.00"), TieredPercentageOff.blackFriday().priceFor(basket));
        assertEquals(Money.of("800.00"), new CheapestOfEveryThird("book").priceFor(basket));
    }

    @Test
    @DisplayName("a rule is testable without a till, because it is handed a basket and nothing else")
    void aRuleNeedsNoContext() {
        PricingRule rule = TieredPercentageOff.blackFriday();

        Basket small = Basket.of(Customer.shopper("Bora"),
                new Line("mug", "kitchen", Money.of("100.00"), 2));
        Basket large = Basket.of(Customer.shopper("Bora"),
                new Line("kettle", "kitchen", Money.of("600.00"), 2));

        assertEquals(Money.of("150.00"), rule.priceFor(small));   // under the threshold: 25%
        assertEquals(Money.of("720.00"), rule.priceFor(large));   // at or over it: 40%
    }

    @Test
    @DisplayName("the rule can be replaced on a till that already exists")
    void theRuleIsAField() {
        Checkout till = new Checkout(new ShelfPrice());
        Basket basket = studentBasket();
        assertEquals(Money.of("1200.00"), till.ring(basket).paid());

        till.setRule(PercentageOff.student());          // the same till, Thursday morning

        assertEquals("STUDENT", till.ruleName());
        assertEquals(Money.of("960.00"), till.ring(basket).paid());
    }

    @Test
    @DisplayName("the receipt's promise costs nothing: the saving is a subtraction")
    void theReceiptPromise() {
        Receipt receipt = new Checkout(TieredPercentageOff.blackFriday()).ring(studentBasket());

        assertEquals("BLACK_FRIDAY", receipt.campaign());
        assertEquals(Money.of("1200.00"), receipt.list());
        assertEquals(Money.of("720.00"), receipt.paid());
        assertEquals(Money.of("480.00"), receipt.saved());
    }

    @Test
    @DisplayName("one till prices one basket five ways, and names no campaign class to do it")
    void oneTillEveryCampaign() {
        List<Receipt> quotes = today().quoteAll(studentBasket());

        assertEquals(5, quotes.size());
        assertEquals(List.of("NONE", "STUDENT", "STAFF", "BLACK_FRIDAY", "BUY_TWO_GET_ONE"),
                quotes.stream().map(Receipt::campaign).toList());

        // The staff rule does not apply to a student, so it charges shelf price. A rule that
        // does not fit is not an exception; it is a rule that takes nothing off.
        assertEquals(Money.of("1200.00"),
                quotes.stream().filter(q -> q.campaign().equals("STAFF")).findFirst()
                        .orElseThrow().paid());
    }

    @Test
    @DisplayName("the best campaign is chosen at run time, from a list handed in")
    void theBestOfThem() {
        assertEquals("BLACK_FRIDAY", today().bestFor(studentBasket()).name());

        // Under the threshold, Black Friday drops to 25% and a rule that is not a
        // percentage at all wins: three books at 300 is 900, and one of them comes off.
        Basket threeBooks = Basket.of(Customer.student("Ceyda"),
                new Line("poetry", "book", Money.of("300.00"), 3));
        assertEquals(Money.of("675.00"), TieredPercentageOff.blackFriday().priceFor(threeBooks));
        assertEquals(Money.of("600.00"), new CheapestOfEveryThird("book").priceFor(threeBooks));
        assertEquals("BUY_TWO_GET_ONE", today().bestFor(threeBooks).name());
    }

    @Test
    @DisplayName("a fourth campaign is one class, and nothing already written is touched")
    void addingACampaignCostsOneClass() {
        // The whole of a new campaign, invented here. No rule, no till and no book is edited.
        PricingRule newYear = new PricingRule() {
            @Override
            public String name() {
                return "NEW_YEAR";
            }

            @Override
            public Money priceFor(Basket basket) {
                return basket.listTotal().minus(Money.of("500.00"));
            }
        };

        CampaignBook book = today().add(newYear);
        assertEquals(6, book.size());
        assertEquals(Money.of("700.00"), newYear.priceFor(studentBasket()));

        // And it wins, by twenty lira over Black Friday's 720. The best rule is decided by
        // arithmetic on the day, not by a rank written into the rules.
        assertEquals("NEW_YEAR", book.bestFor(studentBasket()).name());
    }

    @Test
    @DisplayName("the context asks the rule and never asks which rule it is holding")
    void noBranchInTheContext() throws Exception {
        String body = Files.readString(Path.of(SOURCE + "Checkout.java"));
        body = body.substring(body.indexOf("public final class"));

        // Comments out first. This class documents what it deliberately does not do, so a
        // plain search over the file matches its own javadoc and proves nothing.
        String code = body.replaceAll("(?s)/\\*.*?\\*/", " ").replaceAll("//[^\n]*", " ");

        assertFalse(code.contains("instanceof"), "the context must not test the strategy's type");
        assertFalse(code.contains("switch"), "nor branch over a campaign");
        assertFalse(code.contains("STUDENT"), "nor name one");
        assertTrue(code.contains("rule.priceFor"), "it asks the rule, and that is all");
    }

    @Test
    @DisplayName("five rules carry the campaigns, and the interface has two methods")
    void theArithmetic() {
        List<Class<?>> rules = List.of(ShelfPrice.class, PercentageOff.class,
                TieredPercentageOff.class, CheapestOfEveryThird.class);

        assertTrue(rules.stream().allMatch(PricingRule.class::isAssignableFrom));
        assertEquals(2, PricingRule.class.getDeclaredMethods().length);

        // Four classes cover five campaigns, because PercentageOff is one class used twice.
        assertEquals(4, rules.size());
        assertEquals(5, today().size());
    }

    @Test
    @DisplayName("no campaign means the ShelfPrice rule, not a null")
    void nullIsNotACampaign() {
        assertThrows(NullPointerException.class, () -> new Checkout(null));
        assertEquals(Money.of("1200.00"),
                new Checkout(new ShelfPrice()).ring(studentBasket()).paid());
    }
}
