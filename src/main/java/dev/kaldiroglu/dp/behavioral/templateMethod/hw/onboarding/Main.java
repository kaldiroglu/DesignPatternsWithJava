package dev.kaldiroglu.dp.behavioral.templateMethod.hw.onboarding;

/**
 * The first day of an employee and of a contractor. The contractor overrides the
 * equipment hook, so the contractor gets no laptop.
 */
public final class Main {

    public static void main(String[] args) {
        System.out.println("Employee Elif:   " + new EmployeeOnboarding().start("Elif"));
        System.out.println("Contractor Mert: " + new ContractorOnboarding().start("Mert"));
    }
}
