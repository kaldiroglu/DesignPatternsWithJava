package dev.kaldiroglu.dp.creational.factories.factoryMethod.solution.company.fm1;

public class DirectorFactory implements Factory {

	@Override
	public Director create() {
		Director director = new Director(EmployeeRandomizer.createId(), EmployeeRandomizer.createName(),
				EmployeeRandomizer.createYear(), "Management", "Management", 100_000);
		return director;
	}

}
