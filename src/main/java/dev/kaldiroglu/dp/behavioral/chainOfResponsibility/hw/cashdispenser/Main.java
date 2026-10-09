package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.hw.cashdispenser;

/**
 * Pays three amounts from slots of 200, 100, 50 and 20. Each slot pays what it can and
 * passes the rest on, so 380 and 260 leave 10 unpaid, although other notes would pay them.
 */
public final class Main {

    public static void main(String[] args) {
        NoteSlot machine = new NoteSlot(200);
        machine.then(new NoteSlot(100)).then(new NoteSlot(50)).then(new NoteSlot(20));

        for (int amount : new int[] {370, 380, 260}) {
            System.out.println(amount + ": " + machine.pay(amount));
        }
    }
}
