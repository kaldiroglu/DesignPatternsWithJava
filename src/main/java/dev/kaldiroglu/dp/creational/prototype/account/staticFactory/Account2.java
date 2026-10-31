
package dev.kaldiroglu.dp.creational.prototype.account.staticFactory;

import dev.kaldiroglu.dp.creational.prototype.account.customer.Customer;

public class Account2 {
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
	private Account2(String iban, double balance, double credit, Customer owner, boolean openToWithdraw,
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
	private Account2(String iban, double balance, Customer owner, boolean openToWithdraw,
                     boolean openToPayment, boolean openToTransfer) {
		this.iban = iban;
		this.balance = balance;
		this.owner = owner;
		this.openToWithdraw = openToWithdraw;
		this.openToPayment = openToPayment;
		this.openToTransfer = openToTransfer;
	}

	/**
	 * Creates a new Account2 object by copying the specified Account2 instance.
	 * The new account will have the same balance, credit, and access properties as the
	 * specified account but with a default IBAN and no owner assigned.
	 *
	 * @param other the Account2 instance to copy. Must not be null.
	 * @return a new Account2 instance with the same balance, credit, and access properties as the specified account.
	 * @throws IllegalArgumentException if the specified account is null.
	 */
	public static Account2 copy(Account2 other) {
		if (other == null) {
			throw new IllegalArgumentException("other must not be null");
		}
		// Make sure that a new id and owner will be assigned
		Account2 newAccount = new Account2("-", other.balance, other.credit, null, other.openToWithdraw, other.openToPayment, other.openToTransfer);
		return newAccount;
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
