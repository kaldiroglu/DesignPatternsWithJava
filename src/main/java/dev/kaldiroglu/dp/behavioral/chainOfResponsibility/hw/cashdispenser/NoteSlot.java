package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.hw.cashdispenser;

import java.util.ArrayList;
import java.util.List;

/**
 * Homework 3: a cash machine. Each slot holds one kind of note, pays out as many as it
 * can, and passes the rest of the amount to the next slot.
 * <p>
 * Every link handles part of the request. What is left at the end of the chain cannot be
 * paid. Each slot decides alone, taking the largest notes first, so the chain can fail
 * where another split of notes would work: 260 leaves 10 after 200 and 50, although
 * 100 + 100 + 20 + 20 + 20 is 260.
 */
public final class NoteSlot {

    private final int note;
    private NoteSlot next;

    public NoteSlot(int note) {
        this.note = note;
    }

    public NoteSlot then(NoteSlot next) {
        this.next = next;
        return next;
    }

    public List<String> pay(int amount) {
        List<String> paid = new ArrayList<>();
        int count = amount / note;
        int rest = amount % note;
        if (count > 0) {
            paid.add(count + " x " + note);
        }
        if (rest == 0) {
            return paid;
        }
        if (next == null) {
            paid.add(rest + " cannot be paid");
            return paid;
        }
        paid.addAll(next.pay(rest));
        return paid;
    }
}
