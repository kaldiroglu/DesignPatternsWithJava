package dev.kaldiroglu.dp.behavioral.memento.hw.rollback;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** Homework 2: three transfers, and the second fails. */
class BatchTest {

    @Test
    @DisplayName("when the second of three transfers fails, every account goes back to where it was")
    void aFailedBatchIsRolledBack() {
        Account elif = new Account("Elif", 100);
        Account burak = new Account("Burak", 50);
        Account mert = new Account("Mert", 0);
        Batch batch = new Batch();
        batch.add(elif, burak, 80);
        batch.add(mert, elif, 30);
        batch.add(burak, mert, 10);

        assertEquals("rolled back: Mert cannot pay 30", batch.run(List.of(elif, burak, mert)));
        assertEquals("Elif 100", elif.toString());
        assertEquals("Burak 50", burak.toString());
        assertEquals("Mert 0", mert.toString());
    }

    @Test
    @DisplayName("a batch where every transfer succeeds is done")
    void aBatchThatSucceeds() {
        Account elif = new Account("Elif", 100);
        Account burak = new Account("Burak", 50);
        Batch batch = new Batch();
        batch.add(elif, burak, 80);
        batch.add(burak, elif, 30);

        assertEquals("done", batch.run(List.of(elif, burak)));
        assertEquals("Elif 50", elif.toString());
        assertEquals("Burak 100", burak.toString());
    }
}
