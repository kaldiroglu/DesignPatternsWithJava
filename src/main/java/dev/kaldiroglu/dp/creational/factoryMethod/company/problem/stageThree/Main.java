package dev.kaldiroglu.dp.creational.factoryMethod.company.problem.stageThree;

import dev.kaldiroglu.dp.creational.factoryMethod.company.problem.stageTwo.Employee;
import dev.kaldiroglu.dp.creational.factoryMethod.company.problem.stageTwo.PayrollOffice;

public class Main {

    public static void main(String[] args) {
        HR hr = new HR();

        hr.add("Employee", 1, "Ahmet", 10, "Production");
        hr.add("Employee", 2, "Zeynep", 3, "Sales");
        hr.add("Employee", 3, "Kemal", 7, "Production");

        hr.add("Manager", 4, "Mehmet", 14, "Production");
        hr.add("Manager", 5, "Sami", 2, "Marketing");

        hr.add("Director", 7, "Ahmet", 19, "Production");

        PayrollOffice po = new PayrollOffice();
        for (Employee employee : hr.getEmployees())
            po.paySalary(employee);
    }
}
