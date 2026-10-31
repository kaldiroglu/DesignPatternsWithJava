
package dev.kaldiroglu.dp.creational.prototype.account.solution.solution2;

import dev.kaldiroglu.dp.creational.prototype.account.customer.Customer;

public class AccountFactory implements Factory {

    // Normal account
    private static Account prototype = new Account("1", 1000, 1000, new Customer("Ali"), true, true, true);

    /**
     * Just creates a new account by cloning the prototype
     */
    @Override
    public Account create() {
        // Just clone the prototype and return it
        return prototype.clone();
    }

    /**
     * Receives parameters to set the state of the cloned object
     */
    @Override
    public Account create(Customer owner, double balance) {
        // First clone the prototype
        Account account = prototype.clone();
        account.setOwner(owner);
        account.setBalance(balance);
        return account;
    }
}
