package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.hw.cashdispenser;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** Homework 3: a cash machine with 200, 100, 50 and 20 notes. */
class NoteSlotTest {

    private static NoteSlot machine() {
        NoteSlot first = new NoteSlot(200);
        first.then(new NoteSlot(100)).then(new NoteSlot(50)).then(new NoteSlot(20));
        return first;
    }

    @Test
    @DisplayName("each slot pays what it can and passes the rest on")
    void eachSlotPaysItsPart() {
        assertEquals(List.of("1 x 200", "1 x 100", "1 x 50", "1 x 20"), machine().pay(370));
    }

    @Test
    @DisplayName("380 leaves 10 that nobody can pay")
    void threeHundredEighty() {
        assertEquals(List.of("1 x 200", "1 x 100", "1 x 50", "1 x 20", "10 cannot be paid"),
                machine().pay(380));
    }

    @Test
    @DisplayName("260 leaves 10 as well, although 100 + 100 + 20 + 20 + 20 would pay it")
    void twoHundredSixty() {
        assertEquals(List.of("1 x 200", "1 x 50", "10 cannot be paid"), machine().pay(260));
        assertEquals(260, 100 + 100 + 20 + 20 + 20);
        NoteSlot withoutTheTwoHundred = new NoteSlot(100);
        withoutTheTwoHundred.then(new NoteSlot(20));
        assertEquals(List.of("2 x 100", "3 x 20"), withoutTheTwoHundred.pay(260),
                "the split exists; the chain does not find it because each slot decides alone");
    }
}
