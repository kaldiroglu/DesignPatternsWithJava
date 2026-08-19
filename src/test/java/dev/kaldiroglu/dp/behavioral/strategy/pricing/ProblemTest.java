package dev.kaldiroglu.dp.behavioral.strategy.pricing;

import dev.kaldiroglu.dp.behavioral.strategy.pricing.domain.Basket;
import dev.kaldiroglu.dp.behavioral.strategy.pricing.domain.Customer;
import dev.kaldiroglu.dp.behavioral.strategy.pricing.domain.Line;
import dev.kaldiroglu.dp.behavioral.strategy.pricing.domain.Money;
import dev.kaldiroglu.dp.behavioral.strategy.pricing.domain.Receipt;
import dev.kaldiroglu.dp.behavioral.strategy.pricing.problem.BlackFridayCheckout;
import dev.kaldiroglu.dp.behavioral.strategy.pricing.problem.Campaign;
import dev.kaldiroglu.dp.behavioral.strategy.pricing.problem.Checkout;
import dev.kaldiroglu.dp.behavioral.strategy.pricing.problem.EnumCheckout;
import dev.kaldiroglu.dp.behavioral.strategy.pricing.problem.StudentCheckout;
import dev.kaldiroglu.dp.behavioral.strategy.pricing.problem.SwitchingCheckout;
import dev.kaldiroglu.dp.behavioral.strategy.pricing.problem.Till;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The point of this class.
 * <p>
 * The three naive designs, and what each one costs. All three price correctly — that is
 * what makes them worth teaching — so every number the slides quote about them is measured
 * here rather than asserted in prose.
 */
class ProblemTest {

    private static final String SOURCE =
            "src/main/java/dev/kaldiroglu/dp/behavioral/strategy/pricing/problem/";

    private static Basket studentBasket() {
        return Basket.of(Customer.student("Ceyda"),
                new Line("java-book", "book", Money.of("400.00"), 3));
    }

    private static int countOf(String text, String needle) {
        int count = 0;
        for (int i = text.indexOf(needle); i >= 0; i = text.indexOf(needle, i + needle.length())) {
            count++;
        }
        return count;
    }

    // --------------------------------------------------- stage one: a branch per campaign

    @Test
    @DisplayName("switch: it prices correctly, which is why nobody rewrites it")
    void theSwitchWorks() {
        SwitchingCheckout till = new SwitchingCheckout();
        Basket basket = studentBasket();

        assertEquals(Money.of("1200.00"), till.ring(basket, "NONE").paid());
        assertEquals(Money.of("960.00"), till.ring(basket, "STUDENT").paid());
        assertEquals(Money.of("720.00"), till.ring(basket, "BLACKFRIDAY").paid());
        assertEquals(Money.of("800.00"), till.ring(basket, "BUY2GET1").paid());
    }

    @Test
    @DisplayName("switch: the campaign is a string, so a typo is a run-time failure")
    void theCampaignIsUnchecked() {
        assertThrows(IllegalArgumentException.class,
                () -> new SwitchingCheckout().ring(studentBasket(), "BLACK_FRIDAY"));
    }

    @Test
    @DisplayName("switch: five campaigns, one method, and the threshold written where it is used")
    void everyCampaignIsABranch() throws Exception {
        String body = Files.readString(Path.of(SOURCE + "SwitchingCheckout.java"));
        body = body.substring(body.indexOf("public final class"));

        assertEquals(5, countOf(body, "case \""), "campaigns, each a branch in one method");
        assertEquals(1, countOf(body, "ring("), "and all of them in a single method");
    }

    // ------------------------------------------------------ stage two: the compiler helps

    @Test
    @DisplayName("enum: the same prices, and now the compiler checks the campaign")
    void theEnumWorks() {
        EnumCheckout till = new EnumCheckout();
        Basket basket = studentBasket();

        assertEquals(Money.of("960.00"), till.ring(basket, Campaign.STUDENT).paid());
        assertEquals(Money.of("720.00"), till.ring(basket, Campaign.BLACK_FRIDAY).paid());
        assertEquals(5, Campaign.values().length);
    }

    @Test
    @DisplayName("enum: a campaign is still two edits, in two files that only the compiler joins")
    void aCampaignIsTwoEdits() throws Exception {
        String checkout = Files.readString(Path.of(SOURCE + "EnumCheckout.java"));
        String body = checkout.substring(checkout.indexOf("public final class"));

        // One case per constant, and the switch is exhaustive: no default, so adding a
        // constant breaks this file until somebody writes the branch.
        assertEquals(Campaign.values().length, countOf(body, "case "));
        assertEquals(0, countOf(body, "default ->"), "exhaustive, so the compiler names the gap");
    }

    // --------------------------------------------- stage three: a class per campaign

    @Test
    @DisplayName("subclass: each rule is its own class, readable and testable on its own")
    void aClassPerCampaign() {
        Basket basket = studentBasket();

        assertEquals(Money.of("960.00"), new StudentCheckout().ring(basket).paid());
        assertEquals(Money.of("720.00"), new BlackFridayCheckout().ring(basket).paid());
        assertTrue(Modifier.isAbstract(Checkout.class.getModifiers()));
    }

    @Test
    @DisplayName("subclass: the till IS its campaign, so choosing costs the caller every class name")
    void theReversal() {
        Till till = new Till();
        Receipt best = till.bestFor(studentBasket());

        // It works — the customer is given the better of the campaigns.
        assertEquals("BLACK_FRIDAY", best.campaign());
        assertEquals(Money.of("720.00"), best.paid());
        assertEquals(Money.of("480.00"), best.saved());

        // And this is what it cost. The branch did not disappear at stage three; it moved
        // into the caller and became a list of type names. Adding a campaign edits Till.
        assertEquals(3, till.campaignsNamedHere());

        // There is no operation that changes a checkout's campaign, because the campaign is
        // the object's class.
        assertTrue(java.util.Arrays.stream(Checkout.class.getMethods())
                .noneMatch(m -> m.getName().toLowerCase().contains("setcampaign")
                        || m.getName().toLowerCase().contains("setrule")));
        assertNotEquals(new StudentCheckout().getClass(), new BlackFridayCheckout().getClass());
    }

    @Test
    @DisplayName("all three designs put the same price on the same basket")
    void allThreeAgree() {
        Basket basket = studentBasket();
        List<Money> studentPrice = List.of(
                new SwitchingCheckout().ring(basket, "STUDENT").paid(),
                new EnumCheckout().ring(basket, Campaign.STUDENT).paid(),
                new StudentCheckout().ring(basket).paid());

        assertEquals(1, studentPrice.stream().distinct().count(),
                "they differ in design, not in what the customer pays");
    }
}
