
package dev.kaldiroglu.dp.creational.prototype.account.solution.solution4;

import dev.kaldiroglu.dp.creational.prototype.account.customer.Customer;

public interface Factory {
	
	Account createNormalAccount();
	
	Account createNormalAccount(Customer owner, double balance);
	
	Account createNegativeAccount(Customer owner, double balance);
	
	Account createFrozenAccount(Customer owner, double balance);
	
}
