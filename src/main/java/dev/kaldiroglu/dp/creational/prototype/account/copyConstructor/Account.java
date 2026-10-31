
package dev.kaldiroglu.dp.creational.prototype.account.copyConstructor;

import dev.kaldiroglu.dp.creational.prototype.account.customer.Customer;

public class Account {
	protected String iban;
	protected double balance;
	protected double credit;
	protected Customer owner;

	// Important state variables
	private boolean openToWithdraw;
	private boolean openToPayment;
	private boolean openToTransfer;

	public Account(String iban, double balance, double credit, Customer owner, boolean openToWithdraw,
			boolean openToPayment, boolean openToTransfer) {
		this.iban = iban;
		this.balance = balance;
		this.credit = credit;
		this.owner = owner;
		this.openToWithdraw = openToWithdraw;
		this.openToPayment = openToPayment;
		this.openToTransfer = openToTransfer;
	}

	public Account(Account other) {
		if (other == null) {
			throw new IllegalArgumentException("other must not be null");
		}
		this.iban = "-1"; // Make sure that a new id will be assigned
		this.balance = other.balance;
		this.credit = other.credit;
		this.owner = null; // Make sure that a new owner will be assigned
		this.openToWithdraw = other.openToWithdraw;
		this.openToPayment = other.openToPayment;
		this.openToTransfer = other.openToTransfer;
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public Customer getOwner() {
		return owner;
	}

	public void setOwner(Customer owner) {
		this.owner = owner;
	}

	public boolean isOpenToWithdraw() {
		return openToWithdraw;
	}

	public void setOpenToWithdraw(boolean openToWithdraw) {
		this.openToWithdraw = openToWithdraw;
	}

	public boolean isOpenToPayment() {
		return openToPayment;
	}

	public void setOpenToPayment(boolean openToPayment) {
		this.openToPayment = openToPayment;
	}

	public boolean isOpenToTransfer() {
		return openToTransfer;
	}

	public void setOpenToTransfer(boolean openToTransfer) {
		this.openToTransfer = openToTransfer;
	}

	@Override
	public String toString() {
		return "Account{" +
				"iban='" + iban + '\'' +
				", balance=" + balance +
				", credit=" + credit +
				", owner=" + owner +
				", openToWithdraw=" + openToWithdraw +
				", openToPayment=" + openToPayment +
				", openToTransfer=" + openToTransfer +
				'}';
	}
}
