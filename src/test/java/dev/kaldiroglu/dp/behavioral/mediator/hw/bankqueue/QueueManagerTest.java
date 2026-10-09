package dev.kaldiroglu.dp.behavioral.mediator.hw.bankqueue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static dev.kaldiroglu.dp.behavioral.mediator.Fields.holdsAny;
import static org.junit.jupiter.api.Assertions.*;

/** Homework 1: the queue manager pairs customers and tellers in arrival order. */
class QueueManagerTest {

    @Test
    @DisplayName("customers take numbers, and free tellers call them in arrival order")
    void pairsInArrivalOrder() {
        QueueManager manager = new QueueManager();
        Teller tellerOne = new Teller("Teller 1", manager);
        Teller tellerTwo = new Teller("Teller 2", manager);

        new Customer("Elif", manager).arrive();
        new Customer("Burak", manager).arrive();
        tellerTwo.free();
        tellerOne.free();
        tellerOne.free();
        new Customer("Mert", manager).arrive();

        assertEquals(List.of(
                "Elif takes number 1",
                "Burak takes number 2",
                "Teller 2 calls number 1 (Elif)",
                "Teller 1 calls number 2 (Burak)",
                "Teller 1 waits for a customer",
                "Mert takes number 3",
                "Teller 1 calls number 3 (Mert)"), manager.log());
    }

    @Test
    @DisplayName("customers and tellers never refer to each other")
    void noColleagueKnowsAnother() {
        assertFalse(holdsAny(Customer.class, List.of(Teller.class)));
        assertFalse(holdsAny(Teller.class, List.of(Customer.class)));
    }
}
