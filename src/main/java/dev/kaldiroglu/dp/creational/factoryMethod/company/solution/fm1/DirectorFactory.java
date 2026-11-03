package dev.kaldiroglu.dp.creational.factoryMethod.company.solution.fm1;

public class DirectorFactory implements Factory {

	@Override
	public Director create() {
		Director director = new Director(EmployeeRandomizer.createId(), EmployeeRandomizer.createName(),
				EmployeeRandomizer.createYear(), "Management", 40, 5000);
		return director;
	}

}
