package dev.kaldiroglu.dp.creational.introduction.bank;

public class ShortTermMain {
    public static void main(String[] args) {
        System.out.println("Months: " + new ShortTerm(6).getMonths());

        try {
            new ShortTerm(36);
        } catch (IllegalArgumentException e) {
            System.out.println("Rejected: " + e.getMessage());
        }
    }
}
