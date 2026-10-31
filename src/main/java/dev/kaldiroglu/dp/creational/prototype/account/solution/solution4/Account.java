
package dev.kaldiroglu.dp.creational.prototype.account.solution.solution4;

import dev.kaldiroglu.dp.creational.prototype.account.customer.Customer;

public class Account implements Cloneable {
	protected String iban;
	protected double balance;
	protected double credit;
	protected Customer owner;

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
	public Account clone() {
		Account cloneAccount = null;
		try {
			// This creates a shallow copy of the existing Account object
			cloneAccount = (Account) super.clone();
			// Make sure that iban should be changed and a new owner is assigned!
			cloneAccount.iban = "-1";
			cloneAccount.owner = null;
		} catch (CloneNotSupportedException e) {
			System.out.println("Problem when cloning the object: " + e.getMessage());
			e.printStackTrace();
		}
		return cloneAccount;
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
