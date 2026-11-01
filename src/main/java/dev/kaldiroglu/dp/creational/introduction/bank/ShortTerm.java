package dev.kaldiroglu.dp.creational.introduction.bank;

public class ShortTerm extends Term {

    public ShortTerm(int months) {
        if (months > 12) {
            throw new IllegalArgumentException("A short term is at most 12 months");
        }
        super(months);
    }
}
