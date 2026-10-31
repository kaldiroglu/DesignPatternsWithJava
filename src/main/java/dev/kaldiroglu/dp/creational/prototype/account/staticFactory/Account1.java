
package dev.kaldiroglu.dp.creational.prototype.account.staticFactory;

import dev.kaldiroglu.dp.creational.prototype.account.customer.Customer;

public class Account1 {
	protected String iban;
	protected double balance;
	protected double credit;
	protected Customer owner;

	private boolean openToWithdraw;
	private boolean openToPayment;
	private boolean openToTransfer;

	/**
	 * Creates an Account object with credit amount passed
	 */
	private Account1(String iban, double balance, double credit, Customer owner, boolean openToWithdraw,
					 boolean openToPayment, boolean openToTransfer) {
		this.iban = iban;
		this.balance = balance;
		this.credit = credit;
		this.owner = owner;
		this.openToWithdraw = openToWithdraw;
		this.openToPayment = openToPayment;
		this.openToTransfer = openToTransfer;
	}

	/**
	 * Creates an Account object with no credit
	 */
	private Account1(String iban, double balance, Customer owner, boolean openToWithdraw,
					 boolean openToPayment, boolean openToTransfer) {
		this.iban = iban;
		this.balance = balance;
		this.owner = owner;
		this.openToWithdraw = openToWithdraw;
		this.openToPayment = openToPayment;
		this.openToTransfer = openToTransfer;
	}

	/**
	 * Creates an account in a normal state
	 */
	public static Account1 createNormalAccount(String iban, double balance, double credit, Customer owner) {
		return new Account1(iban, balance, credit, owner, true, true, true);
	}

	/**
	 * Creates an account in a negative state
	 */
	public static Account1 createNegativeAccount(String iban, double balance, double credit, Customer owner) {
		return new Account1(iban, balance, credit, owner, true, false, false);
	}

	/**
	 * Creates an account in a frozen state
	 */
	public static Account1 createFrozenAccount(String iban, double balance, double credit, Customer owner) {
		return new Account1(iban, balance, credit, owner, false, false, false);
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
