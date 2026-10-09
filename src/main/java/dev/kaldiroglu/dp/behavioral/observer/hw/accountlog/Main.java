package dev.kaldiroglu.dp.behavioral.observer.hw.accountlog;

/**
 * A transaction log listens to an account. Two changes are recorded; a withdrawal that is
 * too large throws before any listener is told, so it is not recorded.
 */
public final class Main {

    public static void main(String[] args) {
        Account account = new Account("Ayse", 100);
        TransactionLog log = new TransactionLog();
        account.addListener(log);

        account.deposit(50);
        account.withdraw(30);
        try {
            account.withdraw(500);
        } catch (IllegalArgumentException refused) {
            System.out.println("Refused: " + refused.getMessage());
        }
        for (Transaction transaction : log.transactions()) {
            System.out.println(transaction);
        }
    }
}
