package dev.kaldiroglu.dp.behavioral.observer.payment;

import dev.kaldiroglu.dp.behavioral.observer.Printed;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Observable;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The invoice built on java.util.Observable. The Part 3 slide says the boss is added first
 * and is told second; this class checks it.
 */
@SuppressWarnings("deprecation")
class InvoiceTest {

    @Test
    @DisplayName("the boss is added first, but the accountant is told first")
    void theAccountantIsToldFirst() {
        Invoice invoice = new Invoice(10_000);
        invoice.addObserver(new Boss());
        invoice.addObserver(new Accountant());

        List<String> lines = Printed.by(() -> invoice.payBalance(5000));

        List<String> updates = lines.stream().filter(line -> line.endsWith("received an update.")).toList();
        assertEquals(List.of("Accountant has received an update.", "Boss has received an update."), updates);
        assertTrue(lines.stream().anyMatch(line -> line.startsWith("Invoice [balance=5000.0, no=")));
    }

    @Test
    @DisplayName("Test prints the accountant's update before the boss's, and only the accountant's after the boss is removed")
    void testOutput() {
        List<String> lines = Printed.by(() -> dev.kaldiroglu.dp.behavioral.observer.payment.Test.main(new String[0]));

        List<String> updates = lines.stream().filter(line -> line.endsWith("received an update.")).toList();
        assertEquals(List.of("Accountant has received an update.", "Boss has received an update.",
                "Accountant has received an update."), updates);
        assertEquals(2, lines.stream().filter(line -> line.equals("Some payment made.")).count());
        assertTrue(lines.getLast().startsWith("Invoice [balance=3000.0, no="));
    }

    @Test
    @DisplayName("without setChanged, notifyObservers tells nobody")
    void setChangedComesFirst() {
        Invoice invoice = new Invoice(100);
        invoice.addObserver(new Accountant());

        assertEquals(List.of(), Printed.by(invoice::notifyObservers));
    }

    @Test
    @DisplayName("Observable is a class, so Invoice cannot extend anything else")
    void observableIsAClass() {
        assertFalse(Observable.class.isInterface());
        assertEquals(Observable.class, Invoice.class.getSuperclass());
    }

    @Test
    @DisplayName("Observable and Observer are deprecated since Java 9")
    void deprecatedSinceNine() {
        assertEquals("9", Observable.class.getAnnotation(Deprecated.class).since());
        assertEquals("9", java.util.Observer.class.getAnnotation(Deprecated.class).since());
    }
}
