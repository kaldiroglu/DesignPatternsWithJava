package dev.kaldiroglu.dp.creational.factoryMethod.company.problem.stageThree;

import dev.kaldiroglu.dp.creational.factoryMethod.company.problem.stageTwo.Employee;

/** Mehmet manages 12 people, but add has no parameter for a team size. */
public class TeamSizeMain {

    public static void main(String[] args) {
        HR hr = new HR();
        hr.add("Manager", 4, "Mehmet", 14, "Production");

        for (Employee employee : hr.getEmployees())
            employee.work();
    }
}
