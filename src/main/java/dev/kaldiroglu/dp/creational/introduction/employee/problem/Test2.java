package dev.kaldiroglu.dp.creational.ch01.employee.problem;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Test2 {
    public static void main(String[] args) {

        // 1) Default constructor (uses defaults: hireDate=now, employmentType="Full-time", etc.)
        Employee e0 = new Employee();

        // 2) Basic employee constructor
        Employee e1 = new Employee(
                "Sarah",
                "Johnson",
                "sarah.johnson@company.com",
                "Engineering"
        );

        // 3) New hire constructor (adds position + explicit hireDate)
        Employee e2 = new Employee(
                "Alex",
                "Kim",
                "alex.kim@company.com",
                "Data Science",
                "Data Analyst Intern",
                LocalDate.of(2024, 6, 1)
        );

        // 4) Full employee constructor (the long one)
        Employee e3 = new Employee(
                "Michael",
                "Chen",
                "m.chen@company.com",
                "+1-202-555-0100",
                "Defense Contracts",
                "Senior Director",
                "EXE2020045",
                LocalDate.of(2020, 3, 15),
                new BigDecimal("180000"),
                "CEO"
        );

        // 5) Another “full” example (different data, same constructor)
        Employee e4 = new Employee(
                "Elena",
                "Rodriguez",
                "elena.rodriguez@company.com",
                "+1-415-555-0142",
                "Marketing",
                "UI/UX Designer",
                "MKT2024019",
                LocalDate.of(2024, 1, 10),
                new BigDecimal("95000"),
                "Creative Director"
        );
    }
}
