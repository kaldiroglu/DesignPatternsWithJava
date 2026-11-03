package dev.kaldiroglu.dp.creational.factoryMethod.company.solution.fm3;

public class Director extends Manager {
	protected double bonus;
	
	public Director(int no, String name, int year, String department, int teamSize, double bonus) {
		super(no, name, year, department, teamSize);
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
	
	public double calculateSalary(){
		salary = super.calculateSalary() + bonus;
		return salary;
	}
	
	public double getBonus() {
		return bonus;
	}
	
	public void printInfo(){
		System.out.println("\nDirector Info");
		super.printInfo();
	}

	@Override
	public String toString() {
		return "Director [bonus=" + bonus + ", teamSize=" + teamSize + ", no=" + no + ", name=" + name
				+ ", year=" + year + ", department=" + department + ", salary=" + salary + "]";
	}
}
