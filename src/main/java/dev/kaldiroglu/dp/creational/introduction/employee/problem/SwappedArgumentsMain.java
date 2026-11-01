package dev.kaldiroglu.dp.creational.introduction.employee.problem;

public class SwappedArgumentsMain {
    public static void main(String[] args) {
        // The last two arguments are the wrong way around. All four are String, so this compiles.
        Employee sarah = new Employee("Sarah", "Johnson", "Engineering", "sarah.johnson@company.com");

        System.out.println("Email     : " + sarah.getEmail());
        System.out.println("Department: " + sarah.getDepartment());
    }
}
