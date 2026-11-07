package dev.kaldiroglu.dp.behavioral.state.account;

import dev.kaldiroglu.dp.behavioral.state.Printed;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** The bank account: Active, Overdrawn, Frozen and Closed are status objects. */
class AccountTest {

    @Test
    @DisplayName("Main goes from Active to Overdrawn to Frozen, back to Active, then Closed")
    void mainPrintsEveryStatus() {
        assertEquals(List.of(
                "Status: Active and balance: 1000",
                "Status: Active and balance: 2000",
                "Status: Active and balance: 1800",
                "Status: Overdrawn and balance: -700",
                "Status: Overdrawn and balance: -900",
                "Status: Overdrawn and balance: -1000",
                "Status: Frozen and balance: -1000",
                "In frozen state no transfer is allowed!",
                "Status: Active and balance: 1000",
                "Status: Active and balance: 0",
                "Status: Closed and balance: 0",
                "Account is closed!"), Printed.by(() -> Main.main(new String[0])));
    }

    @Test
    @DisplayName("being frozen is a status object, not a boolean field")
    void theAccountKeepsNoFrozenFlag() {
        List<String> booleans = Arrays.stream(Account.class.getDeclaredFields())
                .filter(f -> f.getType() == boolean.class)
                .map(Field::getName)
                .toList();
        assertEquals(List.of(), booleans);
    }

    @Test
    @DisplayName("a negative or zero amount is rejected and the balance does not change")
    void amountsMustBePositive() {
        Account account = new Account(1000);
        Printed.by(() -> {
            assertThrows(IllegalArgumentException.class, () -> account.deposit(-500));
            assertThrows(IllegalArgumentException.class, () -> account.withdraw(-500));
            assertThrows(IllegalArgumentException.class, () -> account.transfer(0));
        });
        assertEquals(1000, account.getBalance());
    }

    @Test
    @DisplayName("an active account refuses a withdrawal beyond the overdraft limit, and says so")
    void activeRefusesTooLargeAWithdrawal() {
        Account account = new Account(100);
        List<String> printed = Printed.by(() -> account.withdraw(5000));
        assertEquals("You can not withdraw money!", printed.getLast());
        assertEquals(100, account.getBalance());
        assertInstanceOf(Active.class, account.getStatus());
    }

    @Test
    @DisplayName("closing a closed account says it is already closed")
    void closingTwice() {
        Account account = new Account(0);
        Printed.by(account::closeAccount);
        assertEquals(List.of("Account is already closed!"), Printed.by(account::closeAccount));
    }
}
