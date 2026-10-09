package dev.kaldiroglu.dp.behavioral.observer.publisher;

import dev.kaldiroglu.dp.behavioral.observer.Printed;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** Magazines and their subscribers: one publish, two different reactions. */
class PublisherTest {

    @Test
    @DisplayName("Akin and Sevgi read the new issue, and BankOne puts it on the shelf")
    void onePublishTwoReactions() {
        Publication newsweek = new Newsweek("Newsweek");
        newsweek.addSubscriber(new IndividualSubscriber("Akin"));
        newsweek.addSubscriber(new IndividualSubscriber("Sevgi"));
        newsweek.addSubscriber(new InstitutionalSubscriber("BankOne"));

        List<String> lines = Printed.by(() -> newsweek.publish("2026-10-09"));

        assertEquals(List.of(
                "Akin is reading Newsweek - 2026-10-09",
                "Sevgi is reading Newsweek - 2026-10-09",
                "Newsweek - 2026-10-09 is on the shelf of BankOne"), lines);
    }

    @Test
    @DisplayName("a person can subscribe to both magazines, and a removed subscriber is not told")
    void twoMagazines() {
        Publisher publisher = new Publisher();
        Subscriber akin = new IndividualSubscriber("Akin");
        Subscriber bank = new InstitutionalSubscriber("BankOne");
        publisher.getNewsweek().addSubscriber(akin);
        publisher.getFourFourTwo().addSubscriber(akin);
        publisher.getFourFourTwo().addSubscriber(bank);
        publisher.getFourFourTwo().removeSubscriber(bank);

        assertEquals(List.of("Akin is reading FourFourTwo - May"),
                Printed.by(() -> publisher.getFourFourTwo().publish("May")));
        assertEquals("Newsweek", publisher.getNewsweek().getName());
    }

    @Test
    @DisplayName("Test prints that Akin and Sevgi are reading the issue, and that it is on the shelf of BankOne")
    void testOutput() {
        List<String> lines = Printed.by(() -> dev.kaldiroglu.dp.behavioral.observer.publisher.Test.main(new String[0]));

        assertEquals(5, lines.size());
        assertEquals(" New Newsweek On The Way", lines.get(1));
        assertTrue(lines.get(2).startsWith("Akin is reading Newsweek - "));
        assertTrue(lines.get(3).startsWith("Sevgi is reading Newsweek - "));
        assertTrue(lines.get(4).endsWith(" is on the shelf of BankOne"));
    }
}
