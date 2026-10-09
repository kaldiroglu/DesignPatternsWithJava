package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.callCenter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static dev.kaldiroglu.dp.behavioral.chainOfResponsibility.Printed.by;
import static org.junit.jupiter.api.Assertions.*;

/**
 * The call center: standard, then gold, then VIP. The chain is built here as
 * Test.createCallTakers builds it, without the random customers.
 */
class CallCenterTest {

    private static CallTaker standardDesk() {
        VipCallTaker vip = new VipCallTaker(null);
        GoldCallTaker gold = new GoldCallTaker(vip);
        return new StandardCallTaker(gold);
    }

    private static List<String> desksAndAnswers(Customer customer) {
        CallTaker first = standardDesk();
        return by(() -> first.answer(customer)).stream()
                .filter(line -> line.endsWith("received a customer.") || line.startsWith("Answer:"))
                .toList();
    }

    @Test
    @DisplayName("a standard customer is answered by the standard desk")
    void standard() {
        assertEquals(List.of(
                "StandardCallTaker received a customer.",
                "Answer: Here is your answer!"), desksAndAnswers(new StandardCustomer()));
    }

    @Test
    @DisplayName("a gold customer passes the standard desk and is answered by the gold desk")
    void gold() {
        assertEquals(List.of(
                "StandardCallTaker received a customer.",
                "GoldCallTaker received a customer.",
                "Answer: Here is your GOLD answer!"), desksAndAnswers(new GoldCustomer()));
    }

    @Test
    @DisplayName("a VIP customer passes two desks and gets the VIP answer")
    void vip() {
        assertEquals(List.of(
                "StandardCallTaker received a customer.",
                "GoldCallTaker received a customer.",
                "VipCallTaker received a customer.",
                "Answer: Here is your VIP answer!"), desksAndAnswers(new VipCustomer()));
    }

    @Test
    @DisplayName("the VIP desk is the end of the chain and has no next desk")
    void theVipDeskIsLast() {
        VipCallTaker vip = new VipCallTaker(null);
        assertNull(vip.getNext());
    }
}
