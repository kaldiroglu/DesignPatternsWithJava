package dev.kaldiroglu.dp.creational.factories.factoryMethod.solution.company.variety1;

public class DirectorFactory extends EmployeeFactory {

	@Override
	public Director create() {
		Director director = new Director(EmployeeRandomizer.createId(), EmployeeRandomizer.createName(),
				EmployeeRandomizer.createYear(), "Management", "Management", 5000);
		return director;
	}

}
