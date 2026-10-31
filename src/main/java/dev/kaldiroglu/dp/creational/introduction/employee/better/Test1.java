package dev.kaldiroglu.dp.creational.ch01.employee.better;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Test1 {
    public static void main(String[] args) {
        // 1) Office full-time employee
        Employee officeFullTime = new Employee(
                new PersonalInfo("Sarah", "Johnson", LocalDate.of(2000, 2, 14),
                        "BS Computer Science", "Java, Spring", "N/A"),
                new ContactInfo("sarah.johnson@company.com", "+1-555-0101", "Jamie Johnson", "+1-555-0199"),
                new JobInfo("ENG2024001", "Engineering", "Junior Software Developer", "Pat Manager",
                        LocalDate.of(2024, 7, 1), "Full-time", ""),
                new WorkInfo("NYC Office", false, "EST"),
                new Compensation(new BigDecimal("75000"))
        );

        // 2) Remote contractor
        Employee remoteContractor = new Employee(
                new PersonalInfo("Elena", "Rodriguez", LocalDate.of(1996, 11, 3),
                        "BA Design", "Figma, UX Research", "Freelance"),
                new ContactInfo("elena.contractor@company.com", "+1-555-0142", "", ""),
                new JobInfo("", "Marketing", "UI/UX Designer", "Creative Director",
                        LocalDate.of(2024, 1, 10), "Contract", ""),
                WorkInfo.remote("PST"),
                new Compensation(new BigDecimal("0"))
        );

        // 3) Intern (limited job scope)
        Employee intern = new Employee(
                new PersonalInfo("Alex", "Kim", LocalDate.of(2003, 5, 22),
                        "MS Data Science (Expected)", "Python, SQL", ""),
                new ContactInfo("alex.kim.intern@company.com", "", "", ""),
                new JobInfo("", "Data Science", "Data Analyst Intern", "Dr. Lisa Wang",
                        LocalDate.of(2024, 6, 1), "Intern", ""),
                new WorkInfo("Palo Alto Office", false, "PST"),
                new Compensation(new BigDecimal("25000"))
        );

        // 4) Executive with clearance + high comp
        Employee executive = new Employee(
                new PersonalInfo("Michael", "Chen", LocalDate.of(1982, 9, 9),
                        "MBA", "Leadership, Strategy", "Industry"),
                new ContactInfo("m.chen@company.com", "+1-555-0100", "", ""),
                new JobInfo("EXE2020045", "Defense Contracts", "Senior Director", "CEO",
                        LocalDate.of(2020, 3, 15), "Full-time", "Top Secret"),
                new WorkInfo("Washington DC", false, "EST"),
                new Compensation(new BigDecimal("180000"))
        );

        printSummary("officeFullTime", officeFullTime);
        printSummary("remoteContractor", remoteContractor);
        printSummary("intern", intern);
        printSummary("executive", executive);
    }

    private static void printSummary(String label, Employee e) {
        System.out.printf(
                "%s: %s %s | %s | %s | %s | remote=%s | tz=%s | salary=%s%n",
                label,
                e.getPersonal().getFirstName(),
                e.getPersonal().getLastName(),
                e.getJob().getDepartment(),
                e.getJob().getPosition(),
                e.getJob().getEmploymentType(),
                e.getWork().isRemoteWorker(),
                e.getWork().getTimeZone(),
                e.getCompensation().getSalary()
        );
    }
}
