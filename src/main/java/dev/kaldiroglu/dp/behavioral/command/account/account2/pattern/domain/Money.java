package dev.kaldiroglu.dp.behavioral.command.account.account2.pattern.domain;

import dev.kaldiroglu.dp.behavioral.command.account.account2.pattern.ex.IllegalMoneyException;
import dev.kaldiroglu.dp.behavioral.command.account.account2.pattern.ex.InsufficientBalanceException;

import java.math.BigDecimal;
import java.math.BigInteger;

final public class Money {
    private final BigDecimal value;

    private Money(BigDecimal value) {
        this.value = value;
    }

    public static Money of(BigDecimal value) throws IllegalMoneyException {
        validate(value);
        return new Money(value);
    }

    private static void validate(BigDecimal value) throws IllegalMoneyException {
        if (value.compareTo(BigDecimal.ZERO) < 0 || value.compareTo(new BigDecimal(new BigInteger("1000000"))) > 0)
            throw new IllegalMoneyException();
    }

    public BigDecimal value() {
        return value;
    }

    public Money add(Money amount) {
        return deposit(amount);
    }

    public synchronized boolean withdrawable(Money amount) throws InsufficientBalanceException {
        boolean b = false;
        if (value.compareTo(amount.value) >= 0)
            b = true;
        else throw new InsufficientBalanceException(value.doubleValue());
        return b;
    }

    public synchronized Money withdraw(Money amount) {
        BigDecimal newValue = value.subtract(amount.value);
        return new Money(newValue);
    }

    public synchronized Money deposit(Money amount) {
        BigDecimal newValue = value.add(amount.value);
        return new Money(newValue);
    }

//    @Override
//    public boolean equals(Object o) {
//        if (o == null || getClass() != o.getClass()) return false;
//        Money money = (Money) o;
//        return Objects.equals(value, money.value);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(value);
//    }
//
//    @Override
//    public String toString() {
//        return "Money{" +
//                "value=" + value +
//                '}';
//    }
}
