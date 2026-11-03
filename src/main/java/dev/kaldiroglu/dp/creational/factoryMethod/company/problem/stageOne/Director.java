package dev.kaldiroglu.dp.creational.factoryMethod.company.problem.stageOne;

public class Director extends Manager {
    protected double bonus;

    public double calculateSalary() {
        salary = super.calculateSalary() + bonus;
        return salary;
    }

    public void setBonus(double bonus) {
        this.bonus = bonus;
    }

    public double getBonus() {
        return bonus;
    }
}
