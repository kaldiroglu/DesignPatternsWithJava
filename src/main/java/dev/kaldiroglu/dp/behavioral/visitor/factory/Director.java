package dev.kaldiroglu.dp.behavioral.visitor.factory;


public class Director extends Manager {
	protected int bonus;
	
	public Director(int no, String name, int year, String workingDepartment, String managingDepartment, int bonus) {
		super(no, name, year, workingDepartment, managingDepartment);
		this.bonus = bonus;
	}
	
	public void work(){
		System.out.println("Director is working!");
		manage();
	}
	
	public void manage(){
		System.out.println("Director manages whole company!");
		makeAStrategicPlan();
	}
	
	public void makeAStrategicPlan(){
		System.out.println("Director makes a strategic plan for the company!");
	}
	
	public int calculateSalary(){
		return super.calculateSalary() + MANAGEMENT_PAYMENT + bonus;
	}
	
	public void printInfo(){
		System.out.println("\nDirector Info");
		super.printInfo();
	}
}
