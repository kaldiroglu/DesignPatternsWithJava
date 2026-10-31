
package dev.kaldiroglu.dp.creational.prototype.account.solution.solution2;


import dev.kaldiroglu.dp.creational.prototype.account.customer.Customer;

public interface Factory {
	
	Account create();

	// Receives parameters to set the state of the cloned object
	Account create(Customer owner, double balance);
	
}
