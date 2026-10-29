package dev.kaldiroglu.dp.introduction.problems.pricing;

import dev.kaldiroglu.dp.introduction.problems.pricing.domain.Basket;
import dev.kaldiroglu.dp.introduction.problems.pricing.domain.Customer;
import dev.kaldiroglu.dp.introduction.problems.pricing.domain.Line;
import dev.kaldiroglu.dp.introduction.problems.pricing.domain.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The one-method checkout the Introduction deck shows as a long switch: it prices
 * correctly, every campaign is a branch of one method, and a typo in a campaign name
 * fails only at run time.
 */
class SwitchingCheckoutTest {

    private static final String SOURCE =
            "src/main/java/dev/kaldiroglu/dp/introduction/problems/pricing/";

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

    private static List<String> printedBy(Runnable action) {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buffer, true));
        try {
            action.run();
        } finally {
            System.setOut(original);
        }
        return buffer.toString().lines().toList();
    }

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

    @Test
    @DisplayName("StageOneMain prints the four campaigns and the typo that fails at run time")
    void stageOneMainPrintsTheStringSwitch() {
        assertEquals(List.of(
                "Stage one, a switch on a string:",
                "  NONE                   list  1200.00   paid  1200.00   saved     0.00",
                "  STUDENT                list  1200.00   paid   960.00   saved   240.00",
                "  BLACKFRIDAY            list  1200.00   paid   720.00   saved   480.00",
                "  BUY2GET1               list  1200.00   paid   800.00   saved   400.00",
                "  A typo compiles and fails at run time: unknown campaign: BLACK_FRIDAY"),
                printedBy(() -> StageOneMain.main(new String[0])));
    }
}
