package dev.kaldiroglu.dp.creational.factoryMethod.company.solution.fm1;

public class Manager extends Employee{
	protected int teamSize;
	
	public static final int MANAGEMENT_PAYMENT = 20_000;
	
	public Manager(int no, String name, int year, String department, int teamSize){
		super(no, name, year, department);
		this.teamSize = teamSize;
	}
	
	public void work(){
		System.out.println("Manager is working!");
		manage();
	}
	
	public void manage(){
		System.out.println("Manager manages a team of " + teamSize);
	}
	
	public double calculateSalary(){
		salary = super.calculateSalary() + MANAGEMENT_PAYMENT;
		return salary;
	}
	
	public int getTeamSize() {
		return teamSize;
	}
	
	public void printInfo(){
		super.printInfo();
		System.out.println("Team size: " + teamSize);
	}

	@Override
	public String toString() {
		return "Manager [teamSize=" + teamSize + ", no=" + no + ", name=" + name + ", year=" + year
				+ ", department=" + department + ", salary=" + salary + "]";
	}
}
