package dev.kaldiroglu.dp.creational.factoryMethod.company.problem.stageOne;

/** An employee created empty and then filled in with setters. */
public class Employee {
    protected int no;
    protected String name;
    protected int year;
    protected String department;
    protected double salary;

    public static final int BASE_SALARY = 500;

    public double calculateSalary() {
        salary = year * BASE_SALARY;
        return salary;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getNo() {
        return no;
    }

    public String getName() {
        return name;
    }

    public int getYear() {
        return year;
    }

    public String getDepartment() {
        return department;
    }

    public double getSalary() {
        return salary;
    }
}
