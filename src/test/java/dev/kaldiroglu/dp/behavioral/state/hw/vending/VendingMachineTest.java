package dev.kaldiroglu.dp.behavioral.state.hw.vending;

import dev.kaldiroglu.dp.behavioral.state.Printed;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** Homework 3: the vending machine. A sold-out machine returns the coin at once. */
class VendingMachineTest {

    @Test
    @DisplayName("after the last drink the machine is sold out, and it returns the next coin")
    void soldOutReturnsTheCoin() {
        VendingMachine machine = new VendingMachine(1);
        machine.insertCoin();
        machine.pressButton();
        assertEquals("SoldOut", machine.state());
        machine.insertCoin();
        assertEquals("SoldOut", machine.state());
        assertEquals("coin returned: sold out", machine.log().getLast());
    }

    @Test
    @DisplayName("a second coin is returned, and a button with no coin gives nothing")
    void oneCoinAtATime() {
        VendingMachine machine = new VendingMachine(3);
        machine.pressButton();
        machine.insertCoin();
        machine.insertCoin();
        assertEquals(List.of("insert a coin first", "coin accepted", "coin returned: one coin is enough"),
                machine.log());
        assertEquals("HasCoin", machine.state());
    }

    @Test
    @DisplayName("a refill makes a sold-out machine wait for a coin again")
    void aRefill() {
        VendingMachine machine = new VendingMachine(0);
        assertEquals("SoldOut", machine.state());
        machine.refill(5);
        assertEquals("WaitingForCoin", machine.state());
    }

    @Test
    @DisplayName("Main prints the states after one sale and after a refill, and the log")
    void mainOutput() {
        assertEquals(List.of(
                "After one sale: SoldOut",
                "After a refill: WaitingForCoin",
                "Log: [insert a coin first, coin accepted, coin returned: one coin is enough, drink given, coin returned: sold out]"),
                Printed.by(() -> Main.main(new String[0])));
    }
}
