package dev.kaldiroglu.dp.creational.ch01.employee.better;

import java.time.LocalDate;

public class JobInfo {
    private String employeeId;
    private String department;
    private String position;
    private String manager;
    private LocalDate hireDate;
    private String employmentType; // Full-time, Part-time, Contract, Intern
    private String securityClearance;


    public JobInfo(String employeeId, String department, String position, String manager,
                   String securityClearance) {
        this.employeeId = (employeeId == null) ? "" : employeeId;
        this.department = (department == null) ? "" : department;
        this.position = (position == null) ? "" : position;
        this.manager = (manager == null) ? "" : manager;
        this.hireDate = LocalDate.now();
        this.employmentType = "Full-time";
        this.securityClearance = (securityClearance == null) ? "" : securityClearance;
    }

    public JobInfo(String employeeId, String department, String position, String manager,
                   LocalDate hireDate, String employmentType, String securityClearance) {
        this.employeeId = (employeeId == null) ? "" : employeeId;
        this.department = (department == null) ? "" : department;
        this.position = (position == null) ? "" : position;
        this.manager = (manager == null) ? "" : manager;
        this.hireDate = (hireDate == null) ? LocalDate.now() : hireDate;
        this.employmentType = (employmentType == null) ? "Full-time" : employmentType;
        this.securityClearance = (securityClearance == null) ? "" : securityClearance;
    }

    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public String getManager() { return manager; }
    public void setManager(String manager) { this.manager = manager; }

    public LocalDate getHireDate() { return hireDate; }
    public void setHireDate(LocalDate hireDate) { this.hireDate = hireDate; }

    public String getEmploymentType() { return employmentType; }
    public void setEmploymentType(String employmentType) { this.employmentType = employmentType; }

    public String getSecurityClearance() { return securityClearance; }
    public void setSecurityClearance(String securityClearance) { this.securityClearance = securityClearance; }
}