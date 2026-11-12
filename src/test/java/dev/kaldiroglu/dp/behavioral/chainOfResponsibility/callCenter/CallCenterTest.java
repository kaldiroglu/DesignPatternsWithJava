package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.callCenter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static dev.kaldiroglu.dp.behavioral.chainOfResponsibility.Printed.by;
import static org.junit.jupiter.api.Assertions.*;

/**
 * The call center: standard, then gold, then VIP. A higher desk also serves lower customers,
 * so more than one desk may answer a call, and the order of the desks decides which one does.
 */
class CallCenterTest {

    private static List<String> desksAndAnswers(CallTaker first, Customer customer) {
        return by(() -> first.answer(customer)).stream()
                .filter(line -> line.endsWith("received a customer.") || line.startsWith("Answer:"))
                .toList();
    }

    @Test
    @DisplayName("a standard customer is answered by the standard desk")
    void standard() {
        assertEquals(List.of(
                "StandardCallTaker received a customer.",
                "Answer: Here is your answer!"),
                desksAndAnswers(Main.createCallTakers(), new StandardCustomer()));
    }

    @Test
    @DisplayName("a gold customer passes the standard desk and is answered by the gold desk")
    void gold() {
        assertEquals(List.of(
                "StandardCallTaker received a customer.",
                "GoldCallTaker received a customer.",
                "Answer: Here is your GOLD answer!"),
                desksAndAnswers(Main.createCallTakers(), new GoldCustomer()));
    }

    @Test
    @DisplayName("a VIP customer passes two desks and gets the VIP answer")
    void vip() {
        assertEquals(List.of(
                "StandardCallTaker received a customer.",
                "GoldCallTaker received a customer.",
                "VipCallTaker received a customer.",
                "Answer: Here is your VIP answer!"),
                desksAndAnswers(Main.createCallTakers(), new VipCustomer()));
    }

    @Test
    @DisplayName("a higher desk serves lower customers: without the standard desk, gold answers a standard customer")
    void higherDesksServeLowerCustomers() {
        CallTaker first = new GoldCallTaker(new VipCallTaker(null));
        assertEquals(List.of(
                "GoldCallTaker received a customer.",
                "Answer: Here is your GOLD answer!"), desksAndAnswers(first, new StandardCustomer()));
        CallTaker vipOnly = new VipCallTaker(null);
        for (Customer customer : Main.customers()) {
            assertTrue(desksAndAnswers(vipOnly, customer).contains("Answer: Here is your VIP answer!"));
        }
    }

    @Test
    @DisplayName("the order matters: with the VIP desk first, it answers all three customers")
    void theOrderMatters() {
        List<String> lines = by(() -> VipFirstMain.main(new String[0]));
        assertEquals(3, lines.stream().filter("Answer: Here is your VIP answer!"::equals).count());
        assertFalse(lines.contains("GoldCallTaker received a customer."));
        assertFalse(lines.contains("StandardCallTaker received a customer."));
    }

    @Test
    @DisplayName("a customer no desk serves reaches the end of the chain and is told so, with no exception")
    void noDeskCanAnswer() {
        CallTaker first = new StandardCallTaker(new GoldCallTaker(null));
        List<String> lines = by(() -> first.answer(new VipCustomer()));
        assertTrue(lines.contains("Answer: No desk can answer you."));
    }

    @Test
    @DisplayName("the VIP desk is the end of the chain and has no next desk")
    void theVipDeskIsLast() {
        VipCallTaker vip = new VipCallTaker(null);
        assertNull(vip.getNext());
    }

    @Test
    @DisplayName("Main prints each customer reaching the first desk that serves them, one blank line per customer")
    void mainOutput() {
        assertEquals(List.of(
                "StandardCallTaker received a customer.",
                "Standard customer: asking a question.",
                "Answer: Here is your answer!",
                "",
                "StandardCallTaker received a customer.",
                "GoldCallTaker received a customer.",
                "Gold customer: asking a GOLD question.",
                "Answer: Here is your GOLD answer!",
                "",
                "StandardCallTaker received a customer.",
                "GoldCallTaker received a customer.",
                "VipCallTaker received a customer.",
                "VIP customer: asking a VIP question.",
                "Answer: Here is your VIP answer!",
                ""), by(() -> Main.main(new String[0])));
    }

    @Test
    @DisplayName("VipFirstMain prints the VIP desk answering all three customers")
    void vipFirstMainOutput() {
        assertEquals(List.of(
                "VipCallTaker received a customer.",
                "Standard customer: asking a question.",
                "Answer: Here is your VIP answer!",
                "",
                "VipCallTaker received a customer.",
                "Gold customer: asking a GOLD question.",
                "Answer: Here is your VIP answer!",
                "",
                "VipCallTaker received a customer.",
                "VIP customer: asking a VIP question.",
                "Answer: Here is your VIP answer!",
                ""), by(() -> VipFirstMain.main(new String[0])));
    }
}
