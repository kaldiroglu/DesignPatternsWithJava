package dev.kaldiroglu.dp.creational.factoryMethod.company.problem.stageOne;

public class Manager extends Employee {
    protected int teamSize;

    public static final int MANAGEMENT_PAYMENT = 3000;

    public double calculateSalary() {
        salary = super.calculateSalary() + MANAGEMENT_PAYMENT;
        return salary;
    }

    public void setTeamSize(int teamSize) {
        this.teamSize = teamSize;
    }

    public int getTeamSize() {
        return teamSize;
    }
}
