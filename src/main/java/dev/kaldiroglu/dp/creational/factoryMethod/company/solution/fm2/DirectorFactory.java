package dev.kaldiroglu.dp.creational.factoryMethod.company.solution.fm2;

public class DirectorFactory implements Factory {

	@Override
	public Director create(String name) {
		Director director = new Director(EmployeeRandomizer.createId(), name,
				EmployeeRandomizer.createYear(), "Management", 40, 5000);
		return director;
	}

}
