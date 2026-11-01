package dev.kaldiroglu.dp.creational.introduction.employee.problem;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SettersMain {
    public static void main(String[] args) {
        // Example usage scenarios showing different meaningful states:

        // Scenario 1: New college graduate hire
        Employee newGrad = new Employee();
        newGrad.setFirstName("Sarah");
        newGrad.setLastName("Johnson");
        newGrad.setEmail("sarah.johnson@company.com");
        newGrad.setDepartment("Engineering");
        newGrad.setPosition("Junior Software Developer");
        newGrad.setEmployeeId("ENG2024001");
        newGrad.setEducation("BS Computer Science, MIT");
        newGrad.setEmploymentType("Full-time");
        newGrad.setSalary(new BigDecimal("75000"));

        // Scenario 2: Senior executive with security clearance
        Employee executive = new Employee();
        executive.setFirstName("Michael");
        executive.setLastName("Chen");
        executive.setEmail("m.chen@company.com");
        executive.setDepartment("Defense Contracts");
        executive.setPosition("Senior Director");
        executive.setEmployeeId("EXE2020045");
        executive.setHireDate(LocalDate.of(2020, 3, 15));
        executive.setSalary(new BigDecimal("180000"));
        executive.setSecurityClearance("Top Secret");
        executive.setWorkLocation("Washington DC");
        executive.setPreviousCompany("Lockheed Martin");
        executive.setSkills("Project Management, Government Relations, Systems Architecture");

        // Scenario 3: Remote contractor
        Employee contractor = new Employee();
        contractor.setFirstName("Elena");
        contractor.setLastName("Rodriguez");
        contractor.setEmail("elena.contractor@company.com");
        contractor.setDepartment("Marketing");
        contractor.setPosition("UI/UX Designer");
        contractor.setEmploymentType("Contract");
        contractor.setRemoteWorker(true);
        contractor.setTimeZone("PST");
        contractor.setWorkLocation("Remote - California");
        contractor.setSkills("Figma, Adobe Creative Suite, User Research");
        contractor.setHireDate(LocalDate.of(2024, 1, 10));

        // Scenario 4: Part-time intern
        Employee intern = new Employee();
        intern.setFirstName("Alex");
        intern.setLastName("Kim");
        intern.setEmail("alex.kim.intern@company.com");
        intern.setDepartment("Data Science");
        intern.setPosition("Data Analyst Intern");
        intern.setEmploymentType("Intern");
        intern.setManager("Dr. Lisa Wang");
        intern.setEducation("MS Data Science, Stanford (Expected 2025)");
        intern.setHireDate(LocalDate.of(2024, 6, 1));
        intern.setWorkLocation("Palo Alto Office");

        // Scenario 5: International employee transferring
        Employee international = new Employee();
        international.setFirstName("Raj");
        international.setLastName("Patel");
        international.setEmail("raj.patel@company.com");
        international.setDepartment("Global Operations");
        international.setPosition("Operations Manager");
        international.setEmployeeId("OPS2023012");
        international.setHireDate(LocalDate.of(2023, 9, 1));
        international.setSalary(new BigDecimal("95000"));
        international.setWorkLocation("London Office");
        international.setTimeZone("GMT");
        international.setPreviousCompany("Company India Subsidiary");
        international.setEmergencyContact("Priya Patel (Spouse)");
        international.setEmergencyPhone("+44-20-1234-5678");

        // Scenario 6: Returning employee (rehire)
        Employee rehire = new Employee();
        rehire.setFirstName("Jennifer");
        rehire.setLastName("Thompson");
        rehire.setEmail("jennifer.thompson@company.com");
        rehire.setDepartment("Product Management");
        rehire.setPosition("Senior Product Manager");
        rehire.setEmployeeId("PM2018003"); // Keeping old ID
        rehire.setHireDate(LocalDate.of(2024, 7, 15)); // New hire date
        rehire.setSalary(new BigDecimal("125000"));
        rehire.setPreviousCompany("Meta"); // Where they worked between
        rehire.setSkills("Product Strategy, A/B Testing, SQL, Python");
        rehire.setManager("VP Product");

        Employee forgotten = new Employee();

        System.out.println(newGrad);
        System.out.println(executive);
        System.out.println(contractor);
        System.out.println(intern);
        System.out.println(international);
        System.out.println(rehire);
        System.out.println(forgotten);
    }
}
