package dev.kaldiroglu.dp.creational.ch01.employee.problem;

import java.time.LocalDate;
import java.math.BigDecimal;

public class Employee {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String department;
    private String position;
    private String employeeId;
    private LocalDate hireDate;
    private LocalDate birthDate;
    private BigDecimal salary;
    private String manager;
    private String workLocation;
    private String employmentType; // Full-time, Part-time, Contract, Intern
    private String securityClearance;
    private String emergencyContact;
    private String emergencyPhone;
    private boolean isRemoteWorker;
    private String timeZone;
    private String skills;
    private String education;
    private String previousCompany;

    // Default constructor for post-construction initialization
    public Employee() {
        this.hireDate = LocalDate.now();
        this.employmentType = "Full-time";
        this.isRemoteWorker = false;
        this.timeZone = "EST";
    }

    // Imagine trying to create constructors for all meaningful combinations:

    // Basic employee constructor
    public Employee(String firstName, String lastName, String email, String department) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.department = department;
    }

    // New hire constructor
    public Employee(String firstName, String lastName, String email, String department,
                    String position, LocalDate hireDate) {
        this(firstName, lastName, email, department);
        this.position = position;
        this.hireDate = hireDate;
    }

    // Full employee constructor - but this is getting unwieldy!
    public Employee(String firstName, String lastName, String email, String phoneNumber,
                    String department, String position, String employeeId, LocalDate hireDate,
                    BigDecimal salary, String manager) {
        this(firstName, lastName, email, department, position, hireDate);
        this.phoneNumber = phoneNumber;
        this.employeeId = employeeId;
        this.salary = salary;
        this.manager = manager;
    }

    // We'd need hundreds more constructors for different combinations!
    // What about contractors? Remote workers? Interns with different fields?

    // Setter methods for post-construction initialization
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public void setWorkLocation(String workLocation) {
        this.workLocation = workLocation;
    }

    public void setEmploymentType(String employmentType) {
        this.employmentType = employmentType;
    }

    public void setSecurityClearance(String securityClearance) {
        this.securityClearance = securityClearance;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public void setEmergencyPhone(String emergencyPhone) {
        this.emergencyPhone = emergencyPhone;
    }

    public void setRemoteWorker(boolean remoteWorker) {
        this.isRemoteWorker = remoteWorker;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public void setPreviousCompany(String previousCompany) {
        this.previousCompany = previousCompany;
    }
}

/*
Why constructors would be a nightmare for this domain object:

1. DIFFERENT EMPLOYEE TYPES: New grads, executives, contractors, interns,
   international transfers, rehires - each has different relevant fields.

2. WORKFLOW DIFFERENCES:
   - New hires: Basic info first, then HR adds employee ID, salary later
   - Contractors: No employee ID or salary initially
   - Interns: Limited fields, temporary data
   - Transfers: Some fields carry over, others are new

3. TEMPORAL ASPECTS: Employee data is built up over time:
   - Initial application: Name, email, position
   - Background check: Previous company, education
   - Onboarding: Employee ID, manager, work location
   - Payroll setup: Salary, emergency contacts

4. OPTIONAL COMBINATIONS:
   - Remote workers need timezone, not work location
   - Government contractors need security clearance
   - Part-time workers might not have all benefits fields
   - International employees need different emergency contact formats

5. BUSINESS RULES: Different combinations are valid for different scenarios,
   and trying to encode all these rules into constructors would be impossible.

The post-construction approach perfectly models how employee data is actually
collected and managed in real business processes - gradually, with different
fields relevant at different times and for different types of employees.
*/